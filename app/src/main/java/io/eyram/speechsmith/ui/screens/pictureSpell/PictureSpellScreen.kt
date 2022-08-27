package io.eyram.speechsmith.ui.screens.pictureSpell


import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eyram.speechsmith.ui.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon as Material3Icon


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PictureSpellScreen(
    viewModel: PictureSpellViewModel = viewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    onHomeClick: () -> Unit,
) {
    val uiState = viewModel.uiState
    val spellFieldState = uiState.spellFieldState

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Black,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetState = bottomSheetState,
        sheetContent = { BottomSheetContent() }
    ) {
        Scaffold(
            topBar = {
                SpeechSmithAppBar(
                    onHomeClick = onHomeClick::invoke,
                    onSettingsClick = { coroutineScope.launch { bottomSheetState.show() } }
                )
            },
        ) { paddingValues ->

            PictureSpellScreenContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                spellFieldState = spellFieldState,
                onPrevClick = {},
                onNextClick = {},
                onHintClick = {},
                onEnterPress = viewModel::onEnterPress
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PictureSpellScreenContent(
    modifier: Modifier = Modifier,
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
            score = "",
            onScoreCardClick = {},
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
                onNextClick = onNextClick::invoke
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
fun ColumnScope.BottomSheetContent() {

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

    SaveChangesButton()
}

@Composable
fun ColumnScope.SaveChangesButton(modifier: Modifier = Modifier) {
    Button(
        modifier = Modifier
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