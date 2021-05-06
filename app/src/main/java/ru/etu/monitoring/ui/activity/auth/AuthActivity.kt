package ru.etu.monitoring.ui.activity.auth

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_auth.*
import ru.etu.monitoring.R
import ru.etu.monitoring.Screens
import ru.etu.monitoring.presentation.presenter.SimplePresenter
import ru.etu.monitoring.ui.activity.base.BaseMvpRootActivity


class AuthActivity : BaseMvpRootActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun providePresenter(): SimplePresenter {
        return SimplePresenter(Screens.LoginFragmentScreen(), true)
    }

    override fun provideAppBar(): AppBarLayout? {
        return appBar
    }
}
