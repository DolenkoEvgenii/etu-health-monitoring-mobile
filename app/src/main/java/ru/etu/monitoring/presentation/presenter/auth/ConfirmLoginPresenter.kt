package ru.etu.monitoring.presentation.presenter.auth

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.auth.ConfirmLoginView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class ConfirmLoginPresenter(val phone: String) : BasePresenter<ConfirmLoginView>() {
    private val router: Router by inject()
    private val userRepository: UserRepository by inject()

    fun onConfirmBtnClicked(code: String) {
        confirmPhone(phone, code)
    }

    fun onResendSmsClick() {
        resendSms(phone)
    }

    private fun confirmPhone(phone: String, code: String) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            userRepository
                .confirmLogin(phone, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()
                    router.newRootScreen(Screens.MainActivityScreen())
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun resendSms(phone: String) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            userRepository
                .login(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()
                    if (it.isRegistered) {
                        router.navigateTo(Screens.`ConfirmLoginScreen`(phone))
                    } else {
                        router.navigateTo(Screens.SignUpScreen(phone))
                    }
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }
}
