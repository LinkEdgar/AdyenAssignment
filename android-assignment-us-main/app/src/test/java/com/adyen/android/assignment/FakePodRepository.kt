package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePodRepository(
    private val podStorage: PODStorage
) : PODRepo{

    private var shouldNetworkSucceed = true

    private val _pods = MutableStateFlow<List<PODImageModel>>(emptyList())

    private var podsToReturn : List<PODImageModel> = emptyList()

    fun setPodsToReturn(pods: List<PODImageModel>) {
        podsToReturn = pods
    }

    fun setShouldNetworkSucceed(shouldSucceed: Boolean) {
        shouldNetworkSucceed = shouldSucceed
    }

    override suspend fun addPodToFavorites(pod: PODImageModel) {
        podStorage.addPodToFavorites(pod)
    }

    override suspend fun removePodFromFavorites(pod: PODImageModel) {
        podStorage.removePodFromFavorites(pod)
    }

    override suspend fun getImagePods(): Resource<List<PODImageModel>> {
        return if (shouldNetworkSucceed) {
            podStorage.cachePods(podsToReturn)
            Resource.Success(podsToReturn)
        } else {
            Resource.Error("Error")
        }
    }

    override fun getPODStream(): Flow<List<PODImageModel>> {
        return podStorage.getCachedPodsStream()
    }

    override fun getFavPodStream(): Flow<List<PODImageModel>> {
        return podStorage.getFavoritePodsStream()
    }

    override fun getPodByID(id: String): PODImageModel? {
        return podStorage.getPodByID(id)
    }
}