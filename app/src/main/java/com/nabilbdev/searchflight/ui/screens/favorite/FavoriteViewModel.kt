package com.nabilbdev.searchflight.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.entity.Favorite
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoriteUiState(
    val favoriteAirports: Map<Airport, Airport> = emptyMap(),
    val statusMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModel(
    private val searchFlightRepository: SearchFlightRepository
) : ViewModel() {

    private val _airportHolder = MutableStateFlow<Map<Airport, Airport>>(emptyMap())
    private val _favoriteAirports = MutableStateFlow<Map<Airport, Airport>>(emptyMap())
    private val _statusMessage = MutableStateFlow<String?>(null)

    val favoriteUiState: StateFlow<FavoriteUiState> = combine(
        _favoriteAirports, _statusMessage
    ) { favoriteAirports, statusMessage ->

        FavoriteUiState(
            favoriteAirports = favoriteAirports,
            statusMessage = statusMessage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), FavoriteUiState())

    init {
        fetchFavoriteAirports()
    }

    private fun fetchFavoriteAirports() {
        viewModelScope.launch {
            searchFlightRepository.getAllFavoriteAirportsStream().collect { favoriteAirports ->
                if (favoriteAirports.isEmpty()) {
                    _statusMessage.value = "You have no favorites yet!"
                } else {
                    val favoriteMap = favoriteAirports.associate { favorite ->
                        val departureAirport = searchFlightRepository
                            .getAirportByCodeStream(favorite.departureCode)
                            .filterNotNull()
                            .distinctUntilChanged()
                            .first()

                        val destinationAirport = searchFlightRepository
                            .getAirportByCodeStream(favorite.destinationCode)
                            .filterNotNull()
                            .distinctUntilChanged()
                            .first()

                        departureAirport to destinationAirport
                    }

                    _favoriteAirports.value = favoriteMap
                    _statusMessage.value = null
                }
            }
        }
    }

    fun saveToFavorites(favorite: Favorite) {
        viewModelScope.launch {
            searchFlightRepository.insertFavoriteAirport(favorite)
        }
    }

    fun deleteFromFavorites(favorite: Favorite) {
        viewModelScope.launch {
            searchFlightRepository.insertFavoriteAirport(favorite)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}