package ru.etu.monitoring.di.module

import org.koin.dsl.module
import retrofit2.Retrofit
import ru.etu.monitoring.model.network.user.UserApi
import ru.etu.monitoring.model.network.user.UserRepository

val apiModule = module {
    factory<UserApi> {
        val retrofit: Retrofit = get()
        retrofit.create(UserApi::class.java)
    }
    factory<UserRepository> {
        UserRepository(get())
    }
}

