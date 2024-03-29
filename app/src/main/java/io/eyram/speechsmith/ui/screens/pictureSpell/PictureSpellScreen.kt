package io.eyram.speechsmith.ui.screens.pictureSpell


import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import io.eyram.speechsmith.ui.components.*
import io.eyram.speechsmith.ui.screens.audioSpell.ConnectivityStatus
import io.eyram.speechsmith.ui.screens.destinations.AudioSpellScreenDestination
import io.eyram.speechsmith.ui.screens.destinations.ExerciseCompleteScreenDestination
import io.eyram.speechsmith.ui.screens.exercisecomplete.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon as Material3Icon

@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PictureSpellScreen(
    navigator: DestinationsNavigator,
    viewModel: PictureSpellViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
) {
    val uiState = viewModel.uiState
    val spellFieldState = uiState.spellFieldState
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }
    fun getCurrentExercise() =
        "${uiState.currentExerciseNumber + 1} OF ${uiState.totalNumberOfQuestions}"
    val connectivityStatus =
        viewModel.listenForConnectivity().collectAsState(initial = ConnectivityStatus.Available)

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Black,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetState = bottomSheetState,
        sheetContent = { BottomSheetContent() }
    ) {
        Scaffold(
            topBar = {
                SpeechSmithAppBar(
                    onHomeClick = { navigator.navigateUp() },
                    onSettingsClick = { coroutineScope.launch { bottomSheetState.show() } }
                )
            },
        ) { paddingValues ->

            PictureSpellScreenContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                spellFieldState = spellFieldState,
                onPrevClick = viewModel::onPrevPress,
                onNextClick = viewModel::onNextPress,
                onHintClick = { showDialog = true },
                onEnterPress = viewModel::onEnterPress,
                currentExercise = getCurrentExercise()
            )

            if (showDialog) {
                PictureSpellHintDialog(
                    wordToSpell = uiState.wordToSpell.toUpperCase(Locale.current),
                    onDismissRequest = { showDialog = false },
                    onPlaySoundClick = viewModel::onPlaySoundClick,
                    audioPlayerState = uiState.audioPlayerState
                )
            }

            if (uiState.isExerciseComplete) {
                navigator.navigate(
                    ExerciseCompleteScreenDestination(from = Screen.PictureSpell),
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
fun PictureSpellScreenContent(
    modifier: Modifier = Modifier,
    currentExercise: String,
    uiState: PictureSpellScreenState,
    spellFieldState: SpellFieldState,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onEnterPress: () -> Unit,
    onHintClick: () -> Unit
) {

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (hintRef, keyboardRef, imageColumnRef, indicationRef) = createRefs()

        HintRow(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .constrainAs(hintRef) {
                    top.linkTo(parent.top, 12.dp)
                    start.linkTo(imageColumnRef.start)
                    end.linkTo(imageColumnRef.end)
                },
            onHintClick = onHintClick::invoke,
            exerciseNumberTracker = currentExercise,
            onScoreCardClick = {},
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
                        text = visualIndicatorState.message
                    )

                    Material3Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(id = visualIndicatorState.icon),
                        contentDescription = null
                    )
                }
            }
        }

        Column(
            modifier = Modifier.constrainAs(imageColumnRef) {
                top.linkTo(hintRef.bottom)
                bottom.linkTo(keyboardRef.top)
                start.linkTo(keyboardRef.start)
                end.linkTo(keyboardRef.end)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            ImageView(
                onPrevClick = onPrevClick::invoke,
                onNextClick = onNextClick::invoke,
                imageUrl = uiState.imageUrl
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
            onEnterPress = onEnterPress::invoke,
            onBackSpacePress = spellFieldState::onBackSpacePress
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build(),
    )

    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LaunchedEffect(painter.state) {
            when (painter.state) {
                is AsyncImagePainter.State.Error -> {
                    println("Image Loading Error")
                }
                is AsyncImagePainter.State.Success -> {
                    println("ImageLoading Success")
                }
                is AsyncImagePainter.State.Empty -> {
                    println("Image Loading Empty")
                }
                is AsyncImagePainter.State.Loading -> {
                    println("Image Loading ")
                }
            }
        }
        PrevNextButton(
            label = LABEL_PREV,
            onClick = onPrevClick::invoke,
        )
        AnimatedContent(
            targetState = painter.state,
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(),
                    initialContentExit = fadeOut()
                )
            }
        ) { targetState ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Gray.copy(alpha = 0.5F)
            ) {
                when (targetState) {
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = modifier
                                .size(220.dp, 165.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    is AsyncImagePainter.State.Error -> {

                    }
                    else -> {
                        ShimmerAnimation(Modifier.size(220.dp, 165.dp))
                    }
                }
            }
        }

        PrevNextButton(
            label = LABEL_NEXT,
            onClick = onNextClick::invoke,
        )
    }
}

@Composable
fun ColumnScope.BottomSheetContent() {

    DragIndicator(Modifier.align(Alignment.CenterHorizontally))

//    BottomSheetItem(
//        modifier = Modifier.padding(top = 32.dp),
//        content = {
//            OptionWithDropDown(optionLabel = "Word Group") {
//
//            }
//        })

//    BottomSheetItem(content = {
//        AddNSubOption(text = "Total Questions",
//            onAddButtonClick = {},
//            onSubButtonClick = {}
//        )
//    })
//
//    BottomSheetItem(content = {
//        AddNSubOption(text = "Max Letters In Word",
//            onAddButtonClick = {},
//            onSubButtonClick = {}
//        )
//    })

    SaveChangesButton()
}

@Composable
fun ColumnScope.SaveChangesButton(modifier: Modifier = Modifier) {
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
        onClick = { /*TODO*/ }) {
        Text(
            "SAVE CHANGES",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

const val LABEL_HOME = "HOME"
const val LABEL_SETTINGS = "SETTINGS"
const val LABEL_PREV = "PREV"
const val LABEL_NEXT = "NEXT"