package io.eyram.speechsmith.ui.screens.spellingexercise


import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.*
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SpellingExerciseScreen(viewModel: SpellingExerciseScreenVM = viewModel()) {

    val uiState = viewModel.uiState
    val spellFieldState = uiState.spellFieldState
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Black,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetState = sheetState,
        sheetContent = {
            DragIndicator(Modifier.align(Alignment.CenterHorizontally))

            BottomSheetItem(
                modifier = Modifier.padding(top = 32.dp),
                content = {
                    OptionsWithSelection(text = "Word Group") {

                    }
                })

            BottomSheetItem(content = {
                OptionsWithOps(text = "Total Questions",
                    onAddButtonClick = {},
                    onSubButtonClick = {}
                )
            })

            BottomSheetItem(content = {
                OptionsWithOps(text = "Max Letters In Word",
                    onAddButtonClick = {},
                    onSubButtonClick = {}
                )
            })

            Button(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 24.dp)
                    .size(160.dp, 32.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp),
                onClick = { /*TODO*/ }) {
                Text(
                    "SAVE CHANGES",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                SpellingExerciseAppBar(
                    onHomeClick = {},
                    onSettingsClick = { coroutineScope.launch { sheetState.show() } }
                )
            },
        ) { paddingValues ->

            ConstraintLayout(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                val (hintRef, keyboardRef, imageColumnRef, indicationRef) = createRefs()

                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .constrainAs(hintRef) {
                            top.linkTo(parent.top, 12.dp)
                            start.linkTo(imageColumnRef.start)
                            end.linkTo(imageColumnRef.end)
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

                AnimatedVisibility(
                    modifier = Modifier.constrainAs(indicationRef) {
                        top.linkTo(parent.top, 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    visible = showDialog,
                    enter = slideInVertically() + fadeIn(initialAlpha = 0.3f),
                    exit = scaleOut() + fadeOut()
                ) {
                    Card(Modifier.size(240.dp, 40.dp)) {}
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
                        modifier = Modifier.padding(top = 12.dp),
                        onPrevClick = {},
                        onNextClick = {}
                    )
                    SpellField(spellFieldState = spellFieldState)
                }

                Keyboard(
                    modifier = Modifier.constrainAs(keyboardRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    keyboardLabels = uiState.keyboardLabels,
                    onKeyPress = spellFieldState::onKeyPress,
                    onEnterPress = {
                        coroutineScope.launch {
                            spellFieldState.onEnterPress()
                            val inputFieldState = when {
                                spellFieldState.isSpellInputFilled() -> {
                                    if (spellFieldState.isSpellingCorrect())
                                        SpellFieldInputState.Correct
                                    else
                                        SpellFieldInputState.Incorrect
                                }
                                else -> SpellFieldInputState.InComplete
                            }
                            showDialog = true
                            delay(800)
                            showDialog = false
                            if (inputFieldState == SpellFieldInputState.Correct) {
                                delay(300)
                                viewModel.showNextWord()
                            }
                        }
                    },
                    onBackSpacePress = spellFieldState::onBackSpacePress
                )
            }
        }
    }
}


@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    enablePrevButton: Boolean = false,
    enableNextButton: Boolean = true,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PrevNextButton(
            label = LABEL_PREV,
            onClick = onPrevClick::invoke,
            enabled = enablePrevButton
        )
        Surface(
            modifier = modifier.size(220.dp, 165.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.Gray
        ) {}
        PrevNextButton(
            label = LABEL_NEXT,
            onClick = onNextClick::invoke,
            enabled = enableNextButton
        )
    }
}

@Composable
fun SpellingExerciseAppBar(onHomeClick: () -> Unit, onSettingsClick: () -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.Black),

        ) {
        val (homeRef, settingsRef) = createRefs()
        AppBarButton(
            modifier = Modifier.constrainAs(homeRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(settingsRef.start)
            },
            icon = R.drawable.ic_home,
            onClick = onHomeClick::invoke,
            label = LABEL_HOME
        )

        AppBarButton(
            modifier = Modifier.constrainAs(settingsRef) {
                start.linkTo(homeRef.end)
                top.linkTo(homeRef.top)
                bottom.linkTo(homeRef.bottom)
                end.linkTo(parent.end)
            },
            icon = R.drawable.ic_settings,
            onClick = onSettingsClick::invoke,
            label = LABEL_SETTINGS
        )
    }
}


@Preview
@Composable
fun SpellingExerciseScreenPreview() {
    SpeechsmithTheme {
        SpellingExerciseScreen()
    }
}

@Composable
fun AppBarButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    label: String,
) {
    ConstraintLayout(
        modifier = modifier
            .height(36.dp)
            .wrapContentWidth()
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick::invoke)
            .border(Dp.Hairline, Color.DarkGray, RoundedCornerShape(50)),
    ) {
        val (iconRef, textRef) = createRefs()
        Icon(
            modifier = Modifier.constrainAs(iconRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, 20.dp)
            },
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.constrainAs(textRef) {
                top.linkTo(iconRef.top)
                bottom.linkTo(iconRef.bottom)
                start.linkTo(iconRef.end, 8.dp)
                end.linkTo(parent.end, 20.dp)
            },
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.width(20.dp))
    }
}


const val LABEL_HOME = "HOME"
const val LABEL_SETTINGS = "SETTINGS"
const val LABEL_PREV = "PREV"
const val LABEL_NEXT = "NEXT"