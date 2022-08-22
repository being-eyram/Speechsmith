package io.eyram.speechsmith.ui.screens.listenandselectpic


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.eyram.speechsmith.ui.components.PrevNextButton
import io.eyram.speechsmith.ui.screens.listenandchoose.PlaySoundButton
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_PREV
import io.eyram.speechsmith.ui.screens.spellingexercise.SpellingExerciseAppBar
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenAndChooseTextScreen() {
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
                    .padding(horizontal = 12.dp)
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
                    modifier = Modifier.padding(horizontal = 12.dp),
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

            LazyVerticalGrid(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .constrainAs(optCardRef) {
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(testQuestion.optionsAsList()) {
                    ImgView()
                }
            }
        }
    }
}


@Preview()
@Composable
fun ListenAndSelectPicPreview() {
    SpeechsmithTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ListenAndChooseTextScreen()
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

@Composable
fun ImgView() {
    Box(
        Modifier
            .size(150.dp)
            .background(Color.Gray)
    ) {}
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
