package ru.etu.monitoring

import android.content.Context
import android.content.Intent
import ru.etu.monitoring.model.navigator.BaseAppScreen
import ru.etu.monitoring.ui.activity.MainActivity

object Screens {
    class MainActivityScreen : BaseAppScreen() {
        override fun getActivityIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
