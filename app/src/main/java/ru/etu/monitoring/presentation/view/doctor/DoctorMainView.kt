package ru.etu.monitoring.presentation.view.doctor

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.presentation.presenter.doctor.DoctorMainPresenter
import ru.etu.monitoring.presentation.view.BaseMvpView

interface DoctorMainView : BaseMvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRequests(requests: List<Request>, type: DoctorMainPresenter.RequestType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addRequests(requests: List<Request>, type: DoctorMainPresenter.RequestType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearRequests(type: DoctorMainPresenter.RequestType)
}
