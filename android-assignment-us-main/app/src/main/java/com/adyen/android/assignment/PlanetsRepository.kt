package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.ui.planets.PlanetImageModel

class PlanetsRepository(
    private val planetaryService: PlanetaryService
) {
    /**
     * Get planets and filters out by media type for images
     */
    suspend fun getImagePlanets(): List<PlanetImageModel> {
        //todo wrap resource to identify success vs error case
        try {
            val response = planetaryService.getPictures()
            if (response.isSuccessful) {
                //todo find out exact name of video media type
                val planets =  response.body()?.filter { it.mediaType  != "Video"}
                val mappedPlanets = PlanetMapper.mapAstronomyPicturesToPlanetModels(planets)
                return mappedPlanets
            } else {
                return emptyList()
            }
        } catch (e : Exception) {
            return emptyList()
        }
    }
}