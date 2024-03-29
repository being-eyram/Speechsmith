package io.eyram.speechsmith.ui.screens.audioSpell

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import io.eyram.speechsmith.data.preferences.DIFFICULTY_EASY
import io.eyram.speechsmith.data.preferences.DIFFICULTY_HARD
import io.eyram.speechsmith.data.preferences.DIFFICULTY_MEDIUM
import io.eyram.speechsmith.ui.components.*
import io.eyram.speechsmith.ui.screens.audioToWordMatch.LABEL_QUESTION
import io.eyram.speechsmith.ui.screens.destinations.AudioSpellScreenDestination
import io.eyram.speechsmith.ui.screens.destinations.ExerciseCompleteScreenDestination
import io.eyram.speechsmith.ui.screens.exercisecomplete.Screen
import kotlinx.coroutines.launch

@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AudioSpellScreen(
    navigator: DestinationsNavigator,
    viewModel: AudioSpellViewModel = hiltViewModel(),
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
) {
    val uiState = viewModel.uiState
    val spellFieldState = viewModel.uiState.spellFieldState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val connectivityStatus =
        viewModel.listenForConnectivity().collectAsState(initial = ConnectivityStatus.Available)

    fun getCurrentExercise() =
        "${uiState.currentExerciseNumber + 1} OF ${uiState.totalNumberOfQuestions}"

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = Color.Black,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            AudioSpellBottomSheetContent(
                wordGroup = uiState.exerciseWordGroup,
                difficulty = uiState.exerciseDifficulty,
                totalNumberOfQuestions = uiState.totalNumberOfQuestions,
                onSaveChangesClick = viewModel::onSaveChangesClick,
                onAddQuestionsClick = viewModel::onAddQuestionsClick,
                onSubtractQuestionsClick = viewModel::onSubtractQuestionsClick,
                onWordGroupDropDownClick = viewModel::onWordGroupDropDownClick,
                onDifficultyDropDownClick = viewModel::onDifficultyDropDownClick,
            )
        }
    ) {
        Scaffold(
            topBar = {
                SpeechSmithAppBar(
                    onHomeClick = { navigator.navigateUp() },
                    onSettingsClick = {
                        coroutineScope.launch { bottomSheetState.show() }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            AudioSpellContent(
                modifier = Modifier.padding(padding),
                uiState = uiState,
                spellFieldState = spellFieldState,
                onHintClick = { showDialog = true },
                onScoreClick = {},
                onPrevClick = viewModel::onPrevPress,
                onNextClick = viewModel::onNextPress,
                onEnterPress = viewModel::onEnterPress,
                exerciseNumberTracker = getCurrentExercise(),
                onPlaySoundClick = viewModel::onPlaySoundClick
            )
            if (showDialog) {
                AudioSpellHintDialog(
                    wordToSpell = uiState.wordToSpell.toUpperCase(Locale.current),
                    hintImageUrl = uiState.hintImgUrl,
                    onDismissRequest = { showDialog = false },
                )
            }
            if (uiState.isExerciseComplete) {
                navigator.navigate(
                    ExerciseCompleteScreenDestination(from = Screen.AudioSpell),
                    onlyIfResumed = true
                ) {
                    popUpTo(AudioSpellScreenDestination) { inclusive = true }
                }
            }

            if (connectivityStatus.value == ConnectivityStatus.Unavailable) {
                LaunchedEffect(connectivityStatus.value) {
                    snackbarHostState.showSnackbar(
                        message = "Network Connection Unavailable",
                        duration = SnackbarDuration.Indefinite
                    )
                }
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
    exerciseNumberTracker: String,
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
            exerciseNumberTracker = exerciseNumberTracker,
            onHintClick = onHintClick::invoke,
            onScoreCardClick = onScoreClick::invoke
        )

        AnimatedVisibility(
            modifier = Modifier.constrainAs(indicationRef) {
                top.linkTo(parent.top, 12.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            visible = uiState.showFieldStateVisualIndicator,
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
                        text = visualIndicatorState.message,
                        color = Color.White
                    )

                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(id = visualIndicatorState.icon),
                        tint = Color.White,
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
                audioPlayerState = uiState.audioPlayerState
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
    onSubtractQuestionsClick: () -> Unit,
    onSaveChangesClick: () -> Unit,
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

    SaveChangesButton(onSaveChangesClick = onSaveChangesClick::invoke)
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