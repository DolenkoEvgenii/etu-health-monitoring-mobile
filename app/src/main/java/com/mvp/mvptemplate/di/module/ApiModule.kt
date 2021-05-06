package com.mvp.mvptemplate.di.module

import com.mvp.mvptemplate.model.network.user.UserApi
import com.mvp.mvptemplate.model.network.user.UserRepository
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

