package com.mvp.mvptemplate

import android.content.Context
import android.content.Intent
import com.mvp.mvptemplate.model.navigator.BaseAppScreen
import com.mvp.mvptemplate.ui.activity.MainActivity

object Screens {
    class MainActivityScreen : BaseAppScreen() {
        override fun getActivityIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
