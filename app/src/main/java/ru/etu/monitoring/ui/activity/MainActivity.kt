package ru.etu.monitoring.ui.activity

import android.os.Bundle
import android.util.Log
import org.koin.android.ext.android.inject
import ru.etu.monitoring.R
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.ui.activity.base.BaseMvpFragmentActivity

class MainActivity : BaseMvpFragmentActivity() {
    val userRepo: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("lambada", userRepo.isAuthorized.toString())
    }
}
