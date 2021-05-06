package ru.etu.monitoring.di.module

import ru.etu.monitoring.model.network.user.UserApi
import ru.etu.monitoring.model.network.user.UserRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    factory<UserApi> {
        val retrofit: Retrofit = get()
        retrofit.create(UserApi::class.java)
    }
    factory<UserRepository> {
        UserRepository(get())
    }
}

