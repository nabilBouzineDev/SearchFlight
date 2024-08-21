package com.nabilbdev.searchflight.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import com.nabilbdev.searchflight.ui.screens.search.utils.popularCitiesAirportCodeList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class HomeUiState(
    val popularCityAirports: List<Airport> = emptyList(),
    val isLoadingPopularCityAirports: Boolean = true,
)


class HomeViewModel(
    private val searchFlightRepository: SearchFlightRepository
) : ViewModel() {

    /**
     * A set of observable data holders: holds data from flows observed in Room DB.
     */
    private val _popularCitiesAirport = MutableStateFlow<List<Airport>>(emptyList())
    private val _isLoadingPopularCityAirports = MutableStateFlow(true)

    /**
     * A Ui State that expose updates related to the home screen
     */
    val homeUiState: StateFlow<HomeUiState> = combine(
        _popularCitiesAirport,
        _isLoadingPopularCityAirports
    ) { popularCityAirports, isLoadingPopularCity ->

        HomeUiState(
            popularCityAirports = popularCityAirports,
            isLoadingPopularCityAirports = isLoadingPopularCity
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), HomeUiState())

    init {
        fetchPopularCityAirports()
    }


    /**
     * TODO: Getting recent Searches of the user.
     */


    /**
     * Get a list of airports based onf popular cities airport code [popularCitiesAirportCodeList]
     */
    private fun fetchPopularCityAirports() {
        viewModelScope.launch {
            val airports = popularCitiesAirportCodeList.map { cityCode ->
                searchFlightRepository.getAirportByCodeStream(cityCode).filterNotNull().first()
            }
            _popularCitiesAirport.value = airports
            _isLoadingPopularCityAirports.value = false
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}