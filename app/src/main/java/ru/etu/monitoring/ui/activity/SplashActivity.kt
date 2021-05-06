package ru.etu.monitoring.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.etu.monitoring.R
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.ui.activity.base.BaseMvpFragmentActivity
import ru.terrakok.cicerone.Router


class SplashActivity : BaseMvpFragmentActivity() {
    private val userRepo: UserRepository by inject()
    private val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideStatusBar()
        lifecycleScope.launch {
            ivIcon.start()
            delay(2400)

            if (userRepo.isAuthorized) {
                router.newRootScreen(Screens.MainActivityScreen())
            } else {
                router.newRootScreen(Screens.MainActivityScreen())
            }
            ivIcon.stop()
        }
    }

    private fun hideStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.WHITE
    }
}
