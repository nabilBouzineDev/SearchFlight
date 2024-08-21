package com.nabilbdev.searchflight

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nabilbdev.searchflight.ui.AppViewModelProvider
import com.nabilbdev.searchflight.ui.screens.home.HomeScreen
import com.nabilbdev.searchflight.ui.screens.home.HomeUiState
import com.nabilbdev.searchflight.ui.screens.home.HomeViewModel
import com.nabilbdev.searchflight.ui.screens.search.MySearchBar
import com.nabilbdev.searchflight.ui.screens.search.SearchUiState
import com.nabilbdev.searchflight.ui.screens.search.SearchViewModel
import com.nabilbdev.searchflight.ui.screens.search.SelectUiState

@Composable
fun SearchFlightApp() {

    val searchVM: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val homeVM: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val searchUiState: SearchUiState = searchVM.searchUiState.collectAsState().value
    val selectUiState: SelectUiState = searchVM.selectUiState.collectAsState().value
    val homeUiState: HomeUiState = homeVM.homeUiState.collectAsState().value

    Scaffold(
        topBar = {
            MySearchBar(
                query = searchUiState.searchQuery,
                errorMessage = searchUiState.errorMessage,
                allAirportsList = selectUiState.allAirportList,
                airportListByQuery = searchUiState.airportListByQuery,
                showFiltersSelected = selectUiState.showFilters,
                mostVisitedSelected = selectUiState.mostVisitedSelected,
                byNameSelected = selectUiState.byNameSelected,
                viewModel = searchVM
            )
        }
    ) { innerPadding ->
        HomeScreen(
            popularCitiesAirports = homeUiState.popularCityAirports,
            isLoadingAirports = homeUiState.isLoadingPopularCityAirports,
            modifier = Modifier.padding(innerPadding)
        )
    }
}