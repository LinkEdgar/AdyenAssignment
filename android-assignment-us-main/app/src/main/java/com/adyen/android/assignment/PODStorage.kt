package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PODStorage {

    private val _favs = mutableListOf<PODImageModel>()

    private val _cachedPODS = MutableSharedFlow<List<PODImageModel>>()

    val favs = _cachedPODS.asSharedFlow()

    fun addPodToFavs(pod: PODImageModel) {
        if (!_favs.contains(pod)) {
            _favs.add(pod)
        }
    }

    fun removeFromFavs(pod: PODImageModel) {
        if (_favs.contains(pod)) {
            _favs.remove(pod)
        }
    }


}