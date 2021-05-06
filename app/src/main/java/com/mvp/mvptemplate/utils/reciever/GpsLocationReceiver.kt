package com.mvp.mvptemplate.utils.reciever


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mvp.mvptemplate.model.event.GpsSettingsChangedEvent
import org.greenrobot.eventbus.EventBus

class GpsLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null && action.matches("android.location.PROVIDERS_CHANGED".toRegex())) {
            EventBus.getDefault().post(GpsSettingsChangedEvent())
        }
    }
}