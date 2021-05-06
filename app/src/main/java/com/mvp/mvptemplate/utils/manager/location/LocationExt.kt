package com.mvp.mvptemplate.utils.manager.location

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.mvp.mvptemplate.ui.activity.base.BaseMvpFragmentActivity
import com.mvp.mvptemplate.ui.fragment.BaseMvpFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.Single

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
    return LocationManager.checkLocationPermissions(this.context, com.tbruyelle.rxpermissions2.RxPermissions(this), bindUntilEvent(com.trello.rxlifecycle3.android.FragmentEvent.DESTROY))
}

fun BaseMvpFragment.checkLocationSettings(): Observable<Boolean> {
    val locationSettingsRequest = com.google.android.gms.location.LocationSettingsRequest.Builder()
            .addLocationRequest(com.google.android.gms.location.LocationRequest.create().setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY))
            .build()

    val activity = activity ?: return io.reactivex.Observable.just(false)
    return RxLocationSettings.with(activity).ensure(locationSettingsRequest)
}