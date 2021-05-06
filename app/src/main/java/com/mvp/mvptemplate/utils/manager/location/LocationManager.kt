package com.mvp.mvptemplate.utils.manager.location

import android.content.Context
import com.mvp.mvptemplate.utils.manager.permission.PermissionManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.LifecycleTransformer
import io.reactivex.Single

object LocationManager {
    fun checkLocationPermissions(context: Context?, rxPermissions: RxPermissions,
                                 transformer: LifecycleTransformer<Boolean>): Single<Boolean> {

        return Single.create<Boolean> { emitter ->
            val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (PermissionManager.isPermissionsGranted(context, *permissions)) {
                emitter.onSuccess(true)
            } else {
                PermissionManager.requestPermissions(rxPermissions, transformer, permissions)
                        .subscribe { res -> emitter.onSuccess(res) }
            }
        }
    }
}