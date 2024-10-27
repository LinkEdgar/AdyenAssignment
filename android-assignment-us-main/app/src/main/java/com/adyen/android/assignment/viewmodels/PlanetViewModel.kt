package com.adyen.android.assignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.PlanetsRepository
import com.adyen.android.assignment.ui.planets.PlanetImageModel
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Using android viewmodel to help with configuration changes
 */
class PlanetViewModel(
    private val planetsRepository: PlanetsRepository
): ViewModel() {

    //todo add tests

    private val filterType: FilterType = FilterType.TITLE
    private val _uiState = MutableStateFlow<Resource<List<PlanetImageModel>>>(Resource.Uninitiated)

    val uiState = _uiState.asStateFlow()

    fun loadPlanets() {
        _uiState.value = Resource.Loading
        viewModelScope.launch {

            val response = planetsRepository.getImagePlanets()
            if (response is Resource.Success) {
                val planets = response.data
                val sortedPlanets = getSortedPlanets(planets)
                _uiState.value = Resource.Success(sortedPlanets)
            } else {
                _uiState.value = Resource.Error((response as Resource.Error).error)
            }
        }
    }

    fun setFilterType(filterType: FilterType) {
        val planets = (_uiState.value as? Resource.Success)?.data ?: emptyList()
        val sortedPlanets = getSortedPlanets(planets)
        _uiState.value = Resource.Success(sortedPlanets)
    }

    fun getSortedPlanets(planets: List<PlanetImageModel>) : List<PlanetImageModel> {
        return when (filterType) {
            FilterType.TITLE -> {
                planets.sortedBy { planet -> planet.title }
            }

            FilterType.DATE -> {
                planets.sortedByDescending { planet -> planet.date }
            }
        }
    }

}

/**
 *  there are other potential categories we could sort by
 */

enum class FilterType {
    TITLE,
    DATE
}
