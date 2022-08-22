package io.eyram.speechsmith.ui.screens.listenandspell

import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eyram.speechsmith.ui.components.Keyboard
import io.eyram.speechsmith.ui.components.SpellField
import io.eyram.speechsmith.ui.screens.listenandchoose.LABEL_QUESTION
import io.eyram.speechsmith.ui.screens.listenandchoose.SoundControls
import io.eyram.speechsmith.ui.screens.spellingexercise.SpellingExerciseAppBar
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenAndSpellScreen(viewModel: ListenAndSpellScreenVM = viewModel()) {
    Scaffold(
        topBar = { SpellingExerciseAppBar(onHomeClick = { /*TODO*/ }) {} }
    ) { padding ->

        ConstraintLayout(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            val (columnRef, keyboardRef) = createRefs()
            val uiState = viewModel.uiState
            val spellFieldState = viewModel.uiState.spellFieldState
            Column(
                modifier = Modifier.constrainAs(columnRef) {
                    bottom.linkTo(keyboardRef.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
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


                SoundControls(
                    modifier = Modifier.padding(end = 24.dp, start = 24.dp, top = 48.dp),
                    onPrevClick = { /*TODO*/ },
                    onPlaySoundClick = { /*TODO*/ }
                ) {}

                Text(
                    modifier = Modifier.paddingFromBaseline(top = 48.dp, bottom = 32.dp),
                    text = LABEL_QUESTION,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        color = Color.White
                    )
                )

                SpellField(
                    Modifier.padding(bottom = 20.dp),
                    spellFieldState = spellFieldState
                )
            }

            Keyboard(
                modifier = Modifier.constrainAs(keyboardRef) {
                    bottom.linkTo(parent.bottom, 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                keyboardLabels = uiState.keyboardLabels,
                onKeyPress = spellFieldState::onKeyPress,
                onBackSpacePress = spellFieldState::onBackSpacePress,
                onEnterPress = { /*TODO*/ }
            )
        }
    }
}

@Preview
@Composable
fun ListenAndSpellPreview() {
    SpeechsmithTheme(darkTheme = true) {
        Surface(){
            ListenAndSpellScreen()
        }

    }
}


