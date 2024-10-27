package com.adyen.android.assignment.di

import com.adyen.android.assignment.PlanetsRepository
import com.adyen.android.assignment.api.PlanetaryService
import org.koin.dsl.module


val planetaryModules = module {
    single { PlanetsRepository(get()) }
}

val networkModule = module {
    single { PlanetaryService.instance }
}