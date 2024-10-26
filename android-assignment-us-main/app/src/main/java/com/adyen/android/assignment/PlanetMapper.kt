package com.adyen.android.assignment

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.ui.planets.PlanetImageModel

object PlanetMapper {

    /**
     * Maps [AstronomyPicture] to [PlanetImageModel]
     */
    fun mapAstronomyPictureToPlanetsModel(astronomyPicture: AstronomyPicture): PlanetImageModel {
        return PlanetImageModel(
            title = astronomyPicture.title,
            explanation = astronomyPicture.explanation,
            date = astronomyPicture.date,
            imageUrl = astronomyPicture.url,
            imageUrlHQ = astronomyPicture.hdUrl
        )
    }

    /**
     * Maps a list of [AstronomyPicture] to a list of [PlanetImageModel]
     * and returns an empty list if [astronomyPictures] is null
     */
    fun mapAstronomyPicturesToPlanetModels(astronomyPictures: List<AstronomyPicture>?) : List<PlanetImageModel> {
        return astronomyPictures?.map {
            mapAstronomyPictureToPlanetsModel(it)
        } ?: emptyList()
    }
}