package com.nabilbdev.searchflight.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.R
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.CommonAirportVerticalGrid
import com.nabilbdev.searchflight.ui.components.CustomCircularChart
import com.nabilbdev.searchflight.ui.components.ErrorMessage
import com.nabilbdev.searchflight.ui.components.FilterButton
import com.nabilbdev.searchflight.ui.components.FilterChipWrapper
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT
import com.nabilbdev.searchflight.ui.theme.SearchFlightTheme

@Composable
fun SearchScreen(
    popularCitiesAirports: List<Airport>,
    isLoadingAirports: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TODO("Recent Searches")
        // TODO("Favorites")
        Spacer(modifier = Modifier.height(8.dp))
        CommonAirportVerticalGrid(
            titleContent = "Popular Cities Flights",
            airportList = popularCitiesAirports,
            isLoadingAirports = isLoadingAirports,
            imageVector = Icons.Outlined.Search
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    query: String,
    errorMessage: String?,
    allAirportsList: List<Airport>,
    airportListByQuery: List<Airport>,
    viewModel: SearchScreenViewModel,
    modifier: Modifier = Modifier
) {
    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = when (active) {
            false -> modifier
                .fillMaxWidth()
                .padding(12.dp)

            else -> modifier.fillMaxSize()
        },
        enabled = true,
        query = query,
        onQueryChange = { newWord -> viewModel.setNewSearchQuery(newWord) },
        onSearch = {
            // Navigate to search routes
        },
        active = active,
        onActiveChange = { inactive -> active = inactive },
        placeholder = { Text(text = "Search departures...") },
        leadingIcon = {
            if (active) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        viewModel.clearSearch()
                        active = false
                    }
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            }
        },
        trailingIcon = {
            if (active) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Clear your search",
                    modifier = Modifier.clickable {
                        // To clear the user's typing search
                        viewModel.clearSearch()
                    }
                )
            }
        }
    ) {
        if (query.isEmpty()) {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(airportListByQuery) { airport ->
                    SearchResultBody(
                        airport = airport,
                        passengerNumber = viewModel.passengerNumWrapper(airport.passengers.toLong()),
                        passengerPercentage = viewModel.getPassengerNumberPercentage(airport.passengers)
                    )
                    HorizontalDivider()
                }
            }
        }
        when (errorMessage) {
            null -> {
                when (query) {
                    "" -> {
                        FilterSelection()
                        LazyColumn(modifier = Modifier.padding(8.dp)) {
                            items(allAirportsList) { airport ->
                                SearchResultBody(
                                    airport = airport,
                                    passengerNumber = viewModel.passengerNumWrapper(airport.passengers.toLong()),
                                    passengerPercentage = viewModel.getPassengerNumberPercentage(
                                        airport.passengers
                                    )
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    else -> {
                        LazyColumn(modifier = Modifier.padding(8.dp)) {
                            items(airportListByQuery) { airport ->
                                SearchResultBody(
                                    airport = airport,
                                    passengerNumber = viewModel.passengerNumWrapper(airport.passengers.toLong()),
                                    passengerPercentage = viewModel.getPassengerNumberPercentage(
                                        airport.passengers
                                    )
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            else -> ErrorMessage(errorMessage = errorMessage, errorIconId = R.drawable.flight_error)
        }
    }
}

@Composable
fun FilterSelection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        FilterChipWrapper(label = "Most Visited")
        FilterChipWrapper(label = "By Name")
        FilterButton(
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun SearchResultBody(
    airport: Airport,
    passengerNumber: String,
    passengerPercentage: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        shape = CardDefaults.elevatedShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.6f),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.flight_from),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "From",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    text = airport.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f),
                contentAlignment = Alignment.TopEnd
            ) {
                CustomCircularChart(indicatorValuePercentage = passengerPercentage) {
                    Text(
                        text = "Passengers",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                    Text(
                        text = passengerNumber,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SearchResultBodyPreview() {
    SearchFlightTheme {
        SearchResultBody(
            airport = AIRPORT_DEFAULT,
            passengerPercentage = 9,
            passengerNumber = "1.4M"
        )
    }
}
