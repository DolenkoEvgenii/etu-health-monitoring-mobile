package ru.etu.monitoring.di.module

import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

val ciceroneModule = module {
    single<Cicerone<Router>> { Cicerone.create() }

    single<Router> {
        val cicerone: Cicerone<Router> = get()
        cicerone.router
    }
    single<NavigatorHolder> {
        val cicerone: Cicerone<Router> = get()
        cicerone.navigatorHolder
    }
}

