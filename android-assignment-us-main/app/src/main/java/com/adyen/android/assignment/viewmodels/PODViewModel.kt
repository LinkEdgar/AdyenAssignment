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
) : ViewModel() {

    private var filterType: FilterType = FilterType.TITLE
    private val _uiState = MutableStateFlow<Resource<List<PODImageModel>>>(Resource.Uninitiated)

    //POD in detail flow
    private val _detailPod = MutableStateFlow<PODImageModel?>(null)

    val uiState = _uiState.asStateFlow()

    val detailPodState = _detailPod.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            podsRepository.getFavPods().collect { favPods ->
                //update detail pod
                val detailPod = _detailPod.value
                if (favPods.contains(detailPod)) {
                    _detailPod.value = _detailPod.value?.copy(isFavorite = true)
                } else {
                    _detailPod.value = _detailPod.value?.copy(isFavorite = false)
                }

                val state = _uiState.value
                if (state is Resource.Success) {
                    val favPodBuilder = mutableListOf<PODImageModel>()
                    val pods = state.data
                    pods.forEach { pod ->
                        if (favPods.contains(pod)) {
                            favPodBuilder.add(pod.copy(isFavorite = true))
                        } else {
                            favPodBuilder.add(pod)
                        }
                    }
                    _uiState.value = Resource.Success(favPodBuilder)
                }
            }
        }
    }


    fun addPODToFavorite(pod: PODImageModel) {
        viewModelScope.launch(dispatcher) {
            podsRepository.addPod(pod)
        }
    }

    fun removePodFromFavorites(pod: PODImageModel) {
        viewModelScope.launch(dispatcher) {
            podsRepository.removePodFromFavorites(pod)
        }
    }

    /**
     * Returns POD given a successful getPODImages call
     * otherwise returns false
     * @param id --> UUID for POD
     */
    fun loadPod(id: String) {
        val pod = (_uiState.value as? Resource.Success)?.data?.find { it.id == id }
        _detailPod.value = pod
    }

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

    private fun getSortedPODS(planets: List<PODImageModel>): List<PODImageModel> {
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
