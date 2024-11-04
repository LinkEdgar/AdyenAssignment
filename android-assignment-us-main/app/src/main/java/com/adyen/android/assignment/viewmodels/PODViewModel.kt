package com.adyen.android.assignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.PODsRepository
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Using android viewmodel to help with configuration changes
 */
open class PODViewModel(
    private val podsRepository: PODsRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var filterType: MutableStateFlow<FilterType> = MutableStateFlow(FilterType.TITLE)

    //POD in detail flow
    private val _detailPod = MutableStateFlow<PODImageModel?>(null)

    private val _loadPods = MutableStateFlow<Resource<List<PODImageModel>>>(Resource.Uninitiated)

    val uiState: StateFlow<PODsUi> = combine(
        filterType, _loadPods, podsRepository.getFavPods()
    ) { type, podsState, favPods ->

        _detailPod.value =
            _detailPod.value?.copy(isFavorite = favPods.contains(_detailPod.value))

        when (podsState) {
            is Resource.Success -> {
                val sortedPods = getSortedPODS(
                    podsState.data,
                    type
                ).map { pod -> pod.copy(isFavorite = favPods.contains(pod)) }
                PODsUi(
                    isLoading = false,
                    pods = sortedPods,
                    favoriteList = favPods
                )
            }

            is Resource.Loading -> {
                PODsUi(
                    isLoading = true
                )

            }

            is Resource.Error -> {
                PODsUi(
                    isLoading = false,
                    errorMessage = podsState.error
                )

            }

            else -> {
                PODsUi()
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, PODsUi())

    val detailPodState = _detailPod.asStateFlow()

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
        val pod = uiState.value.pods.find { it.id == id }
        _detailPod.value = pod
    }

    fun loadPlanets() {
        if (_loadPods.value !is Resource.Success) { //this will prevent us from reloading image after every configuraton state. Ideally we can use a datastore to update every 24 hours
            _loadPods.value = Resource.Loading
            viewModelScope.launch(dispatcher) {
                val response = podsRepository.getImagePlanets()
                _loadPods.value = response
            }
        }
    }

    fun setFilterType(filterType: FilterType) {
        this.filterType.value = filterType
    }

    private fun getSortedPODS(planets: List<PODImageModel>, type: FilterType): List<PODImageModel> {
        return when (type) {
            FilterType.TITLE -> {
                planets.sortedBy { planet -> planet.title }
            }

            FilterType.DATE -> {
                planets.sortedByDescending { planet -> planet.date }
            }
        }
    }

}

data class PODsUi(
    val isLoading: Boolean = true,
    val pods: List<PODImageModel> = emptyList(),
    val errorMessage: String? = null,
    val favoriteList: List<PODImageModel> = emptyList()
)

/**
 *  there are other potential categories we could sort by
 */

enum class FilterType {
    TITLE,
    DATE
}
