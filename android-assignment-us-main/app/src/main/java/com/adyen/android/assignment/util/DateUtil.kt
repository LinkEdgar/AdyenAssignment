package com.adyen.android.assignment.util

import java.time.LocalDate

object DateUtil {

    /**
     * Returns a LocalDate object from a date string
     */
    fun convertStringToDate(stringDate: String) : LocalDate? {
        try {
            val date = LocalDate.parse(stringDate)
            return date
        } catch (e : Exception) {
            //Logging this error would be something to help see if this conversion is failing
            return null
        }
    }

}