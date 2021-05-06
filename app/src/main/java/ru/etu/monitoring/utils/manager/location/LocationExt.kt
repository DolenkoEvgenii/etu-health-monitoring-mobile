package ru.etu.monitoring.utils.manager.location

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.Single
import ru.etu.monitoring.ui.activity.base.BaseMvpFragmentActivity
import ru.etu.monitoring.ui.fragment.BaseMvpFragment

fun BaseMvpFragmentActivity.checkLocationPermissions(): Single<Boolean> {
    return LocationManager.checkLocationPermissions(this, RxPermissions(this), bindUntilEvent(ActivityEvent.DESTROY))
}

fun BaseMvpFragmentActivity.checkLocationSettings(): Observable<Boolean> {
    val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
            .build()

    return RxLocationSettings.with(this).ensure(locationSettingsRequest)
}

fun BaseMvpFragment.checkLocationPermissions(): Single<Boolean> {
    return LocationManager.checkLocationPermissions(this.context, RxPermissions(this), bindUntilEvent(com.trello.rxlifecycle3.android.FragmentEvent.DESTROY))
}

fun BaseMvpFragment.checkLocationSettings(): Observable<Boolean> {
    val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
            .build()

    val activity = activity ?: return Observable.just(false)
    return RxLocationSettings.with(activity).ensure(locationSettingsRequest)
}