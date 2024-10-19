package com.nabilbdev.searchflight

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nabilbdev.searchflight.data.local.entity.Favorite
import com.nabilbdev.searchflight.ui.AppViewModelProvider
import com.nabilbdev.searchflight.ui.screens.favorite.FavoriteScreen
import com.nabilbdev.searchflight.ui.screens.favorite.FavoriteUiState
import com.nabilbdev.searchflight.ui.screens.favorite.FavoriteViewModel
import com.nabilbdev.searchflight.ui.screens.home.HomeScreen
import com.nabilbdev.searchflight.ui.screens.home.HomeUiState
import com.nabilbdev.searchflight.ui.screens.home.HomeViewModel
import com.nabilbdev.searchflight.ui.screens.route.RouteFavButtonUiState
import com.nabilbdev.searchflight.ui.screens.route.RouteScreen
import com.nabilbdev.searchflight.ui.screens.route.RouteUiState
import com.nabilbdev.searchflight.ui.screens.route.RouteViewModel
import com.nabilbdev.searchflight.ui.screens.search.MySearchBar
import com.nabilbdev.searchflight.ui.screens.search.SearchUiState
import com.nabilbdev.searchflight.ui.screens.search.SearchViewModel
import com.nabilbdev.searchflight.ui.screens.search.SelectFiltersUiState

@Composable
fun SearchFlightApp() {

    val searchVM: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val homeVM: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val routeVM: RouteViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val favoriteVM: FavoriteViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val searchUiState: SearchUiState = searchVM.searchUiState.collectAsStateWithLifecycle().value
    val selectFiltersUiState: SelectFiltersUiState =
        searchVM.selectFiltersUiState.collectAsStateWithLifecycle().value
    val homeUiState: HomeUiState = homeVM.homeUiState.collectAsStateWithLifecycle().value

    val routeUiState: RouteUiState = routeVM.routeUiState.collectAsStateWithLifecycle().value
    val routeFavButtonUiState: RouteFavButtonUiState =
        routeVM.routeFavButtonUiState.collectAsStateWithLifecycle().value

    val favoriteUiState: FavoriteUiState =
        favoriteVM.favoriteUiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            MySearchBar(
                query = searchUiState.searchQuery,
                errorMessage = searchUiState.errorMessage,
                allAirportsList = selectFiltersUiState.allAirportList,
                airportListByQuery = searchUiState.airportListByQuery,
                showFiltersSelected = selectFiltersUiState.showFilters,
                byPassengersSelected = selectFiltersUiState.byPassengersSelected,
                byNameSelected = selectFiltersUiState.byNameSelected,
                viewModel = searchVM,
                onAirportSelected = { fromAirport ->
                    routeVM.showSelectionBottomSheet(fromAirport)
                }
            )
        }
    ) { innerPadding ->
        when (routeUiState.airportResultSelected) {
            true -> {
                RouteScreen(
                    fromAirport = routeUiState.fromAirport,
                    toAirport = routeUiState.toAirport,
                    otherAirports = routeUiState.otherAirports,
                    onHideBottomSheet = routeVM::hideSelectionBottomSheet,
                    onArrivalAirportSelected = routeVM::selectArrivalAirport,
                    saveToFavoriteSelected = routeFavButtonUiState.saveToFavoriteSelected,
                    isFavButtonDisabled = routeFavButtonUiState.isFavoriteDisabled,
                    onSaveToFavoriteClicked = { favorite ->
                        favoriteVM.saveToFavorites(favorite)
                        routeVM.onSaveToFavoriteClicked()
                    }
                )
            }

            false -> {
                /*HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    popularCitiesAirports = homeUiState.popularCityAirports,
                    isLoadingAirports = homeUiState.isLoadingPopularCityAirports
                )*/
                FavoriteScreen(
                    modifier = Modifier.padding(innerPadding),
                    statusMessage = favoriteUiState.statusMessage,
                    favoriteRoutes = favoriteUiState.favoriteAirports,
                    viewModel = favoriteVM
                )
            }
        }
    }
}