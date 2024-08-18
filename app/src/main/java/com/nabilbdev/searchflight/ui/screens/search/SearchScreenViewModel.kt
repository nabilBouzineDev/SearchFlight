package com.nabilbdev.searchflight.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository
import com.nabilbdev.searchflight.ui.screens.search.utils.popularCitiesAirportCodeList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
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
    val popularCityAirports: List<Airport> = emptyList(),
)

data class SelectUiState(
    val allAirportList: List<Airport> = emptyList()
)

data class LoadUiState(
    val isLoadingPopularCityAirports: Boolean = true,
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchScreenViewModel(
    private val searchFlightRepository: SearchFlightRepository
) : ViewModel() {

    /**
     * A set of observable data holders: holds data from flows observed in Room DB.
     */
    private val _searchQuery = MutableStateFlow("")
    private val _allAirportList = MutableStateFlow<List<Airport>>(emptyList())
    private val _airportListByQuery = MutableStateFlow<List<Airport>>(emptyList())
    private val _popularCitiesAirport = MutableStateFlow<List<Airport>>(emptyList())
    private val _isLoadingPopularCityAirports = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private var _maxPassengerNumber = MutableStateFlow(0)


    /**
     * A Ui State that expose updates related to filtering and get airports list to the UI
     */
    val selectUiState: StateFlow<SelectUiState> = _allAirportList
        .mapLatest {
            when {
                _searchQuery.value.isEmpty() -> {
                    _allAirportList.value = searchFlightRepository.getAllAirportsStream()
                        .first()
                        .onEach { airport -> setMaxPassengerNumber(airport.passengers) }
                }

                else -> _allAirportList.value = emptyList()
            }

            SelectUiState(allAirportList = _allAirportList.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), SelectUiState())

    /**
     * A Ui State that expose updates related to search and airports list to the UI
     */
    val searchUiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        _airportListByQuery,
        _popularCitiesAirport,
    ) { searchQuery, airportListByQuery, popularCityAirports ->

        if (searchQuery.isBlank())
            clearSearch()

        SearchUiState(
            searchQuery = searchQuery,
            airportListByQuery = airportListByQuery,
            popularCityAirports = popularCityAirports,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), SearchUiState())


    /**
     * A Ui State that expose updates related to the loading & error state of the airports lists to the UI
     */
    val loadUiState: StateFlow<LoadUiState> = combine(
        _isLoadingPopularCityAirports,
        _errorMessage
    ) { isLoadingPopularCity, errorMessage ->
        LoadUiState(
            isLoadingPopularCityAirports = isLoadingPopularCity,
            errorMessage = errorMessage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), LoadUiState())

    init {
        clearSearch()
        fetchPopularCityAirports()

        // Observe search query and update the airport list accordingly
        _searchQuery
            .debounce(300)
            .filter { it.length > 1 }
            .distinctUntilChanged()
            .onEach { query ->
                _airportListByQuery.value =
                    searchFlightRepository.getAirportsByQueryStream("%$query%")
                        .first()
                        .onEach { airport -> setMaxPassengerNumber(airport.passengers) }

                _errorMessage.value = when (_airportListByQuery.value.size) {
                    0 -> "Oops, we can't find this in our flight database!"
                    else -> null
                }
            }
            .launchIn(viewModelScope)
    }

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

    private fun setMaxPassengerNumber(passengers: Int) {
        _maxPassengerNumber.value =
            maxOf(passengers, _maxPassengerNumber.value)
    }

    /**
     * TODO: Getting recent Searches of the user.
     */

    fun setNewSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _errorMessage.value = null
        _airportListByQuery.value = emptyList()
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
    fun getPassengerNumberPercentage(passengers: Int): Int {
        return round((passengers.toDouble() / _maxPassengerNumber.value) * 100).toInt()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}