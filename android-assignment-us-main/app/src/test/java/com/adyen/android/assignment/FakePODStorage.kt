package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import kotlinx.coroutines.flow.Flow

class FakePODStorage : PODStorage  {

    private val pods: MutableList<PODImageModel> = mutableListOf()

    private val favPods: MutableList<PODImageModel> = mutableListOf()

    override suspend fun addPodToFavorites(pod: PODImageModel) {
        TODO("Not yet implemented")
    }

    override suspend fun removePodFromFavorites(pod: PODImageModel) {
        TODO("Not yet implemented")
    }

    override suspend fun cachePods(pods: List<PODImageModel>) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePods() {
        TODO("Not yet implemented")
    }

    override fun getCachedPodsStream(): Flow<List<PODImageModel>> {
        TODO("Not yet implemented")
    }

    override fun getFavoritePodsStream(): Flow<List<PODImageModel>> {
        TODO("Not yet implemented")
    }

    override fun getPodByID(id: String): PODImageModel? {
        TODO("Not yet implemented")
    }
}