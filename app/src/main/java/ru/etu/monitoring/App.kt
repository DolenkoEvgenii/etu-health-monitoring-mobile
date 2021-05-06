package ru.etu.monitoring

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import es.dmoral.toasty.Toasty
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import ru.etu.monitoring.di.module.*

open class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        buildComponent()
        initLogger()
        initToasty()

        MultiDex.install(this)
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    }

    private fun initToasty() {
        Toasty.Config.getInstance()
                .setTextSize(15)
                .apply()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .methodOffset(7)
                .tag("Main_Log")
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    private fun buildComponent() {
        koinApplication = startKoin {
            androidContext(this@App)
            modules(appModule, apiModule, ciceroneModule, retrofitModule, prefsModule)
        }
    }

    companion object {
        lateinit var koinApplication: KoinApplication
    }
}
