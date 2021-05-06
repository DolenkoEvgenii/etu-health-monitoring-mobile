package ru.etu.monitoring.ui.activity.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.appbar.AppBarLayout
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import ru.etu.monitoring.R
import ru.etu.monitoring.model.navigator.MainNavigator
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.provider.LoadingDialogProvider
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import java.util.concurrent.TimeUnit

abstract class BaseMvpFragmentActivity : BaseRxFragmentActivity() {
    private val navigatorHolder: NavigatorHolder by inject()

    private lateinit var navigator: Navigator
    private var sweetAlertDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = MainNavigator(this)
    }

    @SuppressLint("CheckResult")
    fun executeAfterDelay(timeMillis: Long, block: () -> Unit) {
        Observable.timer(timeMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({ block() }, {})
    }

    fun showLoadingDialog() {
        if (sweetAlertDialog == null) {
            initDialog()
        }

        if (sweetAlertDialog?.isShowing != true) {
            sweetAlertDialog?.show()
        }
    }

    fun closeLoadingDialog() {
        sweetAlertDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismissWithAnimation()
                Handler().postDelayed({
                    if (dialog.isShowing) {
                        dialog.dismiss()
                        sweetAlertDialog = null
                    }
                }, 100)
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onBackPressed() {
        val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.vFragmentContainer)
        if (currentFragment is BaseMvpFragment) {
            if (currentFragment.onBackPressed()) {
                return
            }
        }

        super.onBackPressed()
    }

    open fun provideAppBar(): AppBarLayout? {
        return findViewById(R.id.appBar)
    }

    private fun initDialog() {
        sweetAlertDialog = LoadingDialogProvider.getProgressDialog(this)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}
