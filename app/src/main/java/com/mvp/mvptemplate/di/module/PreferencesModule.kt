package com.mvp.mvptemplate.di.module

import com.mvp.mvptemplate.model.preference.SettingsPreferences
import com.mvp.mvptemplate.model.preference.UserPreferences
import org.koin.dsl.module

val prefsModule = module {
    factory<UserPreferences> {
        UserPreferences(get())
    }
    factory<SettingsPreferences> {
        SettingsPreferences(get())
    }
}