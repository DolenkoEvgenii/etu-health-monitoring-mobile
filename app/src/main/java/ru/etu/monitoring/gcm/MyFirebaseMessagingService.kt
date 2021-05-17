package ru.etu.monitoring.gcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.etu.monitoring.R
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.model.preference.UserPreferences
import ru.etu.monitoring.ui.activity.SplashActivity

open class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {
    protected val userRepository: UserRepository by inject()
    protected val userPreferences: UserPreferences by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            sendNotification(it, remoteMessage.data)
        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification, data: Map<String, String>) {
        val intent = Intent(this, SplashActivity::class.java)
        if (data.containsKey(EXTRA_ID)) {
            intent.putExtra(EXTRA_ID, data[EXTRA_ID])
        }
        intent.putExtra(EXTRA_ACTION, data[EXTRA_ACTION])
        createNotificationChannel(this)

        val channelId = getString(R.string.default_notification_channel_id)
        val pendingIntent = PendingIntent.getActivity(this, 12342, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.heart)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        sendFirebaseTokenToServer(newToken)
    }

    @SuppressLint("CheckResult")
    private fun sendFirebaseTokenToServer(token: String) {
        userPreferences.firebaseToken = token
        if (userPreferences.isAuthorized) {
            userRepository.updateFirebaseToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("firebase_token_send", it.firebaseToken)
                }, {
                    it.printStackTrace()
                })
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"

        const val EXTRA_ID = "item_id"
        const val EXTRA_ACTION = "click_action"

        fun createNotificationChannel(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channelId = context.getString(R.string.default_notification_channel_id)
                val name = "Etu Health Main Channel"

                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, name, importance).apply {
                    description = "Etu Health Notifications Channel"
                    enableLights(true)
                    lightColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    setShowBadge(false)
                }

                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }
}