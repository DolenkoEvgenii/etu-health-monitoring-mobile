package com.mvp.mvptemplate.presentation.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseMvpView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoadingDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeLoadingDialog()
}
