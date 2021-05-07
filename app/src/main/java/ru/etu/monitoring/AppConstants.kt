package ru.etu.monitoring

object AppConstants {
    const val API_API = "https://ehm.devigro.ru/api/"
    val HOST = API_API.substring(0, API_API.indexOf("/", API_API.indexOf("://") + 3))
}
