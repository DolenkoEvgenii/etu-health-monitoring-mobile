package ru.etu.monitoring.presentation.presenter.auth

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.auth.LoginView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class LoginPresenter : BasePresenter<LoginView>() {
    private val router: Router by inject()
    private val userRepository: UserRepository by inject()

    fun onLoginBtnClicked(phone: String) {
        login(phone)
    }

    private fun login(phone: String) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            userRepository
                .login(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()
                    if (it.isRegistered) {
                        router.navigateTo(Screens.ConfirmLoginScreen(phone))
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
