package com.adyen.android.assignment.ui

import android.app.Application
import com.adyen.android.assignment.di.dispatcherModule
import com.adyen.android.assignment.di.networkModule
import com.adyen.android.assignment.di.planetaryModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(planetaryModules, networkModule, dispatcherModule)
        }
    }

}