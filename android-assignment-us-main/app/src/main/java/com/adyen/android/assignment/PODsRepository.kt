package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.flow.Flow

interface PODRepo {
    suspend fun addPodToFavorites(pod: PODImageModel)
    suspend fun removePodFromFavorites(pod: PODImageModel)
    suspend fun getImagePods(): Resource<List<PODImageModel>>
    fun getPODStream(): Flow<List<PODImageModel>>
    fun getFavPodStream() : Flow<List<PODImageModel>>
    fun getPodByID(id: String) : PODImageModel?
}

open class PODsRepository(
    private val planetaryService: PlanetaryService,
    private val podStorage: PODStorage
) : PODRepo {


    /**
     * Removes pod from storage
     * @param pod --> item to be added from storage
     */
    override suspend fun addPodToFavorites(pod: PODImageModel) {
        podStorage.addPodToFavorites(pod)
    }

    override suspend fun removePodFromFavorites(pod: PODImageModel) {
        podStorage.removePodFromFavorites(pod)
    }

    override suspend fun getImagePods(): Resource<List<PODImageModel>> {
        try {
            val response = planetaryService.getPictures()
            if (response.isSuccessful) {
                val pods =  response.body()
                val mappedPods = PODMapper.mapAstronomyPicturesToPlanetModels(pods)
                podStorage.cachePods(mappedPods)
                println("mapped planets --> $mappedPods")
                return Resource.Success(mappedPods)
            } else {
                println("getImagePlanets Error loading planets")
                return Resource.Error("getImagePlanets Error loading planets")
            }
        } catch (e : Exception) {
            println("getImagePlanets exception --> ${e.message}")
            return Resource.Error("getImagePlanets Error loading planets ${e.message}")
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