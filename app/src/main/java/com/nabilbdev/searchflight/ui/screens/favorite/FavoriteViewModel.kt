package com.nabilbdev.searchflight.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class FavoriteUiState(
    val favoriteAirports: List<Airport> = emptyList(),
    val successMessage: String? = null,
    val cancelMessage: String? = null
)

class FavoriteViewModel(
    private val searchFlightRepository: SearchFlightRepository
) : ViewModel() {

    private val _favoriteAirports = MutableStateFlow<List<Airport>>(emptyList())
    private val _successMessage = MutableStateFlow<String?>(null)
    private val _cancelMessage = MutableStateFlow<String?>(null)

    val favoriteUiState: StateFlow<FavoriteUiState> = combine(
        _favoriteAirports, _successMessage, _cancelMessage
    ) { favoriteAirports, successMessage, cancelMessage ->

        FavoriteUiState(
            favoriteAirports = favoriteAirports,
            successMessage = successMessage,
            cancelMessage = cancelMessage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), FavoriteUiState())

    init {
       // TODO: fetch favorite airport list from DB.
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}