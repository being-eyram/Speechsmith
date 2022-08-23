package io.eyram.speechsmith.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.screens.audioToWordMatch.OptionButtonState


@Composable
fun OptionButton(
    index: Int,
    body: String,
    answer: String,
    revealAnswer: Boolean,
    onRevealAnswer: () -> Unit
) {

    var state by remember { mutableStateOf(OptionButtonState.Initial) }

    InternalOptionButton(
        optionNumber = index.toString(),
        optionBody = body,
        state = state
    ) {
        state = when (body) {
            answer -> OptionButtonState.Correct
            else -> OptionButtonState.Incorrect
        }
        if (state == OptionButtonState.Incorrect) {
            onRevealAnswer.invoke()
        }
    }

    //this line should show the right card incase a wrong one was pressed.
    if (revealAnswer && (body == answer)) {
        state = OptionButtonState.Correct
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun InternalOptionButton(
    modifier: Modifier = Modifier,
    optionNumber: String,
    optionBody: String,
    state: OptionButtonState,
    onClick: () -> Unit,

    ) {

    val labelColor by animateColorAsState(
        targetValue = when (state) {
            OptionButtonState.Correct -> Color(0xFF538D4E)
            OptionButtonState.Incorrect -> Color(0xFFBF4040)
            else -> Color(0xFF3A3A3C)
        }
    )

    val labelStrokeColor by animateColorAsState(
        targetValue = when (state) {
            OptionButtonState.Initial -> Color.Unspecified
            else -> Color.White
        }
    )

    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            OptionButtonState.Correct -> Color(0xFF538D4E)
            OptionButtonState.Incorrect -> Color(0xFFBF4040)
            else -> Color.White.copy(alpha = 0.05F)
        }
    )

    Button(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .fillMaxWidth(),
        border = BorderStroke(
            width = Dp.Hairline,
            color = when (state) {
                OptionButtonState.Initial -> Color.DarkGray
                else -> Color.Unspecified
            }
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor, Color.White),
        onClick = onClick::invoke
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OptionLabel(
                    label = optionNumber,
                    backgroundColor = labelColor,
                    strokeColor = labelStrokeColor
                )

                Text(
                    text = optionBody,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                    )
                )
            }

            fun showIcon() =
                (state == OptionButtonState.Correct) || (state == OptionButtonState.Incorrect)

            val icon = when (state) {
                OptionButtonState.Correct -> R.drawable.ic_correct
                else -> R.drawable.ic_incorrect
            }

            AnimatedVisibility(
                visible = showIcon(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun OptionLabel(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    strokeColor: Color,
    label: String = ""
) {
    Box(
        modifier = modifier
            .size(24.dp, 32.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(50))
            .border(1.dp, strokeColor, shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}