package com.adyen.android.assignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.PODsRepository
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Using android viewmodel to help with configuration changes
 */
open class PODViewModel(
    private val podsRepository: PODsRepository,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private var filterType: FilterType = FilterType.TITLE
    private val _uiState = MutableStateFlow<Resource<List<PODImageModel>>>(Resource.Uninitiated)

    val uiState = _uiState.asStateFlow()

    fun loadPlanets() {
        if (_uiState.value !is Resource.Success) { //this will prevent us from reloading image after every configuraton state. Ideally we can use a datastore to update every 24 hours
            _uiState.value = Resource.Loading
            viewModelScope.launch(dispatcher) {

                val response = podsRepository.getImagePlanets()
                if (response is Resource.Success) {
                    val planets = response.data
                    val sortedPlanets = getSortedPODS(planets)
                    _uiState.value = Resource.Success(sortedPlanets)
                } else {
                    _uiState.value = Resource.Error((response as Resource.Error).error)
                }
            }
        }
    }

    fun setFilterType(filterType: FilterType) {
        this.filterType = filterType
        val planets = (_uiState.value as? Resource.Success)?.data ?: emptyList()
        val sortedPlanets = getSortedPODS(planets)
        _uiState.value = Resource.Success(sortedPlanets)
    }

    private fun getSortedPODS(planets: List<PODImageModel>) : List<PODImageModel> {
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
