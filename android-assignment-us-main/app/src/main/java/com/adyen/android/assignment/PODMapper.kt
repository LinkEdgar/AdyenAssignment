package com.adyen.android.assignment

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.DateUtil

object PODMapper {

    /**
     * Maps [AstronomyPicture] to [PODImageModel]
     */
    fun mapImageAstronomyPictureToPlanetsModel(astronomyPicture: AstronomyPicture): PODImageModel? {
        if (astronomyPicture.mediaType != "image") return null // since we are only concerned about images at the moment
        return PODImageModel(
            title = astronomyPicture.title ?: return null, //since we want to order by title I decided not to models without it
            explanation = astronomyPicture.explanation ?: "",
            date = DateUtil.convertStringToDate(astronomyPicture.date ?: "") ?: return null, //since we want to order by date I decided not to models without it
            imageUrl = astronomyPicture.url ?: "",
            imageUrlHQ = astronomyPicture.hdUrl
        )
    }

    /**
     * Maps a list of [AstronomyPicture] to a list of [PODImageModel]
     * and returns an empty list if [astronomyPictures] is null
     */
    fun mapAstronomyPicturesToPlanetModels(astronomyPictures: List<AstronomyPicture>?) : List<PODImageModel> {
        return astronomyPictures?.mapNotNull {
            mapImageAstronomyPictureToPlanetsModel(it)
        } ?: emptyList()
    }
}