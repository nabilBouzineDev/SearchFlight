package com.nabilbdev.searchflight.ui.screens.favorite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.FavoriteRouteTicket
import com.nabilbdev.searchflight.utils.DragTarget
import com.nabilbdev.searchflight.utils.DropItem

@Composable
fun FavoriteScreen(
    favoriteRoutes: List<Pair<Airport, Airport>>,
    deletedOrNoFavoriteStatusMessage: String?,
    viewModel: FavoriteViewModel,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    when {
        favoriteRoutes.isEmpty() -> {
            Column(
                modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.5f)
                )
                deletedOrNoFavoriteStatusMessage?.let {
                    Text(text = it)
                }
            }
        }

        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Your Favorite Routes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp,
                        bottom = 4.dp
                    )
                )
                LazyRow {
                    items(favoriteRoutes) { favoriteRouteItem ->
                        DragTarget(
                            dataToDrop = favoriteRouteItem,
                            viewModel = viewModel
                        ) {
                            FavoriteRouteTicket(
                                fromAirport = favoriteRouteItem.first,
                                toAirport = favoriteRouteItem.second,
                                modifier = Modifier
                                    .width(325.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BinDropItem(
                    viewModel = viewModel,
                    statusMessage = deletedOrNoFavoriteStatusMessage,
                    snackBarHostState = snackBarHostState
                )
            }
        }
    }
}

@Composable
fun BinDropItem(
    viewModel: FavoriteViewModel,
    statusMessage: String?,
    snackBarHostState: SnackbarHostState
) {

    LaunchedEffect(key1 = statusMessage) {
        statusMessage?.let {
            snackBarHostState.showSnackbar(statusMessage)
            viewModel.clearStatusMessage()
        }
    }

    AnimatedVisibility(
        visible = viewModel.isCurrentlyDragging,
        enter = slideInVertically(initialOffsetY = { it })
    ) {
        DropItem<Pair<Airport, Airport>>(
            modifier = Modifier.size(50.dp)
        ) { isInBound, favoriteRouteItem ->
            if (isInBound && favoriteRouteItem != null) {
                LaunchedEffect(key1 = favoriteRouteItem) {
                    viewModel.deleteFromFavorites(favoriteRouteItem)
                }
            }
            if (isInBound) {
                BinIconDropItem(iconColor = Color.Red)
            } else {
                BinIconDropItem(iconColor = Color.DarkGray)
            }
        }
    }
}

@Composable
fun BinIconDropItem(
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                iconColor.copy(0.3f), MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete a favorite route!",
            tint = iconColor,
        )
    }
}