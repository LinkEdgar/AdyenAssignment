package com.adyen.android.assignment.di

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun provideHttpClient() : OkHttpClient {
    return OkHttpClient()
        .newBuilder().readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
}
