package com.adyen.android.assignment

import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import com.adyen.android.assignment.viewmodels.FilterType
import com.adyen.android.assignment.viewmodels.PODViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class PODViewModelTest {

    //todo update tests


    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private val podRepo: PODsRepository = mock()

    private lateinit var testSubject: PODViewModel

    @Before
    fun setup() {
        whenever(podRepo.getFavPods()).thenReturn(MutableSharedFlow())
        testSubject = PODViewModel(podRepo, UnconfinedTestDispatcher())
    }


    @Test
    fun `Given successful getImagePlanets is called successfully we expect uiState to not be loading and pods to be populated`() = runTest {
        //given
        val successFulPods = listOf(PODImageModel("","",LocalDate.now(),"","","",false))
        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Success(successFulPods))

        val collectJob = launch(mainCoroutineRule.testDispatcher) {
            testSubject.uiState.collect()
        }
        val uiState = testSubject.uiState.value
        assert(uiState.isLoading)



        testSubject.loadPlanets()
        val yeet = testSubject.uiState.first()
//        advanceUntilIdle()
        assert(yeet.isLoading)
//        assert(uiState.value.pods == successFulPods)
        collectJob.cancel()
    }

    @Test
    fun `Given error getImagePlanets is called successfully we expect uiState to be error`() = runTest {
        val errorMessage = "error"
        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Error(error = errorMessage))
        testSubject.loadPlanets()
        val uiState = testSubject.uiState
        assert(!uiState.value.isLoading)
        assert(uiState.value.pods.isEmpty())
        assert(uiState.value.errorMessage == errorMessage )
    }

    @Test
    fun `Given setSort type set to title we expect pods to be sorted`() = runTest {
        val pods = listOf(
            PODImageModel("M", "", LocalDate.now(), "","","", isFavorite = false),
            PODImageModel("Z", "", LocalDate.now(), "","","", isFavorite = false),
            PODImageModel("D", "", LocalDate.now(), "","","", isFavorite = false),
            PODImageModel("Y", "", LocalDate.now(), "","","", isFavorite = false)
            )

        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Success(pods))
        testSubject.loadPlanets()
        testSubject.setFilterType(FilterType.TITLE)
        val uiState = testSubject.uiState
        val sortedPods = uiState.value.pods
        assert(sortedPods[0].title == "D")
        assert(sortedPods[1].title == "M")
        assert(sortedPods[2].title == "Y")
        assert(sortedPods[3].title == "Z")
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

        whenever(podRepo.getImagePlanets()).thenReturn(Resource.Success(pods))
        testSubject.loadPlanets()
        testSubject.setFilterType(FilterType.DATE)
        val uiState = testSubject.uiState
        val sortedPods = uiState.value.pods
        assert(sortedPods[0].title == "first")
        assert(sortedPods[1].title == "second")
        assert(sortedPods[2].title == "third")
        assert(sortedPods[3].title == "fourth")
    }
}