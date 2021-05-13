package ru.etu.monitoring.presentation.presenter.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.main.MainView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {
    private val userRepository: UserRepository by inject()
    private val router: Router by inject()

    fun onImIllClick() {
        router.navigateTo(Screens.CreateIllnessFragmentScreen())
    }

    fun onResume() {
        loadProfile()
    }

    private fun loadProfile() {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            userRepository
                .getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()
                    viewState.bindProfile(it)
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }
}
