package com.mvp.mvptemplate.utils.manager.location

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.LocationSettingsRequest
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

class RxLocationSettings private constructor(activity: FragmentActivity) {
    private val rxLocationProvider: ReactiveLocationProvider = ReactiveLocationProvider(activity.application)
    private val resolutionFragment: ResolutionFragment = ResolutionFragment.from(activity.supportFragmentManager)

    fun ensure(request: LocationSettingsRequest): Observable<Boolean> {
        return this.rxLocationProvider.checkLocationSettings(request).flatMapSingle { result ->
            val status = result.status

            return@flatMapSingle if (status.hasResolution()) {
                resolutionFragment.startResolutionForResult(status.resolution)
            } else {
                Single.just(status.isSuccess)
            }
        }
    }

    class ResolutionFragment : Fragment() {
        private val resolutionResult = PublishSubject.create<Boolean>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            this.retainInstance = true
        }

        override fun onDestroy() {
            super.onDestroy()
            this.resolutionResult.onComplete()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == LOCATION_SETTINGS_REQUEST) {
                val isLocationSettingsEnabled = resultCode == -1
                this.resolutionResult.onNext(isLocationSettingsEnabled)
            }
        }

        fun startResolutionForResult(resolution: PendingIntent): Single<Boolean> {
            try {
                this.startIntentSenderForResult(resolution.intentSender, 21, null as Intent?, 0, 0, 0, null as Bundle?)
                return this.resolutionResult.first(false)
            } catch (var3: IntentSender.SendIntentException) {
                Log.w("RxLocationSettings", "Failed to start resolution for location settings result", var3)
                return Single.just(false)
            }
        }

        companion object {
            private val TAG = "RxLocationSettings"
            private const val LOCATION_SETTINGS_REQUEST = 21

            internal fun from(fragmentManager: FragmentManager): ResolutionFragment {
                var resolutionFragment = fragmentManager.findFragmentByTag("RxLocationSettings") as ResolutionFragment?
                if (resolutionFragment == null) {
                    resolutionFragment = ResolutionFragment()
                    fragmentManager.beginTransaction().add(resolutionFragment, "RxLocationSettings").commit()
                    fragmentManager.executePendingTransactions()
                }

                return resolutionFragment
            }
        }
    }

    companion object {
        fun with(activity: FragmentActivity): RxLocationSettings {
            return RxLocationSettings(activity)
        }
    }
}