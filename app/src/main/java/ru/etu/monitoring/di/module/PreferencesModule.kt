package ru.etu.monitoring.di.module

import ru.etu.monitoring.model.preference.SettingsPreferences
import ru.etu.monitoring.model.preference.UserPreferences
import org.koin.dsl.module

val prefsModule = module {
    factory<UserPreferences> {
        UserPreferences(get())
    }
    factory<SettingsPreferences> {
        SettingsPreferences(get())
    }
}