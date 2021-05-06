package ru.etu.monitoring.di.module

import org.koin.dsl.module
import ru.etu.monitoring.model.preference.SettingsPreferences
import ru.etu.monitoring.model.preference.UserPreferences

val prefsModule = module {
    factory<UserPreferences> {
        UserPreferences(get())
    }
    factory<SettingsPreferences> {
        SettingsPreferences(get())
    }
}