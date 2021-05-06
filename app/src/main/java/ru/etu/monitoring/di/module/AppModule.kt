package ru.etu.monitoring.di.module

import android.content.res.Resources
import android.util.DisplayMetrics
import ru.etu.monitoring.utils.helpers.displayMetricks
import org.koin.dsl.module

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

