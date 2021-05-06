package ru.etu.monitoring.ui.activity.base

import android.os.Bundle
import ru.etu.monitoring.R
import ru.etu.monitoring.presentation.presenter.SimplePresenter
import ru.etu.monitoring.presentation.view.BaseMvpView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

abstract class BaseMvpRootActivity : BaseMvpFragmentActivity(), BaseMvpView {
    @InjectPresenter
    lateinit var presenter: SimplePresenter

    @ProvidePresenter
    abstract fun providePresenter(): SimplePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
    }
}
