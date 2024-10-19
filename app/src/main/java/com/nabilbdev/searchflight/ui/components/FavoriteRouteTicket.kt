package com.nabilbdev.searchflight.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.screens.route.CompactAirportSelection
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT

@Composable
fun FavoriteRouteTicket(
    modifier: Modifier = Modifier,
    fromAirport: Airport = AIRPORT_DEFAULT,
    toAirport: Airport = AIRPORT_DEFAULT
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(),
    ) {
        CustomCurvedFlightPath {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                CompactAirportSelection(
                    airport = fromAirport,
                    isFrom = true,
                    modifier = modifier.weight(0.4f)
                )
                CompactAirportSelection(
                    airport = toAirport,
                    modifier = modifier.weight(0.4f)
                )
            }
        }
    }
}