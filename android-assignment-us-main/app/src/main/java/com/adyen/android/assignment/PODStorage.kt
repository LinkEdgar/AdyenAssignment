package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onSubscription

open class PODStorage {

    private val _favs = mutableListOf<PODImageModel>()

    private val _cachedPODS = MutableSharedFlow<List<PODImageModel>>()

    open val favs = _cachedPODS.asSharedFlow().onSubscription { emit(emptyList()) }

    open suspend fun addPodToFavs(pod: PODImageModel) {
        if (!_favs.contains(pod)) {
            _favs.add(pod)
        }
        _cachedPODS.emit(_favs)
    }

    open suspend fun removeFromFavs(pod: PODImageModel) {
        if (_favs.contains(pod)) {
            _favs.remove(pod)
        }
        _cachedPODS.emit(_favs)
    }


}