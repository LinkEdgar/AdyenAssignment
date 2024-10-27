package com.adyen.android.assignment

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.ui.planets.PlanetImageModel
import com.adyen.android.assignment.util.DateUtil

object PlanetMapper {

    /**
     * Maps [AstronomyPicture] to [PlanetImageModel]
     */
    fun mapImageAstronomyPictureToPlanetsModel(astronomyPicture: AstronomyPicture): PlanetImageModel? {
        if (astronomyPicture.mediaType != "image") return null // since we are only concerned about images at the moment
        return PlanetImageModel(
            title = astronomyPicture.title ?: return null, //since we want to order by title I decided not to models without it
            explanation = astronomyPicture.explanation ?: "",
            date = DateUtil.convertStringToDate(astronomyPicture.date ?: "") ?: return null, //since we want to order by date I decided not to models without it
            imageUrl = astronomyPicture.url ?: "",
            imageUrlHQ = astronomyPicture.hdUrl
        )
    }

    /**
     * Maps a list of [AstronomyPicture] to a list of [PlanetImageModel]
     * and returns an empty list if [astronomyPictures] is null
     */
    fun mapAstronomyPicturesToPlanetModels(astronomyPictures: List<AstronomyPicture>?) : List<PlanetImageModel> {
        return astronomyPictures?.mapNotNull {
            mapImageAstronomyPictureToPlanetsModel(it)
        } ?: emptyList()
    }
}