package com.nabilbdev.searchflight.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        CommonAirportVerticalGrid(
            airportList = popularCitiesAirports,
            isLoadingAirports = isLoadingAirports,
            imageVector = Icons.Outlined.Search
        )
    }
}