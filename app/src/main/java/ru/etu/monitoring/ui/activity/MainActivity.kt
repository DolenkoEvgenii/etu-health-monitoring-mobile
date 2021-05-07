package ru.etu.monitoring.ui.activity

import android.os.Bundle
import org.koin.android.ext.android.inject
import ru.etu.monitoring.R
import ru.etu.monitoring.Screens
import ru.etu.monitoring.ui.activity.base.BaseMvpFragmentActivity
import ru.terrakok.cicerone.Router

class MainActivity : BaseMvpFragmentActivity() {
    val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.MainFragmentScreen())
        }
    }
}
