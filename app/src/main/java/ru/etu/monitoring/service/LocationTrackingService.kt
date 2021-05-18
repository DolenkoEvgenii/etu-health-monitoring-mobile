package ru.etu.monitoring.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import ru.etu.monitoring.R
import ru.etu.monitoring.model.network.patient.PatientRepository
import ru.etu.monitoring.ui.activity.SplashActivity
import ru.etu.monitoring.utils.helpers.activityManager
import ru.etu.monitoring.utils.helpers.notificationManager
import java.util.*
import java.util.concurrent.TimeUnit


class LocationTrackingService : Service(), KoinComponent {
    private val patientRepository: PatientRepository by inject()

    private val myBinder = MyLocalBinder()
    private var compositeDisposable = CompositeDisposable()

    inner class MyLocalBinder : Binder() {
        fun getService(): LocationTrackingService {
            return this@LocationTrackingService
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(applicationContext)
        startListenForLocation()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return myBinder
    }

    @SuppressLint("MissingPermission")
    fun startListenForLocation() {
        val req = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(TimeUnit.SECONDS.toMillis(50))

        val locationProvider = ReactiveLocationProvider(this)
        val locationDisposable = locationProvider.getUpdatedLocation(req)
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe({
                sendLocation(it)
            }, {
                it.printStackTrace()
            })

        compositeDisposable.add(locationDisposable)
    }

    private fun sendLocation(location: Location) {
        val disposable = patientRepository.sendMyGeo(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Location Tracker", "cords sent")
            }, {
                Log.d("Location Tracker", "cords send error")
            })

        compositeDisposable.add(disposable)
    }

    // Notification Part
    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.notificationManager

            val importance = IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = "notification channel description"
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    private fun startForeground(context: Context) {
        val title = "Вы на карантине"
        val message = "Идет трекинг вашей геопозиции"

        createChannel(context)
        val notifyIntent = Intent(context, SplashActivity::class.java)

        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("message", message)
        notifyIntent.putExtra("notification", true)

        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mNotification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotification = Notification.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_gps)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentTitle(title)
                .setStyle(Notification.BigTextStyle().bigText(message))
                .setContentText(message).build()

        } else {
            mNotification = Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_gps)
                .setAutoCancel(false)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setStyle(Notification.BigTextStyle().bigText(message))
                .setContentText(message).build()
        }

        startForeground(999, mNotification)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "etu health MAIN_CHANNEL_ID"
        const val CHANNEL_NAME = "LocationTrackerChannel"

        fun isServiceRunning(context: Context): Boolean {
            val activityManager = context.activityManager

            activityManager?.getRunningServices(Integer.MAX_VALUE)?.forEach { service ->
                if (LocationTrackingService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}
