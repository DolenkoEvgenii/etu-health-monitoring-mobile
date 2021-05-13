package ru.etu.monitoring.di.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.etu.monitoring.AppConstants
import ru.etu.monitoring.utils.gson.ExcludeCompanionConverter
import ru.etu.monitoring.utils.gson.SerializableAsNullConverter
import java.util.*
import java.util.concurrent.TimeUnit

val retrofitModule = module {
    single<Retrofit> {
        val builder: Retrofit.Builder = get()
        builder.baseUrl(AppConstants.API_API).build()
    }

    single<Retrofit.Builder> {
        Retrofit.Builder()
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(get())
    }

    single<Converter.Factory> {
        GsonConverterFactory.create(get())
    }

    single<Gson> {
        GsonBuilder()
            .registerTypeAdapterFactory(ExcludeCompanionConverter())
            .registerTypeAdapterFactory(SerializableAsNullConverter())
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .registerTypeAdapter(Date::class.java, NetworkClasses.MyDateTypeAdapter())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .serializeNulls()
            .create()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .cache(null)
            .retryOnConnectionFailure(true)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(NetworkClasses.AuthInterceptor(get()))
            .addInterceptor(NetworkClasses.UserAgentInterceptor(get()))
            .addInterceptor(ChuckInterceptor(get()))
            .build()
    }
}

