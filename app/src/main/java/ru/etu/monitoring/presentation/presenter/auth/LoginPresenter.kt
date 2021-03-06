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
                    if (it.isExistUser) {
                        router.navigateTo(Screens.ConfirmLoginFragmentScreen(phone))
                    } else {
                        router.navigateTo(Screens.SignUpFragmentScreen(phone))
                    }
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }
}
