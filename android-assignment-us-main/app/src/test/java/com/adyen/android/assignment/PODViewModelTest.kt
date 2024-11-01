package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import com.adyen.android.assignment.viewmodels.FilterType
import com.adyen.android.assignment.viewmodels.PODViewModel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

class PODViewModelTest {

    @Mock
    private val podRepo: PODsRepository = mock()

    private lateinit var testSubject: PODViewModel

    @Before
    fun setup() {
        testSubject = PODViewModel(podRepo, UnconfinedTestDispatcher())
    }

    @Test
    fun `Given successful getImagePlanets is called successfully we expect uiState to be successful`() = runTest {
        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Success(emptyList()))
        testSubject.loadPlanets()
        val uiState = testSubject.uiState
        assert(uiState.value is Resource.Success)
    }

    @Test
    fun `Given error getImagePlanets is called successfully we expect uiState to be error`() = runTest {
        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Error(error = "error"))
        testSubject.loadPlanets()
        val uiState = testSubject.uiState
        assert(uiState.value is Resource.Error)
    }

    @Test
    fun `Given setSort type set to title we expect pods to be sorted`() = runTest {
        val pods = listOf(
            PODImageModel("M", "", LocalDate.now(), "","",""),
            PODImageModel("Z", "", LocalDate.now(), "","",""),
            PODImageModel("D", "", LocalDate.now(), "","",""),
            PODImageModel("Y", "", LocalDate.now(), "","","")
            )

        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Success(pods))
        testSubject.loadPlanets()
        testSubject.setFilterType(FilterType.TITLE)
        val uiState = testSubject.uiState
        assert(uiState.value is Resource.Success)
        val sortedPods = (uiState.value as Resource.Success).data
        assert(sortedPods[0].title == "D")
        assert(sortedPods[1].title == "M")
        assert(sortedPods[2].title == "Y")
        assert(sortedPods[3].title == "Z")
    }

    //this test could be improved by checking the dates
    @Test
    fun `Given setSort type set to date we expect pods to be sorted by date decending`() = runTest {
        val pods = listOf(
            PODImageModel("third", "", LocalDate.parse("2021-01-07"), "","",""),
            PODImageModel("second", "", LocalDate.parse("2028-01-07"), "","",""),
            PODImageModel("first", "", LocalDate.parse("2084-01-07"), "","",""),
            PODImageModel("fourth", "", LocalDate.parse("1984-01-07"), "","","")
        )

        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Success(pods))
        testSubject.loadPlanets()
        testSubject.setFilterType(FilterType.DATE)
        val uiState = testSubject.uiState
        assert(uiState.value is Resource.Success)
        val sortedPods = (uiState.value as Resource.Success).data
        assert(sortedPods[0].title == "first")
        assert(sortedPods[1].title == "second")
        assert(sortedPods[2].title == "third")
        assert(sortedPods[3].title == "fourth")
    }
}