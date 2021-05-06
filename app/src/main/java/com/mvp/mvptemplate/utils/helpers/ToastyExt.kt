package com.mvp.mvptemplate.utils.helpers

import android.content.Context
import android.widget.Toast
import com.mvp.mvptemplate.App
import es.dmoral.toasty.Toasty


@JvmOverloads
fun showErrorToast(stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    showErrorToast(getString(stringResId), duration)
}

@JvmOverloads
fun showErrorToast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    val context: Context = App.koinApplication.koin.get()
    Toasty.error(context, message ?: "", duration).show()
}

@JvmOverloads
fun showInfoToast(stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    showInfoToast(getString(stringResId), duration)
}

@JvmOverloads
fun showInfoToast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.info(App.koinApplication.koin.get(), message ?: "", duration).show()
}


@JvmOverloads
fun showWarningToast(stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    showWarningToast(getString(stringResId), duration)
}

@JvmOverloads
fun showWarningToast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.warning(App.koinApplication.koin.get(), message ?: "", duration).show()
}


@JvmOverloads
fun showSuccessToast(stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    showSuccessToast(getString(stringResId), duration)
}

@JvmOverloads
fun showSuccessToast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.success(App.koinApplication.koin.get(), message ?: "", duration).show()
}
