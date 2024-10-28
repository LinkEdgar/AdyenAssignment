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

    @Test
    fun `Given a date with a day of ordinal value less than 10 we expect date string to be formatted as mm-dd-yyy`() {
        val stringDate = "2021-01-07"
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date != null)
        val expectedDate = "01/07/2021"
        val result = DateUtil.formatLocalDateToString(date!!)
        assert(expectedDate == result)
    }

    @Test
    fun `Given a date with a day of ordinal value greater than 10 we expect date string be formatted as mm-dd-yyy`() {
        val stringDate = "2021-01-24"
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date != null)
        val expectedDate = "01/24/2021"
        val result = DateUtil.formatLocalDateToString(date!!)
        assert(expectedDate == result)
    }

    @Test
    fun `Given a date with a month of ordinal value less than 10 we expect date string to be formatted as mm-dd-yyy`() {
        val stringDate = "2022-06-03"
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date != null)
        val expectedDate = "06/03/2022"
        val result = DateUtil.formatLocalDateToString(date!!)
        assert(expectedDate == result)
    }

    @Test
    fun `Given a date with a month of ordinal value greater than 10 we expect date string be formatted as mm-dd-yyy`() {
        val stringDate = "2020-11-24"
        val date = DateUtil.convertStringToDate(stringDate)
        assert(date != null)
        val expectedDate = "11/24/2020"
        val result = DateUtil.formatLocalDateToString(date!!)
        assert(expectedDate == result)
    }
}