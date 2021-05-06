package ru.etu.monitoring.presentation.presenter

import org.koin.core.component.inject
import ru.etu.monitoring.presentation.view.BaseMvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class SimplePresenter constructor(
    private val routeScreen: SupportAppScreen,
    private val isRoot: Boolean
) : BasePresenter<BaseMvpView>() {

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
