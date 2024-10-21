package com.nabilbdev.searchflight.ui.screens.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.entity.Favorite
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoriteUiState(
    val favoriteAirports: List<Pair<Airport, Airport>> = emptyList(),
    val deletedOrNoFavoriteStatusMessage: String? = null,
    val addedOrAlreadyAddedStatusMessage: String? = null,
)

class FavoriteViewModel(
    private val searchFlightRepository: SearchFlightRepository
) : ViewModel() {

    private val _favoriteAirports = MutableStateFlow<List<Pair<Airport, Airport>>>(emptyList())
    private val _deletedOrNoFavoriteStatusMessage = MutableStateFlow<String?>(null)
    private val _addedOrAlreadyAddedStatusMessage = MutableStateFlow<String?>(null)

    var isCurrentlyDragging by mutableStateOf(false)
        private set

    val favoriteUiState: StateFlow<FavoriteUiState> = combine(
        _favoriteAirports, _deletedOrNoFavoriteStatusMessage, _addedOrAlreadyAddedStatusMessage
    ) { favoriteAirports, deletedOrNoFavoriteStatusMessage, addedOrAlreadyAddedStatusMessage ->

        FavoriteUiState(
            favoriteAirports = favoriteAirports,
            deletedOrNoFavoriteStatusMessage = deletedOrNoFavoriteStatusMessage,
            addedOrAlreadyAddedStatusMessage = addedOrAlreadyAddedStatusMessage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), FavoriteUiState())

    init {
        fetchFavoriteAirports()
    }

    private fun fetchFavoriteAirports() {
        clearStatusMessage()
        viewModelScope.launch {
            searchFlightRepository.getAllFavoriteAirportsStream().collect { favoriteAirports ->
                val favoritePair = favoriteAirports.map { favorite ->
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

                    Pair(departureAirport, destinationAirport)
                }


                if (favoritePair.isEmpty()) {
                    _deletedOrNoFavoriteStatusMessage.value = "No favorites yet!"
                    _favoriteAirports.value = emptyList()
                } else {
                    _favoriteAirports.value = favoritePair
                }
            }
        }
    }

    fun saveToFavorites(favorite: Favorite) {
        if (
            _favoriteAirports.value
                .any {
                    it.first.iataCode == favorite.departureCode &&
                            it.second.iataCode == favorite.destinationCode
                }
        ) {
            _addedOrAlreadyAddedStatusMessage.value = "FAVORITE ALREADY SAVED"
            return
        }
        viewModelScope.launch {
            searchFlightRepository.insertFavoriteAirport(favorite)
            _addedOrAlreadyAddedStatusMessage.value = "ADDED TO FAVORITES"
        }
    }

    fun deleteFromFavorites(favoriteRoute: Pair<Airport, Airport>) {
        viewModelScope.launch {
            val favorite = searchFlightRepository
                .getFavoriteByDepartureCodeAndDestinationCodeStream(
                    departureCode = favoriteRoute.first.iataCode,
                    destinationCode = favoriteRoute.second.iataCode
                )
                .filterNotNull()
                .firstOrNull()

            if (favorite != null) {
                searchFlightRepository.deleteFavoriteAirport(favorite)
                _deletedOrNoFavoriteStatusMessage.value = "Deleted successfully!"
            }
        }
    }

    fun startDragging() {
        isCurrentlyDragging = true
    }

    fun stopDragging() {
        isCurrentlyDragging = false
    }

    fun clearStatusMessage() {
        _deletedOrNoFavoriteStatusMessage.value = null
        _addedOrAlreadyAddedStatusMessage.value = null
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}