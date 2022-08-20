package io.eyram.speechsmith.ui.screens.listenandchoose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.constraintlayout.compose.ConstraintLayout
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.PrevNextButton
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_PREV
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


val items = listOf("Apples", "Pear", "Avocadoes", "Bananas")

@Composable
fun ListenAndChooseTextScreen() {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (soundCtrlRef, optCardRef, instRef) = createRefs()

        Column(
            modifier = Modifier.constrainAs(optCardRef) {
                bottom.linkTo(soundCtrlRef.top, margin = 40.dp)
                start.linkTo(soundCtrlRef.start)
                end.linkTo(soundCtrlRef.end)
            }
        ) {
            val optionsList = testQuestion.optionsAsList()

            optionsList.forEachIndexed { index, body ->
                var state by remember { mutableStateOf(OptionButtonState.Initial) }
                OptionCard(
                    optionNumber = index.toString(),
                    optionBody = body,
                    state = state
                ) {
                    state =
                        if (body == testQuestion.ans) OptionButtonState.Correct else OptionButtonState.Incorrect
                }
            }
        }

        SoundControls(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .constrainAs(soundCtrlRef) {
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onPrevClick = { /*TODO*/ },
            onPlaySoundClick = { /*TODO*/ }) {
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ListenNChoosePreview() {
    SpeechsmithTheme {
        ListenAndChooseTextScreen()
    }
}

@Composable
fun SoundControls(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onPlaySoundClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PrevNextButton(label = LABEL_PREV, onClick = { /*TODO*/ }, enabled = true)
        PlaySoundButton {}
        PrevNextButton(label = LABEL_NEXT, onClick = { /*TODO*/ }, enabled = true)
    }
}

@Preview
@Composable
fun BottomControlsPreview() {
    SpeechsmithTheme {
        SoundControls(onPrevClick = { /*TODO*/ }, onPlaySoundClick = { /*TODO*/ }) {

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
            .height(64.dp)
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
                    label = optionNumber,
                    backgroundColor = labelColor,
                    strokeColor = labelStrokeColor
                )

                Text(
                    text = optionBody,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
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

data class Question(
    val optA: String,
    val optB: String,
    val optC: String,
    val optD: String,
    val ans: String
) {
    fun optionsAsList() = listOf(optA, optB, optC, optD)
}

val testQuestion = Question(
    "Apples", "Pears", "Mangoes",
    "Pineapples", "Apples"
)
