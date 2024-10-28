package com.adyen.android.assignment.di

import com.adyen.android.assignment.PODsRepository
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.viewmodels.PODViewModel
import org.koin.dsl.module


val planetaryModules = module {
    single { PODsRepository(get()) }
    factory { PODViewModel(get()) }
}

val networkModule = module {
    single { PlanetaryService.instance }
}