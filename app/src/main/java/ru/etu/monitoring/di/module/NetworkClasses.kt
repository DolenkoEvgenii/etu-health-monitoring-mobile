package ru.etu.monitoring.di.module

import android.content.Context
import android.os.Build
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.Interceptor
import okhttp3.Response
import ru.etu.monitoring.BuildConfig
import ru.etu.monitoring.model.preference.UserPreferences
import java.util.*

object NetworkClasses {
    class UserAgentInterceptor constructor(context: Context) : Interceptor {
        private val userAgent: String = createUserAgent(context)

        private fun createUserAgent(context: Context): String {
            val versionCode = BuildConfig.VERSION_CODE.toString()
            val androidOCVersion = Build.VERSION.RELEASE
            val model = Build.MODEL

            val displayMetrics = context.resources.displayMetrics
            val screenResolution = String.format(Locale.getDefault(), "%dx%d",
                    displayMetrics.heightPixels, displayMetrics.widthPixels)

            return String.format("Android_%s_v_%s_%s_%s", androidOCVersion, versionCode, model, screenResolution)
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestWithUserAgent = originalRequest.newBuilder()
                    .header("User-Agent", userAgent)
                    .build()
            return chain.proceed(requestWithUserAgent)
        }
    }

    class AuthInterceptor constructor(private val userPreferences: UserPreferences) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val token: String = userPreferences.authToken

            val request = original.newBuilder()
                    .header("Authorization", token)
                    .method(original.method, original.body)
                    .build()

            return chain.proceed(request)
        }
    }

    class MyDateTypeAdapter : TypeAdapter<Date>() {
        override fun write(out: JsonWriter, value: Date?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(value.time / 1000)
            }
        }

        override fun read(reader: JsonReader?): Date? {
            return if (reader != null) {
                Date(reader.nextLong() * 1000)
            } else {
                null
            }
        }
    }
}