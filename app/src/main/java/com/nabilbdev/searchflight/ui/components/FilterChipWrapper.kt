package com.nabilbdev.searchflight.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilterChipWrapper(
    selected: Boolean = false,
    onSelectChip: () -> Unit = {},
    label: String = ""
) {
    FilterChip(
        selected = selected,
        onClick = onSelectChip,
        label = { Text(text = label) },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(
                        FilterChipDefaults.IconSize
                    )
                )
            }
        } else {
            null
        }
    )
}