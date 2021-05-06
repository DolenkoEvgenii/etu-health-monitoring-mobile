package ru.etu.monitoring.utils.helpers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> typeToken(): TypeToken<T> {
    return object : TypeToken<T>() {}
}