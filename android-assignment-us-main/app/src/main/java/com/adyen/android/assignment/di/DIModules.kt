package com.adyen.android.assignment.di

import com.adyen.android.assignment.PODStorage
import com.adyen.android.assignment.PODsRepository
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.viewmodels.PODViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module


val planetaryModules = module {
    single { PODsRepository(get(),get()) }
    factory { PODViewModel(get(), get()) }
}

val networkModule = module {
    single { PlanetaryService.instance }
}

val dispatcherModule = module {
    single {
        Dispatchers.IO
    }
}

val storageModule = module {
    single {
        PODStorage()
    }
}