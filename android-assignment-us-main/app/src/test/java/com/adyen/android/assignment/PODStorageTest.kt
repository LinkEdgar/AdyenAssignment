package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

class PODStorageTest {

    private val podStorage = PODStorage()

    @Test
    fun `Given a pod that is not in favs then addToFavs will add pod`() = runTest {
        val pod = PODImageModel("M", "", LocalDate.now(), "","","",isFavorite = false)
        val favPods = podStorage.favs
        //todo

    }
}