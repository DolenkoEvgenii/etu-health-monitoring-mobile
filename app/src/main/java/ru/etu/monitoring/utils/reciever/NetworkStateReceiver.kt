package ru.etu.monitoring.utils.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.greenrobot.eventbus.EventBus
import ru.etu.monitoring.model.event.GpsSettingsChangedEvent

class NetworkStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.extras != null) {
            EventBus.getDefault().post(GpsSettingsChangedEvent())
        }
    }
}