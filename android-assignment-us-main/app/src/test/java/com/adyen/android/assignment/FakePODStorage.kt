package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakePODStorage : PODStorage  {

    private val pods =  MutableStateFlow<List<PODImageModel>>(emptyList())

    private val favPods: MutableStateFlow<List<PODImageModel>> = MutableStateFlow(emptyList())

    override suspend fun addPodToFavorites(pod: PODImageModel) {
        val pods = favPods.value.toMutableList()
        pods.add(pod)
        favPods.value = pods
    }

    override suspend fun removePodFromFavorites(pod: PODImageModel) {
        val pods = favPods.value.toMutableList()
        pods.remove(pod)
        favPods.value = pods
    }

    override suspend fun cachePods(pods: List<PODImageModel>) {
        this.pods.value = pods
    }

    override suspend fun deletePods() {
        pods.value = emptyList()
    }

    override fun getCachedPodsStream(): Flow<List<PODImageModel>> {
        return pods.asSharedFlow()
    }

    override fun getFavoritePodsStream(): Flow<List<PODImageModel>> {
        return favPods.asSharedFlow()
    }

    override fun getPodByID(id: String): PODImageModel? {
        return pods.value.find { it.id == id }
    }
}