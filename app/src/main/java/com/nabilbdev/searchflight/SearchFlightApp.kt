package com.nabilbdev.searchflight

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nabilbdev.searchflight.ui.AppViewModelProvider
import com.nabilbdev.searchflight.ui.screens.search.LoadUiState
import com.nabilbdev.searchflight.ui.screens.search.MySearchBar
import com.nabilbdev.searchflight.ui.screens.search.SearchScreen
import com.nabilbdev.searchflight.ui.screens.search.SearchScreenViewModel
import com.nabilbdev.searchflight.ui.screens.search.SearchUiState
import com.nabilbdev.searchflight.ui.screens.search.SelectUiState

@Composable
fun SearchFlightApp() {

    val viewModel: SearchScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val searchUiState: SearchUiState = viewModel.searchUiState.collectAsState().value
    val selectUiState: SelectUiState = viewModel.selectUiState.collectAsState().value
    val loadUiState: LoadUiState = viewModel.loadUiState.collectAsState().value

    Scaffold(
        topBar = {
            MySearchBar(
                query = searchUiState.searchQuery,
                errorMessage = loadUiState.errorMessage,
                allAirportsList = selectUiState.allAirportList,
                airportListByQuery = searchUiState.airportListByQuery,
                viewModel = viewModel
            )
        }
    ) { innerPadding ->
        SearchScreen(
            popularCitiesAirports = searchUiState.popularCityAirports,
            isLoadingAirports = loadUiState.isLoadingPopularCityAirports,
            modifier = Modifier.padding(innerPadding)
        )
    }
}