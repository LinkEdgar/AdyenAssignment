package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class PlanetRepositoryTest {

    private lateinit var planetsRepository: PODsRepository

    @Mock
    private lateinit var planetaryService : PlanetaryService

    @Mock
    private lateinit var podStorage: PODStorageImp

    @Before
    fun setup() {
        planetaryService = mock()
        podStorage = mock()
        planetsRepository = PODsRepository(planetaryService, podStorage)
    }

    @Test
    fun `Given a successful response from planetaryService's getPictures we expect a resource success`() = runTest {
        whenever(planetaryService.getPictures()).thenReturn(Response.success(emptyList()))
        val response = planetsRepository.getImagePlanets()
        assert(response is Resource.Success)
    }


    @Test
    fun `Given an error response from planetaryService's getPictures we expect a resource error`() = runTest {
        whenever(planetaryService.getPictures()).thenReturn(Response.error(400,ResponseBody.create(null,"")))
        val response = planetsRepository.getImagePlanets()
        assert(response is Resource.Error)
    }
}