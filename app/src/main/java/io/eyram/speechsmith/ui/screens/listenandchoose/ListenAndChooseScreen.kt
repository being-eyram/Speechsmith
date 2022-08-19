package io.eyram.speechsmith.ui.screens.listenandchoose

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.PrevNextButton
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_PREV
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@Composable
fun ListenAndChooseTextScreen() {

}

@Preview
@Composable
fun ListenNChoosePreview() {
    SpeechsmithTheme {

    }
}

@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onPlaySoundClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(132.dp)
            .fillMaxWidth()
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PrevNextButton(label = LABEL_PREV, onClick = { /*TODO*/ }, enabled = true)
            PlaySoundButton {}
            PrevNextButton(label = LABEL_NEXT, onClick = { /*TODO*/ }, enabled = true)
        }
    }
}

@Preview
@Composable
fun BottomControlsPreview() {
    SpeechsmithTheme {
        BottomControls(onPrevClick = { /*TODO*/ }, onPlaySoundClick = { /*TODO*/ }) {

        }
    }
}

@Composable
fun PlaySoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.size(184.dp, 56.dp),
        onClick = onClick::invoke,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.05F))
    ) {
        Icon(
            modifier = Modifier.padding(end = 12.dp),
            painter = painterResource(id = R.drawable.ic_play_circle),
            contentDescription = null
        )
        Text(
            text = PLAY_SOUND,
            style = MaterialTheme.typography.bodyLarge
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OptionCard(
    modifier: Modifier = Modifier,
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
            Dp.Hairline,
            color = if (state == OptionButtonState.Initial) Color.DarkGray else Color.Unspecified
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
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
                    label = "1",
                    backgroundColor = labelColor,
                    strokeColor = labelStrokeColor
                )

                Text(
                    text = optionBody,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
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

@Preview
@Composable
fun OptionCardPreview() {
    SpeechsmithTheme {
        var index by remember { mutableStateOf(0) }
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 32.dp)
        ) {
            Spacer(Modifier.width(16.dp))
            OptionCard(
                optionBody = "Apples",
                state = OptionButtonState.values()[index]
            ) {
                index = (index + 1) % 3
            }
            Spacer(Modifier.width(16.dp))
        }

    }
}

@Preview
@Composable
fun PlayButtonPrev() {
    SpeechsmithTheme {
        PlaySoundButton {

        }
    }
}

const val PLAY_SOUND = "PLAY SOUND"

enum class OptionButtonState {
    Initial,
    Correct,
    Incorrect
}