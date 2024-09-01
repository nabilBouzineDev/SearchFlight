package com.nabilbdev.searchflight.ui.screens.route

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    otherAirports: List<Airport> = listOf(AIRPORT_DEFAULT, AIRPORT_DEFAULT, AIRPORT_DEFAULT),
    isFavButtonDisabled: Boolean,
    saveToFavoriteSelected: Boolean,
    onSaveToFavoriteClicked: () -> Unit = {},
    onHideBottomSheet: () -> Unit = {},
    onArrivalAirportSelected: (Airport) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onHideBottomSheet,
        shape = BottomSheetDefaults.ExpandedShape,
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
                FavoriteButtonAddition(
                    isFavButtonDisabled = isFavButtonDisabled,
                    saveToFavoriteSelected = saveToFavoriteSelected,
                    onSaveToFavoriteClicked = onSaveToFavoriteClicked,
                )
            }
            item {
                RouteBodySelection(
                    otherAirports = otherAirports,
                    onArrivalAirportSelected = onArrivalAirportSelected
                )
            }
        }
    }
}

@Composable
fun FavoriteButtonAddition(
    isFavButtonDisabled: Boolean,
    saveToFavoriteSelected: Boolean,
    modifier: Modifier = Modifier,
    onSaveToFavoriteClicked: () -> Unit = {}
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onSaveToFavoriteClicked,
            enabled = !isFavButtonDisabled,
            colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.LightGray
            )
        ) {
            Text(
                text = "SAVE TO FAVORITES",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        AnimatedVisibility(visible = saveToFavoriteSelected, exit = fadeOut()) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                LinearProgressIndicator(
                    color = Color.Black,
                    trackColor = Color.LightGray,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteButtonAdditionPreview() {
    MaterialTheme {
        FavoriteButtonAddition(
            isFavButtonDisabled = true,
            saveToFavoriteSelected = false
        )
    }
}