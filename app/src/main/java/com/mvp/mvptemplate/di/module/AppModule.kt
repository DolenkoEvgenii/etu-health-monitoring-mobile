package com.mvp.mvptemplate.di.module

import android.content.res.Resources
import android.util.DisplayMetrics
import com.mvp.mvptemplate.utils.helpers.displayMetricks
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

