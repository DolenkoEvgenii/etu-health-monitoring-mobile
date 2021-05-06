package com.mvp.mvptemplate.utils.manager.permission

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.LifecycleTransformer
import io.reactivex.Observable

object PermissionManager {
    @SuppressLint("CheckResult")
    fun isPermissionsGranted(context: Context?, vararg permissions: String): Boolean {
        context ?: return false

        return Observable.fromIterable(permissions.toList())
                .map { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
                .all { isGranted -> isGranted }
                .blockingGet()
    }

    @SuppressLint("CheckResult")
    fun requestPermissions(rxPermissions: RxPermissions, transformer: LifecycleTransformer<Boolean>,
                           permissions: Array<String>): Observable<Boolean> {

        return rxPermissions.request(*permissions)
                .compose(transformer)
    }
}