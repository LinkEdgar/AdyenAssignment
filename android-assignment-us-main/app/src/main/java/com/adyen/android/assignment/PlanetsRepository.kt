package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.ui.planets.PlanetImageModel
import com.adyen.android.assignment.util.Resource

class PlanetsRepository(
    private val planetaryService: PlanetaryService
) {
    /**
     * Get planets and filters out by media type for images
     * Returns a [Resource] wrapped list of [PlanetImageModel] if successful
     * otherwise an error message
     */
    suspend fun getImagePlanets(): Resource<List<PlanetImageModel>> {
        try {
            val response = planetaryService.getPictures()
            if (response.isSuccessful) {
                response.errorBody()
                val planets =  response.body()
                val mappedPlanets = PlanetMapper.mapAstronomyPicturesToPlanetModels(planets)
                println("mapped planets --> $mappedPlanets")
                return Resource.Success(mappedPlanets)
            } else {
                println("getImagePlanets Error loading planets")
                return Resource.Error("getImagePlanets Error loading planets")
            }
        } catch (e : Exception) {
            println("getImagePlanets exception --> ${e.message}")
            return Resource.Error("getImagePlanets Error loading planets ${e.message}")
        }
    }
}