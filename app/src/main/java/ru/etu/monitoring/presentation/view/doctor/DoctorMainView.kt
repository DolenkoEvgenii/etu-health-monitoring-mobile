package ru.etu.monitoring.presentation.view.doctor

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.presentation.view.BaseMvpView

interface DoctorMainView : BaseMvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun bindProfile(profile: User)
}
