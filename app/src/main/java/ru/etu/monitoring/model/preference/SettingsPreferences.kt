package ru.etu.monitoring.model.preference

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.koin.core.component.KoinComponent

class SettingsPreferences constructor(context: Context) : KoinComponent {
    private val gson: Gson
    private var mSettings: SharedPreferences

    init {
        this.mSettings = context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        this.gson = Gson()
    }

    private fun getStringProperty(property: String): String? {
        return mSettings.getString(property, null)
    }

    private fun getBoolProperty(property: String): Boolean {
        return mSettings.getBoolean(property, false)
    }

    private fun updateProperty(property: String, value: Boolean) {
        mSettings.edit().run {
            putBoolean(property, value)
            apply()
        }
    }

    private fun updateProperty(property: String, value: String?) {
        mSettings.edit().run {
            putString(property, value)
            apply()
        }
    }

    companion object {
        private const val SETTINGS_PREFERENCES = "settings_pref"
    }
}
