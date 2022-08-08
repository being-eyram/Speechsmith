package io.eyram.speechsmith.ui.screens.spellingexercise


import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.Keyboard
import io.eyram.speechsmith.ui.components.SpellCheckState
import io.eyram.speechsmith.ui.components.SpellField
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellingExerciseScreen(viewModel: SpellingExerciseScreenVM = viewModel()) {

    val uiState = viewModel.uiState
    val spellFieldState = uiState.spellFieldState
    val spellCheckState = spellFieldState.spellCheckState
    val isWordSpeltCorrectly = spellCheckState.all { it == SpellCheckState.Matched }
    val transitionState = MutableTransitionState(isWordSpeltCorrectly)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SpellingExerciseAppBar(currentExerciseNumber = "9/10")
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                ImageView(modifier = Modifier.padding(top = 12.dp))

                Column {
                    SpellField(
                        spellFieldState = spellFieldState,
                        onSpellCheckFinish = {
                            coroutineScope.launch {
                                if (transitionState.targetState && transitionState.isIdle) {
                                    delay(200)
                                    viewModel.showNextWord()
                                }
                            }
                        }
                    )
                    Keyboard(
                        modifier = Modifier.padding(top = 16.dp),
                        keyboardLabels = uiState.keyboardLabels,
                        onKeyPress = spellFieldState::onKeyPress,
                        onEnterPress = spellFieldState::onEnterPress,
                        onBackSpacePress = spellFieldState::onBackSpacePress
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopCenter),
                visibleState = transitionState,
                exit = fadeOut(tween(1050))
            ) {
                Card(
                    Modifier.size(240.dp, 40.dp)
                ) {}
            }
        }
    }
}

@Composable
fun ImageView(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(256.dp, 280.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.Gray
    ) {}
}

@Composable
fun SpellingExerciseAppBar(currentExerciseNumber: String) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        color = Color.Black,
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp, start = 12.dp, end = 12.dp)
                .fillMaxSize(),
        ) {
            AppBarButton(
                modifier = Modifier.align(Alignment.TopStart),
                icon = R.drawable.ic_home,
                backgroundColor = Color.White.copy(alpha = 0.1F),
                onClick = {},
                label = LABEL_HOME
            )

            Box(
                Modifier
                    .size(120.dp, 40.dp)
                    .align(Alignment.TopCenter)
                    .border(
                        width = Dp.Hairline,
                        color = Color(0xFF82DD8B),
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentExerciseNumber,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 16.sp)
                )
            }

            AppBarButton(
                modifier = Modifier.align(Alignment.TopEnd),
                icon = R.drawable.ic_settings,
                backgroundColor = Color(0xFFFF8717),
                onClick = {},
                label = LABEL_SETTINGS
            )
        }
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
    backgroundColor: Color,
    onClick: () -> Unit,
    label: String,
) {
    Column(
        modifier = modifier.clickable(
            onClick = { onClick() },
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColorFor(backgroundColor = backgroundColor)
            ),
            onClick = { }
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Text(
            modifier = Modifier.paddingFromBaseline(top = 16.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

const val LABEL_HOME = "HOME"
const val LABEL_SETTINGS = "SETTINGS"