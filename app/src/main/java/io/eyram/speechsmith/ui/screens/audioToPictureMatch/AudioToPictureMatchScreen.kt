package io.eyram.speechsmith.ui.screens.audioToPictureMatch


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.eyram.speechsmith.ui.components.HintRow
import io.eyram.speechsmith.ui.components.PlaySoundButton
import io.eyram.speechsmith.ui.components.SoundControls
import io.eyram.speechsmith.ui.components.SpeechSmithAppBar
import io.eyram.speechsmith.ui.screens.audioSpell.AudioPlayerState
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioToPictureMatchScreen() {
    Scaffold(
        topBar = { SpeechSmithAppBar(onHomeClick = { /*TODO*/ }) {} }
    ) { padding ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val (soundCtrlRef, optCardRef, hintRef) = createRefs()

            HintRow(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .constrainAs(hintRef) {
                        top.linkTo(parent.top, 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                exerciseNumberTracker = "",
                onScoreCardClick = {},
                onHintClick = {}
            )

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
                    onPlaySoundClick = { /*TODO*/ },
                    audioPlayerState = AudioPlayerState.Idle,
                    onNextClick = { }
                )

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
            AudioToPictureMatchScreen()
        }
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
        //PlaySoundButton {}
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
