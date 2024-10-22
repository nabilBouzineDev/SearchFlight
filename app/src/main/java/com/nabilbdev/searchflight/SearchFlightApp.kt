package com.nabilbdev.searchflight

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.nabilbdev.searchflight.utils.DraggableScreen

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

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomEnd)
                    .padding(4.dp)
            ) {
                SnackbarHost(hostState = snackBarHostState)
            }
        },
        topBar = {
            MySearchBar(
                query = searchUiState.searchQuery,
                active = searchUiState.activeSearchBar,
                isHomeSearchCardClicked = searchUiState.isHomeSearchCardClicked,
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
                    favoriteStatus = favoriteUiState
                        .favoriteStatus,
                    fromAirport = routeUiState.fromAirport,
                    toAirport = routeUiState.toAirport,
                    otherAirports = routeUiState.otherAirports,
                    isFavButtonDisabled = routeFavButtonUiState.isFavoriteDisabled,

                    clearMessageAndButtonSelection = {
                        favoriteVM.clearFavoriteStatusAndMessage()
                    },
                    onSaveToFavoriteClicked = { favorite ->
                        favoriteVM.saveToFavorites(favorite)
                        routeVM.onSaveToFavoriteClicked()
                    },
                    onHideBottomSheet = routeVM::hideSelectionBottomSheet,
                    onArrivalAirportSelected = routeVM::selectArrivalAirport
                )
            }

            false -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    HomeScreen(
                        popularCitiesAirports = homeUiState.popularCityAirports,
                        isLoadingAirports = homeUiState.isLoadingPopularCityAirports,
                        onSearchClick = { search ->
                            searchVM.onShowSearchBarActiveByHomeCard()
                            searchVM.setNewSearchQuery(search)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DraggableScreen(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        FavoriteScreen(
                            deletedOrNoFavoriteStatusMessage = favoriteUiState
                                .deletedOrNoFavoriteStatusMessage,
                            favoriteRoutes = favoriteUiState.favoriteAirports,
                            viewModel = favoriteVM,
                            snackBarHostState = snackBarHostState
                        )
                    }
                }
            }
        }
    }
}