package com.nabilbdev.searchflight.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.CommonAirportVerticalGrid

@Composable
fun HomeScreen(
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