package com.mvp.mvptemplate

object AppConstants {
    const val API_API = "https://stub.com"
    val HOST = API_API.substring(0, API_API.indexOf("/", API_API.indexOf("://") + 3))
}
