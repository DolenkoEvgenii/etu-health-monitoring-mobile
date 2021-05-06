package ru.etu.monitoring.presentation.presenter.auth

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.auth.SignUpView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class SignUpPresenter(val phone: String) : BasePresenter<SignUpView>() {
    private val router: Router by inject()
    private val userRepository: UserRepository by inject()

    fun onSignUpClick(firstName: String, lastName: String, middleName: String, birthday: String, code: String) {
        signUp(firstName, lastName, middleName, birthday, code, phone)
    }

    private fun signUp(firstName: String, lastName: String, middleName: String, birthday: String, code: String, phone: String) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            userRepository
                .signUp(firstName, lastName, middleName, birthday, code, phone)
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
}
