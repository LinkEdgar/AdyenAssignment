package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onSubscription

interface PODStorage {
    suspend fun addPodToFavorites(pod: PODImageModel)
    suspend fun removePodFromFavorites(pod: PODImageModel)
    suspend fun cachePods(pods: List<PODImageModel>)
    suspend fun deletePods()
    fun getCachedPodsStream(): Flow<List<PODImageModel>>
    fun getFavoritePodsStream(): Flow<List<PODImageModel>>
    fun getPodByID(id: String): PODImageModel?
}

open class PODStorageImp : PODStorage {

    private val _favoritePODS = MutableSharedFlow<List<PODImageModel>>()

    private val _podMap = HashMap<String, PODImageModel>()
    private val _favs = mutableListOf<PODImageModel>()

    private val _cachedPods = MutableSharedFlow<List<PODImageModel>>()


    open val favs = _favoritePODS.asSharedFlow().onSubscription { emit(emptyList()) }

    open val pods = _cachedPods.asSharedFlow().onSubscription { emit(emptyList()) }

    override suspend fun addPodToFavorites(pod: PODImageModel) {
        _favs.add(pod)
        _podMap[pod.id] = pod.copy(isFavorite = true)
        _favoritePODS.emit(_favs)
    }

    override suspend fun removePodFromFavorites(pod: PODImageModel) {
        _favs.remove(pod)
        _podMap[pod.id] = pod.copy(isFavorite = false)
        _favoritePODS.emit(_favs)
    }

    override suspend fun cachePods(pods: List<PODImageModel>) {
        pods.forEach {
            _podMap[it.id] = it
        }
        _cachedPods.emit(_podMap.values.toList())
    }

    override suspend fun deletePods() {
        _podMap.clear()
        _cachedPods.emit(_podMap.values.toList())
    }

    override fun getCachedPodsStream(): Flow<List<PODImageModel>> {
        return pods
    }

    override fun getFavoritePodsStream(): Flow<List<PODImageModel>> {
        return favs
    }

    override fun getPodByID(id: String): PODImageModel? {
        return _podMap[id]
    }


}