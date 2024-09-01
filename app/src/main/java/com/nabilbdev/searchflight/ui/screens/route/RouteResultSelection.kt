package com.nabilbdev.searchflight.ui.screens.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.R
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.CustomCurvedFlightPath
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT

@Composable
fun RouteResultSelection(
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

@Composable
fun CompactAirportSelection(
    airport: Airport,
    modifier: Modifier = Modifier,
    isFrom: Boolean = false
) {
    Column(
        modifier = modifier.padding(start = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = when (isFrom) {
            true -> Alignment.Start
            false -> Alignment.End
        }
    ) {
        Icon(
            painter = when (isFrom) {
                true -> painterResource(id = R.drawable.flight_from)
                false -> painterResource(id = R.drawable.flight_to)
            },
            contentDescription = null
        )
        Text(
            text = airport.iataCode,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = when (isFrom) {
                true -> TextAlign.Start
                false -> TextAlign.End
            }
        )
        Text(
            text = airport.name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelSmall,
            textAlign = when (isFrom) {
                true -> TextAlign.Start
                false -> TextAlign.End
            }
        )
    }
}