package com.nabilbdev.searchflight.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import com.nabilbdev.searchflight.data.local.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.round


data class SearchUiState(
    val searchQuery: String = "",
    val airportListByQuery: List<Airport> = emptyList(),
    val errorMessage: String? = null
)

data class SelectFiltersUiState(
    val allAirportList: List<Airport> = emptyList(),
    val byPassengersSelected: Boolean = false,
    val byNameSelected: Boolean = false,
    val showFilters: Boolean = false,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchFlightRepository: SearchFlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * A set of observable data holders: holds data from flows observed in Room DB.
     */
    private val _searchQuery = MutableStateFlow("")
    private val _airportListByQuery = MutableStateFlow<List<Airport>>(emptyList())
    private val _allAirportList = MutableStateFlow<List<Airport>>(emptyList())
    private val _showFilters = MutableStateFlow(false)
    private val _byPassengersSelected = MutableStateFlow(false)
    private val _byNameSelected = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private var _maxPassengerNumber = MutableStateFlow<Long>(0)


    /**
     * A Ui State that expose updates related to filtering and get airports list to the UI
     */
    val selectFiltersUiState: StateFlow<SelectFiltersUiState> = combine(
        _allAirportList, _showFilters, _byPassengersSelected, _byNameSelected
    ) { allAirportList, showFilters, byPassengersSelected, byNameSelected ->
        SelectFiltersUiState(
            allAirportList = allAirportList,
            showFilters = showFilters,
            byPassengersSelected = byPassengersSelected,
            byNameSelected = byNameSelected
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), SelectFiltersUiState())

    /**
     * A Ui State that expose updates related to search and airports list to the UI
     */
    val searchUiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        _airportListByQuery,
        _errorMessage
    ) { searchQuery, airportListByQuery, errorMessage ->

        if (searchQuery.isBlank())
            clearSearch()

        SearchUiState(
            searchQuery = searchQuery,
            airportListByQuery = airportListByQuery,
            errorMessage = errorMessage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), SearchUiState())

    init {
        clearSearch()

        // Observe search query and update the airport list accordingly
        _searchQuery
            .debounce(300)
            .filter { it.length > 1 }
            .distinctUntilChanged()
            .onEach { query ->
                _airportListByQuery.value =
                    searchFlightRepository.getAirportsByQueryStream("%$query%")
                        .first()

                _errorMessage.value = when (_airportListByQuery.value.size) {
                    0 -> "Oops, we can't find this in our flight database!"
                    else -> null
                }
            }
            .launchIn(viewModelScope)

        // Observe the filter option and sort airport list by number of passengers
        _byPassengersSelected
            .mapLatest { mostVisitedSelected ->
                when (mostVisitedSelected) {
                    true -> {
                        _byNameSelected.value = false
                        _allAirportList.value =
                            searchFlightRepository.getAllAirportsOrderedByPassengersStream()
                                .first()
                    }

                    false -> {
                        fetchAllAirports()
                    }
                }
            }
            .launchIn(viewModelScope)

        // Observe the filter option and sort airport list by name in alphabetical order.
        _byNameSelected
            .mapLatest { byNameSelected ->
                when (byNameSelected) {
                    true -> {
                        _byPassengersSelected.value = false
                        _allAirportList.value =
                            searchFlightRepository.getAllAirportsOrderedByNameStream()
                                .first()
                    }

                    false -> {
                        fetchAllAirports()
                    }
                }
            }
            .launchIn(viewModelScope)

        // Collect all filter preferences and update the state
        viewModelScope.launch {
            userPreferencesRepository.showFilters
                .collect { showFilters ->
                    _showFilters.value = showFilters
                }
        }

        // Save the showFilters state when it changes
        _showFilters
            .mapLatest { showFilters ->
                userPreferencesRepository.saveShowFiltersPreference(showFilters)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Get all airports data
     */
    private fun fetchAllAirports() {
        when {
            _searchQuery.value.isEmpty() -> {
                viewModelScope.launch {
                    _allAirportList.value = searchFlightRepository.getAllAirportsStream()
                        .first()
                        .onEach { airport -> setMaxPassengerNumber(airport.passengers) }
                }
            }

            else -> _allAirportList.value = emptyList()
        }
    }

    private fun setMaxPassengerNumber(passengers: Long) {
        _maxPassengerNumber.value =
            maxOf(passengers, _maxPassengerNumber.value)
    }

    /**
     * Format the passenger number in more readable format:
     *  instead of `1000000` we do `1M`
     */
    fun passengerNumWrapper(passengers: Long): String {
        return when {
            passengers >= 1_000_000 -> String.format(Locale.US, "%.1fM", passengers / 1_000_000.0)
            passengers >= 1_000 -> String.format(Locale.US, "%.1fk", passengers / 1_000.0)
            else -> passengers.toString()
        }.replace(".0", "")
    }

    /**
     * Get the number of passenger in percentage:
     *  Based on the airport that contains the max number of passengers
     */
    fun getPassengerNumberPercentage(passengers: Long): Int {
        return round((passengers.toDouble() / _maxPassengerNumber.value) * 100).toInt()
    }

    fun setNewSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _errorMessage.value = null
        _airportListByQuery.value = emptyList()
    }

    private fun clearFilters() {
        _byPassengersSelected.value = false
        _byNameSelected.value = false
    }

    fun onSelectMostVisited() {
        _byPassengersSelected.value = !_byPassengersSelected.value
    }

    fun onSelectByName() {
        _byNameSelected.value = !_byNameSelected.value
    }

    fun onShowFilters() {
        clearFilters()
        _showFilters.value = !_showFilters.value
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}