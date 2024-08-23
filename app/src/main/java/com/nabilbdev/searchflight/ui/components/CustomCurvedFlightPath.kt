package com.nabilbdev.searchflight.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect.Companion.dashPathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomCurvedFlightPath(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.TopCenter),
        ) {
            val startX = size.width * 0.25f
            val endX = size.width * 0.75f
            val startY = size.height * 0.4f
            val endY = size.height * 0.4f
            val controlPointY = 0f // Control point for the curve's height

            val path = Path().apply {
                moveTo(startX, startY)
                quadraticBezierTo(
                    size.width * 0.5f,
                    controlPointY, // Control point
                    endX,
                    endY // End point
                )
            }

            drawPath(
                path = path,
                color = Black,
                style = Stroke(
                    width = 7f,
                    pathEffect = dashPathEffect(
                        floatArrayOf(
                            25f,
                            25f
                        )
                    )
                )
            )
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCurvedFlightPathPreview() {
    MaterialTheme {
        CustomCurvedFlightPath()
    }
}