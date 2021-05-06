package com.mvp.mvptemplate.ui.activity.base

import android.os.Bundle
import com.mvp.mvptemplate.R
import com.mvp.mvptemplate.presentation.presenter.SimplePresenter
import com.mvp.mvptemplate.presentation.view.BaseMvpView
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
