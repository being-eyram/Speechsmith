package io.eyram.speechsmith.ui.screens.audioSpell

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import io.eyram.speechsmith.R
import io.eyram.speechsmith.data.model.AppSettings
import io.eyram.speechsmith.data.model.DIFFICULTY_EASY
import io.eyram.speechsmith.data.model.DIFFICULTY_HARD
import io.eyram.speechsmith.data.model.DIFFICULTY_MEDIUM
import io.eyram.speechsmith.ui.components.*
import io.eyram.speechsmith.ui.screens.audioToWordMatch.LABEL_QUESTION
import io.eyram.speechsmith.ui.screens.pictureSpell.CORRECT
import io.eyram.speechsmith.ui.screens.pictureSpell.WRONG
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AudioSpellScreen(
    viewModel: AudioSpellViewModel = viewModel(),
    context: Context = LocalContext.current,
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    onHomeClick: () -> Unit,
) {
    val uiState = viewModel.uiState
    val spellFieldState = viewModel.uiState.spellFieldState
    val player = remember { ExoPlayer.Builder(context).build() }
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Black,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetState = bottomSheetState,
        sheetContent = {

            AudioSpellBottomSheetContent(
                difficulty = uiState.exerciseDifficulty,
                onDifficultyDropDownClick = viewModel::onDifficultyDropDownClick,
                totalNumberOfQuestions = uiState.totalNumberOfQuestions,
                onAddQuestionsClick = viewModel::onAddQuestionsClick,
                onSubtractQuestionsClick = viewModel::onSubtractQuestionsClick,
                wordGroup = uiState.exerciseWordGroup,
                onWordGroupDropDownClick = viewModel::onWordGroupDropDownClick
            )
        }
    ) {
        Scaffold(
            topBar = {
                SpeechSmithAppBar(
                    onHomeClick = onHomeClick::invoke,
                    onSettingsClick = {
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    }
                )
            }
        ) { padding ->
            AudioSpellContent(
                modifier = Modifier.padding(padding),
                uiState = uiState,
                spellFieldState = spellFieldState,
                onHintClick = {},
                onScoreClick = {},
                onPrevClick = viewModel::onPrevPress,
                onNextClick = viewModel::onNextPress,
                onEnterPress = {
                    viewModel.onEnterPress()
                    if (uiState.visualIndicatorState.message == CORRECT) {
                        player.setMediaItem(
                            MediaItem.fromUri(
                                RawResourceDataSource.buildRawResourceUri(R.raw.right_ans_audio)
                            )
                        )

                        player.play()
                    }
                    if (uiState.visualIndicatorState.message == WRONG) {
                        player.setMediaItem(
                            MediaItem.fromUri(
                                RawResourceDataSource.buildRawResourceUri(R.raw.wrong_ans_audio)
                            )
                        )
                        player.play()
                    }
                },
                score = "${uiState.currentExerciseNumber + 1} OF 10",
                onPlaySoundClick = {
                    player.apply {
                        setMediaItem(MediaItem.fromUri(uiState.audioUrl))
                        play()
                    }
                }
            )
        }

        DisposableEffect(Unit) {
            player.apply {

                volume = 1F
                prepare()
            }

            onDispose {
                player.release()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AudioSpellContent(
    modifier: Modifier = Modifier,
    uiState: AudioSpellScreenState,
    spellFieldState: SpellFieldState,
    score: String,
    onHintClick: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onEnterPress: () -> Unit,
    onScoreClick: () -> Unit,
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
            score = score,
            onHintClick = onHintClick::invoke,
            onScoreCardClick = onScoreClick::invoke
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


@Composable
fun ColumnScope.AudioSpellBottomSheetContent(
    difficulty: String,
    onDifficultyDropDownClick: (String) -> Unit,
    wordGroup: String,
    onWordGroupDropDownClick: (String) -> Unit,
    totalNumberOfQuestions: Int,
    onAddQuestionsClick: () -> Unit,
    onSubtractQuestionsClick: () -> Unit
) {
    DragIndicator(Modifier.align(Alignment.CenterHorizontally))

    Spacer(Modifier.height(32.dp))
    // Difficulty
    BottomSheetItem(content = {

        val options = listOf(DIFFICULTY_EASY, DIFFICULTY_MEDIUM, DIFFICULTY_HARD)
        var expanded by remember { mutableStateOf(false) }

        OptionWithDropDown(
            options = options,
            optionLabel = "Difficulty",
            showDropDown = expanded,
            selectedOptionText = difficulty,
            onDismissRequest = { expanded = false },
            onDropMenuClick = {
                onDifficultyDropDownClick.invoke(it)
                expanded = false
            },
            onExpandedChange = { expanded = !expanded }
        )
    })


    // WordGroup
    BottomSheetItem(content = {

        val options = listOf("Animals - Domestic", "Animals - Wild", "Home - Kitchen")
        var expanded by remember { mutableStateOf(false) }

        OptionWithDropDown(
            options = options,
            optionLabel = "Word Group",
            showDropDown = expanded,
            selectedOptionText = wordGroup,
            onDismissRequest = { expanded = false },
            onDropMenuClick = {
                println(it)
                onWordGroupDropDownClick.invoke(it)
                expanded = false
            },
            onExpandedChange = { expanded = !expanded }
        )
    })

    //TotalNumber of Questions
    BottomSheetItem(content = {
        AddNSubOption(
            text = "Total Questions",
            onAddButtonClick = onAddQuestionsClick::invoke,
            onSubButtonClick = onSubtractQuestionsClick::invoke,
            total = "$totalNumberOfQuestions"
        )
    })

    SaveChangesButton(onSaveChangesClick = {})
}

@Composable
fun ColumnScope.SaveChangesButton(
    modifier: Modifier = Modifier,
    onSaveChangesClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(top = 24.dp, bottom = 24.dp)
            .size(160.dp, 40.dp)
            .align(Alignment.CenterHorizontally),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = onSaveChangesClick::invoke
    ) {
        Text(
            "SAVE CHANGES",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

//@OptIn(ExperimentalMaterialApi::class)
//@Preview
//@Composable
//fun ListenAndSpellPreview() {
//    SpeechsmithTheme(darkTheme = true) {
//        Surface() {
//            AudioSpellScreen(onHomeClick = {})
//        }
//    }
//}