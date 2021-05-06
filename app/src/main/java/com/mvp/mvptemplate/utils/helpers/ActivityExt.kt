package com.mvp.mvptemplate.utils.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * Setup actionbar
 */
fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))

    supportActionBar?.run {
        this.action()
    }
}

/**
 * Extension method to get ContentView for ViewGroup.
 */
fun Activity.getContentView(): ViewGroup {
    return this.findViewById(android.R.id.content) as ViewGroup
}

fun Activity.isFullScreen(): Boolean {
    val flg = window.attributes.flags
    var flag = false
    if (flg and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
        flag = true
    }
    return flag
}


fun Activity.setFullScreen(state: Boolean) {
    if (state) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity.listenForKeyboard(listener: (Boolean) -> Unit) {
    window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        window.decorView.getWindowVisibleDisplayFrame(r)
        val screenHeight = window.decorView.rootView.height;
        val keypadHeight = screenHeight - r.bottom

        if (keypadHeight > screenHeight * 0.15) {
            listener(true)
        } else {
            listener(false)
        }
    }
}

fun Activity.hideNavBar() {
    if (Build.VERSION.SDK_INT >= 19) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}

fun Activity.showNavBar() {
    if (Build.VERSION.SDK_INT >= 19) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE)
    }
}

fun Activity.makeStatusBarTranslucent() {
    if (Build.VERSION.SDK_INT >= 21) {
        window?.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

fun Activity.makeStatusBarIconsLight() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view: View = window?.decorView ?: return
        var flags = view.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        view.systemUiVisibility = flags
    }
}