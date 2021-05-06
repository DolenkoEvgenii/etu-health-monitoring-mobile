package ru.etu.monitoring.di.module

import android.content.res.Resources
import android.util.DisplayMetrics
import org.koin.dsl.module
import ru.etu.monitoring.utils.helpers.displayMetricks

val appModule = module {
    factory<Resources> {
        val context: android.content.Context = get()
        context.resources
    }
    factory<DisplayMetrics> {
        val context: android.content.Context = get()
        context.displayMetricks
    }
}

