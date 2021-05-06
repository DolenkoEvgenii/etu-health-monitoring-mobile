package ru.etu.monitoring.utils.manager.permission

import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import io.reactivex.Observable
import ru.etu.monitoring.ui.activity.base.BaseRxFragmentActivity
import ru.etu.monitoring.ui.fragment.BaseRxFragment

fun BaseRxFragmentActivity.requestPermissions(permission: Array<String>): Observable<Boolean> {
    return PermissionManager.requestPermissions(
            RxPermissions(this),
            RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY),
            permission
    )
}

fun BaseRxFragment.requestPermissions(permission: Array<String>): Observable<Boolean> {
    return PermissionManager.requestPermissions(
            RxPermissions(this),
            RxLifecycle.bindUntilEvent(lifecycle(), FragmentEvent.DESTROY),
            permission
    )
}

fun BaseRxFragment.isPermissionsGranted(vararg permissions: String): Boolean {
    return PermissionManager.isPermissionsGranted(this.context, *permissions)
}

fun BaseRxFragmentActivity.isPermissionsGranted(vararg permissions: String): Boolean {
    return PermissionManager.isPermissionsGranted(this, *permissions)
}