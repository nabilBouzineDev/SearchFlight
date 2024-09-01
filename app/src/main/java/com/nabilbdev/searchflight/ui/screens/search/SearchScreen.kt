package com.nabilbdev.searchflight.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.R
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.ErrorMessage
import com.nabilbdev.searchflight.ui.components.FilterChipWrapper

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    query: String = "",
    errorMessage: String? = null,
    allAirportsList: List<Airport> = emptyList(),
    airportListByQuery: List<Airport> = emptyList(),
    showFiltersSelected: Boolean = false,
    byPassengersSelected: Boolean = false,
    byNameSelected: Boolean = false,
    viewModel: SearchViewModel,
    onNavigateToAirportRouteSelection: (Airport) -> Unit = {}
) {
    if (query.isEmpty()) {
        if (!showFiltersSelected) {
            FilterSelection(
                byPassengersSelected = byPassengersSelected,
                byNameSelected = byNameSelected,
                onSelectMostVisited = viewModel::onSelectMostVisited,
                onSelectByName = viewModel::onSelectByName
            )
        }
        LazyColumn(modifier = modifier.padding(8.dp)) {
            items(allAirportsList) { airport ->
                SearchResultContent(
                    airport = airport,
                    passengerNumber = viewModel.passengerNumWrapper(airport.passengers),
                    passengerPercentage = viewModel.getPassengerNumberPercentage(
                        airport.passengers
                    ),
                    onAirportCardClicked = { onNavigateToAirportRouteSelection(airport) }
                )
                HorizontalDivider()
            }
        }
    } else {
        when (errorMessage) {
            null -> {
                LazyColumn(modifier = modifier.padding(8.dp)) {
                    items(airportListByQuery) { airport ->
                        SearchResultContent(
                            airport = airport,
                            passengerNumber = viewModel.passengerNumWrapper(airport.passengers),
                            passengerPercentage = viewModel.getPassengerNumberPercentage(
                                airport.passengers
                            ),
                            onAirportCardClicked = { onNavigateToAirportRouteSelection(airport) }
                        )
                        HorizontalDivider()
                    }
                }
            }

            else -> ErrorMessage(errorMessage = errorMessage, errorIconId = R.drawable.flight_error)
        }
    }
}


@Composable
fun FilterSelection(
    byPassengersSelected: Boolean,
    byNameSelected: Boolean,
    onSelectMostVisited: () -> Unit,
    onSelectByName: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        FilterChipWrapper(
            selected = byPassengersSelected,
            onSelectChip = onSelectMostVisited,
            label = "By Passengers",
        )
        FilterChipWrapper(
            selected = byNameSelected,
            onSelectChip = onSelectByName,
            label = "By Name"
        )
    }
}