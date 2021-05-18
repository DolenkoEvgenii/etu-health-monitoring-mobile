package ru.etu.monitoring.presentation.view.main

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.presentation.view.BaseMvpView

interface MainPatientView : BaseMvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun bindProfile(profile: User)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTasks(active: List<RequestTask>, done: List<RequestTask>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun bindService()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun startTrackingService()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun stopTrackingService()
}
