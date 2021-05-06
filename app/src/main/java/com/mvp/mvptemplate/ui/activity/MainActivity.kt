package com.mvp.mvptemplate.ui.activity

import android.os.Bundle
import android.util.Log
import com.mvp.mvptemplate.R
import com.mvp.mvptemplate.model.network.user.UserRepository
import com.mvp.mvptemplate.ui.activity.base.BaseMvpFragmentActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope

class MainActivity : BaseMvpFragmentActivity() {
    val userRepo: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("lambada", userRepo.isAuthorized.toString())
    }
}
