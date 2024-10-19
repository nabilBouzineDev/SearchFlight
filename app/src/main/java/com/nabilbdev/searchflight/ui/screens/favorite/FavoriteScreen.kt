package com.nabilbdev.searchflight.ui.screens.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.FavoriteRouteTicket

@Composable
fun FavoriteScreen(
    favoriteRoutes: Map<Airport, Airport>,
    statusMessage: String?,
    viewModel: FavoriteViewModel,
    modifier: Modifier = Modifier
) {
    when {
        favoriteRoutes.isEmpty() -> {
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = statusMessage ?: "Loading...")
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                items(favoriteRoutes.entries.toList()) { entry ->
                    FavoriteRouteTicket(
                        fromAirport = entry.key,
                        toAirport = entry.value
                    )
                }
            }
        }
    }
}