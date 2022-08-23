package io.eyram.speechsmith.ui.screens.audioToWordMatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.eyram.speechsmith.ui.components.HintRow
import io.eyram.speechsmith.ui.components.OptionButton
import io.eyram.speechsmith.ui.components.PlaySoundButton
import io.eyram.speechsmith.ui.components.SoundControls
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

            HintRow(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .constrainAs(hintRef) {
                        top.linkTo(parent.top, 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            ) {}

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
                    OptionButton(index + 1, body, testQuestion.ans, revealAnswer) {
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

@Preview
@Composable
fun PlayButtonPrev() {
    SpeechsmithTheme {
        PlaySoundButton {}
    }
}

const val PLAY_SOUND = "PLAY SOUND"
const val LABEL_QUESTION = "What did you hear?"

enum class OptionButtonState { Initial, Correct, Incorrect }

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
