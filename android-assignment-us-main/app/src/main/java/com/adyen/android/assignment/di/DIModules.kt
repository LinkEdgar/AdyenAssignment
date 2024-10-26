package com.adyen.android.assignment.di

import com.adyen.android.assignment.PlanetsRepository
import org.koin.dsl.module


val planetaryModules = module {
    single { PlanetsRepository(get()) }
}