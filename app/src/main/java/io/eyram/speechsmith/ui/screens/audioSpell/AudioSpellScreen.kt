package io.eyram.speechsmith.ui.screens.audioSpell

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eyram.speechsmith.ui.components.*
import io.eyram.speechsmith.ui.screens.audioToWordMatch.LABEL_QUESTION
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSpellScreen(
    viewModel: AudioSpellViewModel = viewModel(),
    onHomeClick: () -> Unit,
) {
    val uiState = viewModel.uiState
    val spellFieldState = viewModel.uiState.spellFieldState

    Scaffold(
        topBar = {
            SpeechSmithAppBar(
                onHomeClick = onHomeClick::invoke,
                onSettingsClick = {}
            )
        }
    ) { padding ->
        AudioSpellContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            spellFieldState = spellFieldState,
            onHintClick = {},
            onPrevClick = {},
            onNextClick = {},
            onEnterPress = viewModel::onEnterPress,
            onPlaySoundClick = {}
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AudioSpellContent(
    modifier: Modifier = Modifier,
    uiState: AudioSpellScreenState,
    spellFieldState: SpellFieldState,
    onHintClick: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onEnterPress: () -> Unit,
    onPlaySoundClick: () -> Unit
) {

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (columnRef, hintRef, keyboardRef, indicationRef) = createRefs()

        HintRow(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .constrainAs(hintRef) {
                    top.linkTo(parent.top, 12.dp)
                    start.linkTo(columnRef.start)
                    end.linkTo(columnRef.end)
                },
            onHintClick = onHintClick::invoke
        )

        AnimatedVisibility(
            modifier = Modifier.constrainAs(indicationRef) {
                top.linkTo(parent.top, 12.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            visible = uiState.showFieldStateIndicator,
            enter = slideInVertically() + fadeIn(initialAlpha = 0.3f),
            exit = scaleOut() + fadeOut()
        ) {
            val visualIndicatorState = uiState.visualIndicatorState

            Card(
                modifier = Modifier.size(240.dp, 40.dp),
                colors = CardDefaults.cardColors(visualIndicatorState.color)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = visualIndicatorState.message
                    )

                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(id = visualIndicatorState.icon),
                        contentDescription = null
                    )
                }
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
                modifier = Modifier.padding(horizontal = 24.dp),
                onPrevClick = onPrevClick::invoke,
                onNextClick = onNextClick::invoke,
                onPlaySoundClick = onPlaySoundClick::invoke,
            )

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
            onEnterPress = onEnterPress::invoke
        )
    }
}

@Preview
@Composable
fun ListenAndSpellPreview() {
    SpeechsmithTheme(darkTheme = true) {
        Surface() {
            AudioSpellScreen(onHomeClick = {})
        }
    }
}


