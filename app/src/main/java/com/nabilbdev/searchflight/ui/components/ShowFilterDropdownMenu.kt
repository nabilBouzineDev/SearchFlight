package com.nabilbdev.searchflight.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.R

@Composable
fun ShowFilterDropdownMenu(
    showFiltersSelected: Boolean,
    onShowFilters: () -> Unit = {}
) {

    /**
     * A state to control the drop down Menu visibility
     */
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        Icon(
            painter = painterResource(id = R.drawable.filter_list),
            contentDescription = "Sort",
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp)
        )

        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            DropdownMenuItem(
                text = {
                    if (showFiltersSelected)
                        Text(text = "Show Filters")
                    else
                        Text(text = "Hide Filters")

                },
                onClick = {
                    isExpanded = false
                    onShowFilters()
                },
                leadingIcon = {
                    if (showFiltersSelected)
                        Icon(
                            painter = painterResource(id = R.drawable.show_filters),
                            contentDescription = null
                        )
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.hide_filters),
                            contentDescription = null
                        )
                }
            )
        }
    }

}