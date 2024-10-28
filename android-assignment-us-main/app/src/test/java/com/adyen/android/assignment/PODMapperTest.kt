package com.adyen.android.assignment

import com.adyen.android.assignment.api.model.AstronomyPicture
import org.junit.Test

class PODMapperTest {

    @Test
    fun `Given a null date we expect mapAstronomyPictureToPlanetsModel to return null`() {
        val astronomyPicture = AstronomyPicture(
            serviceVersion = "", title = "", explanation = "",
            date = null, hdUrl = "", url = "", mediaType = "Image"
            )

        val mappedModel = PODMapper.mapImageAstronomyPictureToPlanetsModel(astronomyPicture)
        assert(mappedModel == null)
    }

    @Test
    fun `Given a null title we expect mapAstronomyPictureToPlanetsModel to return null`() {
        val astronomyPicture = AstronomyPicture(
            serviceVersion = "", title = null, explanation = "",
            date = "2014-07-12", hdUrl = "", url = "", mediaType = "Image"
        )

        val mappedModel = PODMapper.mapImageAstronomyPictureToPlanetsModel(astronomyPicture)
        assert(mappedModel == null)
    }

    @Test
    fun `Given a non image media source we expect mapAstronomyPictureToPlanetsModel to return null`() {
        val astronomyPicture = AstronomyPicture(
            serviceVersion = "", title = "title", explanation = "",
            date = "2014-07-12", hdUrl = "", url = "", mediaType = "Video"
        )

        val mappedModel = PODMapper.mapImageAstronomyPictureToPlanetsModel(astronomyPicture)
        assert(mappedModel == null)
    }

    @Test
    fun `Given an image media source we expect mapAstronomyPictureToPlanetsModel to not return null`() {
        val astronomyPicture = AstronomyPicture(
            serviceVersion = "", title = "title", explanation = "",
            date = "2014-07-12", hdUrl = "", url = "", mediaType = "image"
        )

        val mappedModel = PODMapper.mapImageAstronomyPictureToPlanetsModel(astronomyPicture)
        assert(mappedModel != null)
    }
}