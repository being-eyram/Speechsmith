package io.eyram.speechsmith.ui.screens.pictureSpell


import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eyram.speechsmith.ui.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun PictureSpellScreen(viewModel: PictureSpellViewModel = viewModel()) {

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
                SpeechSmithAppBar(
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

                HintRow(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .constrainAs(hintRef) {
                            top.linkTo(parent.top, 12.dp)
                            start.linkTo(imageColumnRef.start)
                            end.linkTo(imageColumnRef.end)
                        },
                ) {}

                AnimatedVisibility(
                    modifier = Modifier.constrainAs(indicationRef) {
                        top.linkTo(parent.top, 12.dp)
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
                        onPrevClick = {},
                        onNextClick = {}
                    )
                    SpellField(spellFieldState = spellFieldState)
                }

                Keyboard(
                    modifier = Modifier
                        .constrainAs(keyboardRef) {
                            bottom.linkTo(parent.bottom, 12.dp)
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


const val LABEL_HOME = "HOME"
const val LABEL_SETTINGS = "SETTINGS"
const val LABEL_PREV = "PREV"
const val LABEL_NEXT = "NEXT"