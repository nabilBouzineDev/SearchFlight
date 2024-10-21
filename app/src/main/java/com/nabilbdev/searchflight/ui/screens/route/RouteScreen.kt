package com.nabilbdev.searchflight.ui.screens.route

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.entity.Favorite
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT
import com.nabilbdev.searchflight.utils.FavoriteStatus
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    favoriteStatus: FavoriteStatus,
    modifier: Modifier = Modifier,
    fromAirport: Airport = AIRPORT_DEFAULT,
    toAirport: Airport = AIRPORT_DEFAULT,
    otherAirports: List<Airport> = listOf(AIRPORT_DEFAULT, AIRPORT_DEFAULT, AIRPORT_DEFAULT),
    isFavButtonDisabled: Boolean,
    clearMessageAndButtonSelection: () -> Unit,
    onSaveToFavoriteClicked: (Favorite) -> Unit = {},
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
                    favoriteStatus = favoriteStatus,
                    isFavButtonDisabled = isFavButtonDisabled,
                    clearMessageAndButtonSelection = clearMessageAndButtonSelection,
                    onSaveToFavoriteClicked = {
                        onSaveToFavoriteClicked(
                            Favorite(
                                departureCode = fromAirport.iataCode,
                                destinationCode = toAirport.iataCode
                            )
                        )
                    },
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
    favoriteStatus: FavoriteStatus,
    clearMessageAndButtonSelection: () -> Unit,
    onSaveToFavoriteClicked: () -> Unit = {}
) {

    AnimatedContent(
        targetState = favoriteStatus,
        transitionSpec = {
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        label = ""
    ) { targetState: FavoriteStatus ->

        when (targetState) {
            FavoriteStatus.ADDED -> {
                SuccessAddedFavoriteButton(
                    isFavButtonDisabled = isFavButtonDisabled,
                    message = "ADDED TO FAVORITES",
                    onSaveToFavoriteClicked = onSaveToFavoriteClicked
                )
            }

            FavoriteStatus.DUPLICATED -> {
                FailedDuplicatedFavoriteButton(
                    isFavButtonDisabled = isFavButtonDisabled,
                    message = "FAVORITE ALREADY SAVED",
                    onSaveToFavoriteClicked = onSaveToFavoriteClicked
                )
            }

            FavoriteStatus.NONE -> {
                NormalFavoriteButton(
                    isFavButtonDisabled = isFavButtonDisabled,
                    message = "SAVE TO FAVORITES",
                    onSaveToFavoriteClicked = onSaveToFavoriteClicked
                )
            }
        }
    }

    LaunchedEffect(key1 = favoriteStatus) {
        if (favoriteStatus != FavoriteStatus.NONE) {
            delay(1500)
            clearMessageAndButtonSelection()
        }
    }
}

@Composable
fun FailedDuplicatedFavoriteButton(
    isFavButtonDisabled: Boolean,
    message: String,
    onSaveToFavoriteClicked: () -> Unit = {}
) {
    Button(
        onClick = onSaveToFavoriteClicked,
        enabled = !isFavButtonDisabled,
        colors = ButtonColors(
            containerColor = Color.Red,
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.LightGray
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = message,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun SuccessAddedFavoriteButton(
    isFavButtonDisabled: Boolean,
    message: String,
    onSaveToFavoriteClicked: () -> Unit = {}
) {
    Button(
        onClick = onSaveToFavoriteClicked,
        enabled = !isFavButtonDisabled,
        colors = ButtonColors(
            containerColor = Color.Green,
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.LightGray
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = message,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun NormalFavoriteButton(
    isFavButtonDisabled: Boolean,
    message: String,
    onSaveToFavoriteClicked: () -> Unit = {}
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
            text = message,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}