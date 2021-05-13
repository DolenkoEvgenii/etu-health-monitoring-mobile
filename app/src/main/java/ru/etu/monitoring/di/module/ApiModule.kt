package ru.etu.monitoring.di.module

import org.koin.dsl.module
import retrofit2.Retrofit
import ru.etu.monitoring.model.network.doctor.DoctorApi
import ru.etu.monitoring.model.network.doctor.DoctorRepository
import ru.etu.monitoring.model.network.patient.PatientApi
import ru.etu.monitoring.model.network.patient.PatientRepository
import ru.etu.monitoring.model.network.user.UserApi
import ru.etu.monitoring.model.network.user.UserRepository

val apiModule = module {
    factory<UserApi> {
        val retrofit: Retrofit = get()
        retrofit.create(UserApi::class.java)
    }

    factory<PatientApi> {
        val retrofit: Retrofit = get()
        retrofit.create(PatientApi::class.java)
    }

    factory<DoctorApi> {
        val retrofit: Retrofit = get()
        retrofit.create(DoctorApi::class.java)
    }

    factory {
        UserRepository(get())
    }

    factory {
        PatientRepository(get())
    }

    factory {
        DoctorRepository(get())
    }
}

