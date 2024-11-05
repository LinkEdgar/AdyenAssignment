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
    fun getFavoritePodsStream() : Flow<List<PODImageModel>>
    fun getPodByID(id: String): PODImageModel?
}

open class PODStorageImp : PODStorage{

    private val _favs = mutableListOf<PODImageModel>()

    private val podCache = mutableListOf<PODImageModel>()

    private val _favoritePODS = MutableSharedFlow<List<PODImageModel>>()

    private val _pods = MutableSharedFlow<List<PODImageModel>>()


    open val favs = _favoritePODS.asSharedFlow().onSubscription { emit(emptyList()) }

    open val pods = _pods.asSharedFlow().onSubscription { emit(emptyList()) }



    open suspend fun addPodToFavs(pod: PODImageModel) {
        if (!_favs.contains(pod)) {
            _favs.add(pod)
        }
        _favoritePODS.emit(_favs)
    }

    open suspend fun removeFromFavs(pod: PODImageModel) {
        if (_favs.contains(pod)) {
            _favs.remove(pod)
        }
        _favoritePODS.emit(_favs)
    }

    override suspend fun addPodToFavorites(pod: PODImageModel) {
        if (!_favs.contains(pod)) {
            _favs.add(pod)
        }
        _favoritePODS.emit(_favs)
    }

    override suspend fun removePodFromFavorites(pod: PODImageModel) {
        if (_favs.contains(pod)) {
            _favs.remove(pod)
        }
        _favoritePODS.emit(_favs)
    }

    override suspend fun cachePods(pods: List<PODImageModel>) {
        podCache.addAll(pods)
        _pods.emit(podCache)
    }

    override suspend fun deletePods() {
        podCache.clear()
        _pods.emit(podCache)
    }

    override fun getCachedPodsStream(): Flow<List<PODImageModel>> {
        return pods
    }

    override fun getFavoritePodsStream(): Flow<List<PODImageModel>> {
        return favs
    }

    override fun getPodByID(id: String): PODImageModel? {
        return podCache.find { pod -> pod.id == id }
    }


}