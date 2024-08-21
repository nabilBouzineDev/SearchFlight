package com.nabilbdev.searchflight.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.R
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.ui.components.CustomCircularChart
import com.nabilbdev.searchflight.ui.screens.search.utils.AIRPORT_DEFAULT
import com.nabilbdev.searchflight.ui.theme.SearchFlightTheme


@Composable
fun SearchResultContent(
    airport: Airport,
    passengerNumber: String,
    passengerPercentage: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        shape = CardDefaults.elevatedShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.6f),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.flight_from),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "From",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    text = airport.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f),
                contentAlignment = Alignment.TopEnd
            ) {
                CustomCircularChart(indicatorValuePercentage = passengerPercentage) {
                    Text(
                        text = "Passengers",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                    Text(
                        text = passengerNumber,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SearchResultContentPreview() {
    SearchFlightTheme {
        SearchResultContent(
            airport = AIRPORT_DEFAULT,
            passengerPercentage = 9,
            passengerNumber = "1.4M"
        )
    }
}
