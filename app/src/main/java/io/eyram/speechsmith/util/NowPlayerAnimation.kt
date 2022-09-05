package io.eyram.speechsmith.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NowPlayingBar(modifier: Modifier = Modifier) {
    val canvasSizePx = with(LocalDensity.current) { 20.dp.toPx() }
    val infiniteScale = rememberInfiniteTransition()

    val animatedDotScale = listOf(
        infiniteScale.animateFloat(
            initialValue = 0f,
            targetValue = canvasSizePx,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 475
                    canvasSizePx * 0.1f at 45 with FastOutLinearInEasing // for 15-75 ms
                    canvasSizePx * 0.4f at 125 with FastOutLinearInEasing // ms
                    canvasSizePx * 0.7f at 225 // ms
                    canvasSizePx * 0.4f at 345
                    canvasSizePx * 0.8f at 385
                    canvasSizePx at 425
                },
                repeatMode = RepeatMode.Reverse
            )
        ),
        infiniteScale.animateFloat(
            initialValue = 0f,
            targetValue = canvasSizePx / 3,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 475
                    canvasSizePx / 3 * 0.3f at 45 with FastOutSlowInEasing// for 15-75 ms
                    canvasSizePx / 3 * 0.7f at 325 with FastOutLinearInEasing // ms
                },
                repeatMode = RepeatMode.Reverse
            )
        ),
        infiniteScale.animateFloat(
            initialValue = 0f,
            targetValue = canvasSizePx,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 475
                    canvasSizePx * 0.1f at 45 with FastOutLinearInEasing // for 15-75 ms
                    canvasSizePx * 0.5f at 325 with FastOutLinearInEasing // ms
                },
                repeatMode = RepeatMode.Reverse
            )
        ),
        infiniteScale.animateFloat(
            initialValue = 0f,
            targetValue = canvasSizePx,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 475
                    canvasSizePx * 0.8f at 125 with FastOutLinearInEasing // for 15-75 ms
                    canvasSizePx * 0.5f at 225 with FastOutLinearInEasing // ms
                    canvasSizePx * 0.3f at 425
                },
                repeatMode = RepeatMode.Reverse
            )
        )
    )

    Canvas(modifier.size(20.dp)) {

        val xSpacing = size.width / 20F
        val barWidth = (size.width - (xSpacing * 5)) / 4f
        var xSpaceOccupied = xSpacing
        // val barHeight = size.height

        repeat(4) {
            drawRect(
                color = Color(0xFF42C736),
                size = Size(barWidth, size.height),
                topLeft = Offset(x = xSpaceOccupied, y = animatedDotScale[it].value)
            )
            xSpaceOccupied += barWidth + xSpacing
        }
    }
}


@Preview
@Composable
fun NowPlayingBarPreview() {
    NowPlayingBar()
}