package com.nabilbdev.searchflight.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nabilbdev.searchflight.ui.theme.SearchFlightTheme

@Composable
fun CustomCircularChart(
    canvasSize: Dp = 128.dp,
    indicatorValuePercentage: Int = 0, // by default is the passengerPercentage
    bgIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    bgIndicatorStrokeWidth: Float = 15f,
    normalFgIndicatorColor: Color = MaterialTheme.colorScheme.primary.copy(blue = 1f),
    errorFgIndicatorColor: Color = MaterialTheme.colorScheme.error,
    successFgIndicatorColor: Color = MaterialTheme.colorScheme.primary.copy(green = 0.87f),
    content: @Composable () -> Unit = {}
) {

    /**
     * This `animatedIndicatorValuePercentage`:
     *      Recompose whenever `indicatorValuePercentage` is changed
     */
    var animatedIndicatorValuePercentage by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(key1 = indicatorValuePercentage) {
        animatedIndicatorValuePercentage = (indicatorValuePercentage.toFloat())
    }

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * animatedIndicatorValuePercentage).toFloat(),
        animationSpec = tween(1000),
        label = "",
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                // canvasSize divided by 1.25f (similar to setting a padding)
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = bgIndicatorColor,
                    indicatorStrokeWidth = bgIndicatorStrokeWidth,
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = when (indicatorValuePercentage) {
                        in 0..25 -> errorFgIndicatorColor
                        in 26..50 -> normalFgIndicatorColor
                        else -> successFgIndicatorColor
                    },
                    indicatorStrokeWidth = bgIndicatorStrokeWidth,
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2,
            y = (size.height - componentSize.height) / 2,
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2,
            y = (size.height - componentSize.height) / 2,
        )
    )
}


@Preview(showBackground = true)
@Composable
fun CustomCircularChartPreview() {
    SearchFlightTheme {
        CustomCircularChart()
    }
}


