package com.adyen.android.assignment.api.model

import com.squareup.moshi.Json

//made all properties nullable to account for any backend issues
data class AstronomyPicture(
    @Json(name = "service_version") val serviceVersion: String?,
    val title: String?,
    val explanation: String?,
    val date: String?,
    @Json(name = "media_type") val mediaType: String?,
    @Json(name = "hdurl") val hdUrl: String?,
    val url: String?,
)