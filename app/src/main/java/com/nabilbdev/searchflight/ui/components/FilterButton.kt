package com.nabilbdev.searchflight.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.R

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    onFilter: () -> Unit = {}
) {
    IconButton(onClick = onFilter) {
        Icon(
            painter = painterResource(id = R.drawable.filter_list), contentDescription = null,
            modifier = modifier.size(50.dp)
        )
    }
}