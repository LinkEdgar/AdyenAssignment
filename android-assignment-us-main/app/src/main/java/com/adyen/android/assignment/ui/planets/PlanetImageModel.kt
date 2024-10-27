package com.adyen.android.assignment.ui.planets

/**
 * Model for planets of the image type.
 * If needed in the future we can make a video planet model as well. This assumes their
 * will be differences between the two types. Video planet model may have information
 * related to ratio and thumbnail urls
 */
data class PlanetImageModel(
    val title: String,
    val explanation: String,
    val date: String,
    val imageUrlHQ: String?,
    val imageUrl: String
)