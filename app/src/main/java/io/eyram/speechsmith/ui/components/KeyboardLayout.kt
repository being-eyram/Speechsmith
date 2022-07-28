package io.eyram.speechsmith.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
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