package ru.etu.monitoring.presentation.presenter

import ru.etu.monitoring.presentation.view.BaseMvpView
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class SimplePresenter constructor(
        private val routeScreen: SupportAppScreen,
        private val isRoot: Boolean) : BasePresenter<BaseMvpView>(), KoinComponent {

    private val router: Router by inject()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (isRoot) {
            router.newRootScreen(routeScreen)
        } else {
            router.navigateTo(routeScreen)
        }
    }
}
