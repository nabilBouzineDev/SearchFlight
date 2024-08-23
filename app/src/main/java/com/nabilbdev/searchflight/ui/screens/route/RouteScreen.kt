package com.nabilbdev.searchflight.ui.screens.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    modifier: Modifier = Modifier,
    fromAirport: Airport = AIRPORT_DEFAULT,
    toAirport: Airport = AIRPORT_DEFAULT,
    otherAirports: List<Airport> = listOf(AIRPORT_DEFAULT, AIRPORT_DEFAULT, AIRPORT_DEFAULT)
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { /*TODO*/ },
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                RouteResultSelection(
                    fromAirport = fromAirport,
                    toAirport = toAirport
                )
            }
            item {
                RouteBodySelection(
                    otherAirports = otherAirports
                )
            }
            item {
                FavoriteButtonAddition()
            }
        }
    }
}

@Composable
fun FavoriteButtonAddition(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(
                color = Color.Black,
            )
            .clickable { /*TODO*/ },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.FavoriteBorder,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .padding(8.dp)
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun SelectRouteScreenPreview() {
    MaterialTheme {
        RouteScreen()
    }
}