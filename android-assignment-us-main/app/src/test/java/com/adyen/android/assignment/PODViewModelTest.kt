package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.viewmodels.FilterType
import com.adyen.android.assignment.viewmodels.PODViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class PODViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val storage =  FakePODStorage()
    private val podRepo: FakePodRepository = FakePodRepository(storage)

    private lateinit var testSubject: PODViewModel

    @Before
    fun setup() {
        testSubject = PODViewModel(podRepo, UnconfinedTestDispatcher())
    }


    @Test
    fun `Given successful getImagePods is called successfully we expect uiState to not be loading and pods to be populated`() = runTest {
        val successFulPods = listOf(PODImageModel("","",LocalDate.now(),"","","",false))
        podRepo.setShouldNetworkSucceed(true)
        podRepo.setPodsToReturn(successFulPods)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testSubject.uiState.collect()
        }

        val uiState = testSubject.uiState

        assert(uiState.value.isLoading)
        testSubject.loadPlanets()
        assert(!uiState.value.isLoading)
        assert(uiState.value.pods == successFulPods)

        job.cancel()

    }

    @Test
    fun `Given error getImagePods is called successfully we expect uiState to be error`() = runTest {
        podRepo.setShouldNetworkSucceed(false)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testSubject.uiState.collect()
        }

        testSubject.loadPlanets()
        val uiState = testSubject.uiState
        assert(!uiState.value.isLoading)
        assert(uiState.value.errorMessage != null)
        job.cancel()
    }

    @Test
    fun `Given setSort type set to title we expect pods to be sorted`() = runTest {
        val pods = listOf(
            PODImageModel("M", "", LocalDate.now(), "","","", isFavorite = false),
            PODImageModel("Z", "", LocalDate.now(), "","","", isFavorite = false),
            PODImageModel("D", "", LocalDate.now(), "","","", isFavorite = false),
            PODImageModel("Y", "", LocalDate.now(), "","","", isFavorite = false)
            )

        podRepo.setShouldNetworkSucceed(true)
        podRepo.setPodsToReturn(pods)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testSubject.uiState.collect()
        }
        testSubject.loadPlanets()
        testSubject.setFilterType(FilterType.TITLE)
        val uiState = testSubject.uiState
        val sortedPods = uiState.value.pods
        assert(sortedPods[0].title == "D")
        assert(sortedPods[1].title == "M")
        assert(sortedPods[2].title == "Y")
        assert(sortedPods[3].title == "Z")

        job.cancel()
    }

    //this test could be improved by checking the dates
    @Test
    fun `Given setSort type set to date we expect pods to be sorted by date decending`() = runTest {
        val pods = listOf(
            PODImageModel("third", "", LocalDate.parse("2021-01-07"), "","","", isFavorite = false),
            PODImageModel("second", "", LocalDate.parse("2028-01-07"), "","","", isFavorite = false),
            PODImageModel("first", "", LocalDate.parse("2084-01-07"), "","","", isFavorite = false),
            PODImageModel("fourth", "", LocalDate.parse("1984-01-07"), "","","", isFavorite = false)
        )

        podRepo.setShouldNetworkSucceed(true)
        podRepo.setPodsToReturn(pods)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testSubject.uiState.collect()
        }
        testSubject.loadPlanets()
        testSubject.setFilterType(FilterType.DATE)
        val uiState = testSubject.uiState
        val sortedPods = uiState.value.pods
        assert(sortedPods[0].title == "first")
        assert(sortedPods[1].title == "second")
        assert(sortedPods[2].title == "third")
        assert(sortedPods[3].title == "fourth")
        job.cancel()
    }
}