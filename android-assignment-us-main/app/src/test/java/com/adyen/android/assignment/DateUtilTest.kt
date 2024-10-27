package com.adyen.android.assignment

import com.adyen.android.assignment.util.DateUtil
import org.junit.Test

class DateUtilTest {

    @Test
    fun `Given a valid date of 2021-01-07 we expect a LocalDate property that matches yeer, day, and month`() {
        val stringDate = "2021-01-07"
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date != null)
        assert(date!!.year == 2021)
        assert(date.monthValue == 1)
        assert(date.dayOfMonth == 7)
    }

    @Test
    fun `Given an empty string we expect localdate to be null`() {
        val stringDate = ""
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date == null)
    }

    @Test
    fun `Given invalid date we expect date returned to be null`() {
        val stringDate = "test@test.com"
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date == null)
    }
}