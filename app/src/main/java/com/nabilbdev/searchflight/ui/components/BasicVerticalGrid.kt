package com.nabilbdev.searchflight.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport

@Composable
fun CommonAirportVerticalGrid(
    titleContent: String,
    airportList: List<Airport>,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    isLoadingAirports: Boolean = false
) {
    AnimatedVisibility(visible = isLoadingAirports, exit = fadeOut()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LinearProgressIndicator()
        }
    }
    AnimatedVisibility(visible = !isLoadingAirports, enter = fadeIn()) {
        CustomVerticalGrid(
            titleContent = titleContent,
            airportList = airportList,
            imageVector = imageVector
        )
    }
}

/**
 * Create a custom grid layout using a combination of Row and Column
 */
@Composable
fun CustomVerticalGrid(
    titleContent: String,
    airportList: List<Airport>,
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = titleContent, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = modifier.height(8.dp))
        airportList.chunked(2).forEach { halfList ->
            Row(modifier = modifier.fillMaxWidth()) {
                halfList.forEach { airport ->
                    AirportCard(
                        airport = airport,
                        imageVector = imageVector,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}