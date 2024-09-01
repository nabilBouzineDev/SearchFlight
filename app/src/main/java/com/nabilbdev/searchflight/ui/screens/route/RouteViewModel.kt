package com.nabilbdev.searchflight.ui.screens.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

data class RouteUiState(
    val airportResultSelected: Boolean = false,
    val fromAirport: Airport = AIRPORT_DEFAULT,
    val toAirport: Airport = AIRPORT_DEFAULT,
    val otherAirports: List<Airport> = emptyList(),
)

data class RouteFavButtonUiState(
    val saveToFavoriteSelected: Boolean = false,
    val isFavoriteDisabled: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class RouteViewModel(
    private val searchFlightRepository: SearchFlightRepository
) : ViewModel() {

    private val _airportResultSelected = MutableStateFlow(false)
    private val _fromAirport = MutableStateFlow(AIRPORT_DEFAULT)
    private val _toAirport = MutableStateFlow(AIRPORT_DEFAULT)
    private val _saveToFavoriteSelected = MutableStateFlow(false)
    private val _isFavoriteDisabled = MutableStateFlow(false)
    private val _otherAirports = MutableStateFlow<List<Airport>>(emptyList())

    val routeUiState: StateFlow<RouteUiState> = combine(
        _airportResultSelected, _fromAirport, _toAirport, _otherAirports
    ) { airportResultSelected, fromAirport, toAirport, otherAirports ->
        RouteUiState(
            airportResultSelected = airportResultSelected,
            fromAirport = fromAirport,
            toAirport = toAirport,
            otherAirports = otherAirports
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), RouteUiState())


    val routeFavButtonUiState: StateFlow<RouteFavButtonUiState> = combine(
        _saveToFavoriteSelected, _isFavoriteDisabled
    ) { saveToFavoriteSelected, isFavoriteDisabled ->
        RouteFavButtonUiState(
            saveToFavoriteSelected = saveToFavoriteSelected,
            isFavoriteDisabled = isFavoriteDisabled,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        RouteFavButtonUiState()
    )

    init {
        clearSelection()

        // Observe fromAirport and update the otherAirports list accordingly
        _fromAirport
            .mapLatest { fromAirport ->
                _otherAirports.value =
                    searchFlightRepository.getAllAirportsExceptStream(fromAirport.iataCode).first()
            }
            .launchIn(viewModelScope)

        _toAirport
            .mapLatest { airport ->
                when (airport.name) {
                    AIRPORT_DEFAULT.name -> {
                        _isFavoriteDisabled.value = true
                    }

                    else -> {
                        _isFavoriteDisabled.value = false
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun showSelectionBottomSheet(fromAirport: Airport) {
        _fromAirport.value = fromAirport
        _toAirport.value = AIRPORT_DEFAULT
        _airportResultSelected.value = true
    }

    fun hideSelectionBottomSheet() {
        _airportResultSelected.value = false
        _saveToFavoriteSelected.value = false
    }

    fun onSaveToFavoriteClicked() {
        if (!_isFavoriteDisabled.value) {
            _saveToFavoriteSelected.value = !_saveToFavoriteSelected.value
        }
    }

    fun selectArrivalAirport(airport: Airport) {
        _toAirport.value = airport
    }

    private fun clearSelection() {
        _toAirport.value = AIRPORT_DEFAULT
        _otherAirports.value = emptyList()
        _airportResultSelected.value = false
        _isFavoriteDisabled.value = false
        _saveToFavoriteSelected.value = false
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}