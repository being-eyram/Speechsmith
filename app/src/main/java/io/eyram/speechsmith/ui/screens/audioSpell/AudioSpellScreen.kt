package io.eyram.speechsmith.ui.screens.audioSpell

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
import io.eyram.speechsmith.ui.screens.audioToWordMatch.LABEL_QUESTION
import io.eyram.speechsmith.ui.screens.audioToWordMatch.SoundControls
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellingExerciseAppBar
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSpellScreen(viewModel: AudioSpellViewModel = viewModel()) {
    Scaffold(
        topBar = { SpellingExerciseAppBar(onHomeClick = { /*TODO*/ }) {} }
    ) { padding ->

        ConstraintLayout(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val (columnRef, hintRef, keyboardRef) = createRefs()
            val uiState = viewModel.uiState
            val spellFieldState = viewModel.uiState.spellFieldState

            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .constrainAs(hintRef) {
                        top.linkTo(parent.top, 12.dp)
                        start.linkTo(columnRef.start)
                        end.linkTo(columnRef.end)
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
                modifier = Modifier.constrainAs(columnRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(hintRef.bottom)
                    bottom.linkTo(keyboardRef.top)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SoundControls(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onPrevClick = { /*TODO*/ },
                    onPlaySoundClick = { /*TODO*/ }
                ) {}

                Text(
                    modifier = Modifier.paddingFromBaseline(top = 48.dp, bottom = 16.dp),
                    text = LABEL_QUESTION,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        color = Color.White
                    )
                )

                SpellField(spellFieldState = spellFieldState)
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
        Surface() {
            AudioSpellScreen()
        }

    }
}


