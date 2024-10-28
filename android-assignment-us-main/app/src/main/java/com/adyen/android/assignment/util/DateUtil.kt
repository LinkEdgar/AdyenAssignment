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

    /**
     * Formats dates to as a string mm/dd/yyyy
     */
    fun formatLocalDateToString(date: LocalDate): String {
        val month = if (date.monthValue < 10) "0${date.monthValue}" else "${date.monthValue}"
        val day = if (date.dayOfMonth < 10) "0${date.dayOfMonth}" else "${date.dayOfMonth}"
        val year = date.year
        return "$month/$day/$year"
    }

}