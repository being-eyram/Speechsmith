package io.eyram.speechsmith.ui.screens.audioToWordMatch

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
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_PREV
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellingExerciseAppBar
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioToWordMatchScreen() {
    Scaffold(
        topBar = { SpellingExerciseAppBar(onHomeClick = { /*TODO*/ }) {} }
    ) { padding ->


        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val (soundCtrlRef, optCardRef, hintRef) = createRefs()

            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .constrainAs(hintRef) {
                        top.linkTo(parent.top, 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.size(84.dp, 40.dp),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {}
                ) {
                    Text(text = "4 of 10")
                }
                Button(
                    modifier = Modifier.size(84.dp, 40.dp),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {}
                ) {
                    Text(text = "Hint")
                }
            }

            Column(
                modifier = Modifier.constrainAs(soundCtrlRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(hintRef.bottom)
                    bottom.linkTo(optCardRef.top)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SoundControls(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onPrevClick = { /*TODO*/ },
                    onPlaySoundClick = { /*TODO*/ }
                ) {}

                Text(
                    modifier = Modifier.paddingFromBaseline(top = 40.dp),
                    text = LABEL_QUESTION,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        color = Color.White
                    )
                )
            }

            Column(
                modifier = Modifier.constrainAs(optCardRef) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                val optionsList = testQuestion.optionsAsList()
                var revealAnswer by remember { mutableStateOf(false) }
                val coroutineScope = rememberCoroutineScope()
                optionsList.forEachIndexed { index, body ->
                    OptionCard(index + 1, body, testQuestion.ans, revealAnswer) {
                        coroutineScope.launch {
                            delay(650)
                            revealAnswer = !revealAnswer
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

        }
    }
}


@Preview()
@Composable
fun ListenNChoosePreview() {
    SpeechsmithTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AudioToWordMatchScreen()
        }
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

@Composable
private fun OptionCard(
    index: Int,
    body: String,
    answer: String,
    revealAnswer: Boolean,
    onRevealAnswer: () -> Unit
) {

    var state by remember { mutableStateOf(OptionButtonState.Initial) }

    OptionCard(
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

@Preview
@Composable
fun PlayButtonPrev() {
    SpeechsmithTheme {
        PlaySoundButton {
        }
    }
}

const val PLAY_SOUND = "PLAY SOUND"
const val LABEL_QUESTION = "What did you hear?"

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
