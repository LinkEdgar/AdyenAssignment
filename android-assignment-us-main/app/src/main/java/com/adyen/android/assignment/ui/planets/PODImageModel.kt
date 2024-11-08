package com.adyen.android.assignment.ui.planets

import com.adyen.android.assignment.util.DateUtil.formatLocalDateToString
import java.time.LocalDate

/**
 * Model for planets of the image type.
 * If needed in the future we can make a video planet model as well. This assumes their
 * will be differences between the two types. Video planet model may have information
 * related to ratio and thumbnail urls
 */
data class PODImageModel(
    val title: String,
    val explanation: String,
    val date: LocalDate,
    val imageUrlHQ: String?,
    val imageUrl: String,
    val id: String,
    val isFavorite: Boolean
) {
    fun getFormattedDate() : String {
        return formatLocalDateToString(date)
    }
}