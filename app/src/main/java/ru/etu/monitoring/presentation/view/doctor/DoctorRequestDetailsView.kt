package ru.etu.monitoring.presentation.view.doctor

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.presentation.view.BaseMvpView

interface DoctorRequestDetailsView : BaseMvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTasks(active: List<RequestTask>, done: List<RequestTask>, removed: List<RequestTask>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun bindRequest(request: Request)

}
