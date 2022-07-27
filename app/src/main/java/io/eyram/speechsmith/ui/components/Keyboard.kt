package io.eyram.speechsmith.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import kotlin.math.roundToInt

@Composable
fun KeyboardLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val keyMinHeight = 56.dp.roundToPx()
        val keyMinWidth = 48.dp.roundToPx()
        val enterKeyMinWidth = 80.dp.roundToPx()
        val backspaceKeyMinWidth = 160.dp.roundToPx()

        //constraints
        val keyConstraints = constraints.copy(
            minWidth = keyMinWidth,
            minHeight = keyMinHeight,
            maxHeight = constraints.maxHeight / 3,
            maxWidth = constraints.maxWidth / 7
        )
        val enterKeyConstraints = keyConstraints.copy(
            minWidth = enterKeyMinWidth,
            maxWidth = (constraints.maxWidth * 0.238095).roundToInt()
        )
        val backspaceConstraints = keyConstraints.copy(
            minWidth = backspaceKeyMinWidth,
            maxWidth = (constraints.maxWidth * 0.476190).roundToInt()
        )

        val alphaKeysMeasurables = measurables.filter {
            it.layoutId.toString().startsWith("key")
        }

        val enterKeyMeasurable = measurables.find { it.layoutId == ENTER_KEY_ID }
        val backspaceKeyMeasurable = measurables.find { it.layoutId == BACKSPACE_KEY_ID }

        val alphaKeysPlaceable = alphaKeysMeasurables.map { it.measure(keyConstraints) }
        val enterKeyPlaceable = enterKeyMeasurable?.measure(enterKeyConstraints)
        val backspaceKeyPlaceable = backspaceKeyMeasurable?.measure(backspaceConstraints)

        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0
            var xPosition = 0

            // Place children in the parent layout
            alphaKeysPlaceable.forEachIndexed { index, placeable ->

                placeable.placeRelative(x = xPosition, y = yPosition)
                xPosition += placeable.width
                //When you get to the 7th key move to the next row and offset the keys.
                // Do same when you get to 13th key
                when (index) {
                    6 -> {
                        xPosition = placeable.width / 2
                        yPosition += placeable.height
                    }
                    12 -> {
                        xPosition = enterKeyPlaceable?.width!!
                        yPosition = placeable.height * 2
                    }
                }
            }

            enterKeyPlaceable?.placeRelative(0, yPosition)
            backspaceKeyPlaceable?.placeRelative(xPosition, yPosition)
        }
    }
}

const val ENTER_KEY_ID = "Enter_Key"
const val BACKSPACE_KEY_ID = "Backspace_Key"


@Preview(device = Devices.NEXUS_5X)
@Preview(device = Devices.NEXUS_5)
@Preview(device = Devices.PIXEL_2_XL)

@Composable
fun DefaultPreview() {
    SpeechsmithTheme {
        Keyboard()
    }
}


@Composable
fun SpellBox(
    modifier: Modifier = Modifier,
    text: String,
    isNext: Boolean = false,
    state: SpellCheckState
) {
    val borderColor = remember { Animatable(Color(0xFFA4A4A4)) }
    val backgroundColor = remember { Animatable(Color.Transparent) }

    Box(
        modifier = modifier
            .padding(horizontal = 1.dp)
            .background(color = backgroundColor.value)
            .size(width = 32.dp, height = 40.dp)
            .border(
                width = 1.dp,
                color = borderColor.value
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
    LaunchedEffect(isNext, state) {
        borderColor.animateTo(
            targetValue = if (isNext) Color(0xFFE0991A) else Color(0xFFA4A4A4),
            animationSpec = tween(
                durationMillis = 350,
                easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
            )
        )
        backgroundColor.animateTo(
            targetValue = when (state) {
                is Matched -> Color(0xFF538D4E)
                is Unmatched -> Color(0xFF3A3A3C)
                else -> Color.Unspecified
            },
            animationSpec = tween(
                easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
            )
        )
    }
}