package ru.etu.monitoring.presentation.view.main

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.presentation.view.BaseMvpView

interface MainView : BaseMvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun bindProfile(profile: User)
}
