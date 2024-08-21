package com.nabilbdev.searchflight.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.HideFilterDropdownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    query: String,
    errorMessage: String?,
    allAirportsList: List<Airport>,
    airportListByQuery: List<Airport>,
    showFiltersSelected: Boolean,
    mostVisitedSelected: Boolean,
    byNameSelected: Boolean,
    viewModel: SearchViewModel,
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
        onSearch = {/*Navigate to search routes*/ },
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
                if (query.isEmpty()) {
                    HideFilterDropdownMenu(
                        showFiltersSelected = showFiltersSelected,
                        onHideFilters = viewModel::onHideFilters,
                    )
                } else {
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
        }
    ) {
        SearchScreen(
            query = query,
            errorMessage = errorMessage,
            allAirportsList = allAirportsList,
            airportListByQuery = airportListByQuery,
            showFiltersSelected = showFiltersSelected,
            mostVisitedSelected = mostVisitedSelected,
            byNameSelected = byNameSelected,
            viewModel = viewModel
        )
    }
}