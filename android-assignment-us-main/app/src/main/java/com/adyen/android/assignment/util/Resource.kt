package com.adyen.android.assignment.util

sealed class Resource<out T> {
    object Uninitiated: Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: String) : Resource<Nothing>()
}