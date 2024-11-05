package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.flow.Flow

open class PODsRepository(
    private val planetaryService: PlanetaryService,
    private val podStorage: PODStorageImp
) {


    /**
     * Removes pod from storage
     * @param pod --> item to be added from storage
     */
    open suspend fun addPod(pod: PODImageModel) {
        podStorage.addPodToFavs(pod)
    }

    /**
     * Removes pod from storage
     * @param pod --> item to be removed from storage
     */
    open suspend fun removePodFromFavorites(pod: PODImageModel) {
        podStorage.removeFromFavs(pod)
    }

    /**
     * One consideration to make is adding a cache time to refresh
     * this API once a day if we wish to conserve user bandwidth
     * This can be accomplished with Shared preferences or local DB
     */

    /**
     * Get planets and filters out by media type for images
     * Returns a [Resource] wrapped list of [PODImageModel] if successful
     * otherwise an error message
     */
    open suspend fun getImagePlanets(): Resource<List<PODImageModel>> {
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

    fun getPodsStream() : Flow<List<PODImageModel>> {
        return podStorage.getCachedPodsStream()
    }

    fun getFavoritePodsStream() : Flow<List<PODImageModel>> {
        return podStorage.getFavoritePodsStream()
    }

    fun getPodByID(id: String) : PODImageModel? {
        return podStorage.getPodByID(id)
    }
}