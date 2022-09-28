package io.eyram.speechsmith.ui.screens.yesandno

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.eyram.speechsmith.ui.components.SpeechSmithAppBar
import io.eyram.speechsmith.ui.screens.audioToWordMatch.OptionButtonState
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YesOrNoScreen(
    navigator: DestinationsNavigator,
    viewModel: YesAndNoViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            SpeechSmithAppBar(
                onHomeClick = { navigator.navigateUp() },
                onSettingsClick = {
                    //coroutineScope.launch { bottomSheetState.show() }
                }
            )
        }
    ) {
        YesOrNoContent(
            modifier = Modifier.padding(it),
            viewModel = viewModel
        )
    }
}

@Composable
fun YesOrNoContent(modifier: Modifier = Modifier, viewModel: YesAndNoViewModel) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(viewModel.questions) { index, question ->
            YesOrNoCard(
                question = question,
                questionNumber = index + 1
            )
        }
    }
}

@Preview
@Composable
fun YesOrNoPreview() {
    val viewModel: YesAndNoViewModel = viewModel()
    SpeechsmithTheme {
        YesOrNoContent(viewModel = viewModel)
    }
}


@Composable
fun YesOrNoCard(
    question: YesOrNoQuestion,
    questionNumber: Int = 1,
) {
    Card(
        Modifier
            .fillMaxWidth()
            .height(164.dp),
        border = BorderStroke(1.dp, Color(0xFF7E7E7E)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF141414),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentSize()
            ) {
                Text(
                    modifier = Modifier.paddingFromBaseline(top = 24.dp),
                    text = "QUESTION $questionNumber",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                    color = Color.White.copy(alpha = 0.65F),
                )
                Text(
                    modifier = Modifier.paddingFromBaseline(top = 56.dp, bottom = 16.dp),
                    text = question.question,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                    color = Color.White.copy(alpha = 0.87F),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var enabled by remember { mutableStateOf(true) }

                options.forEach() { option ->

                    OptionButtonYesOrNo(
                        label = option,
                        answer = question.answer,
                        enabled = enabled
                    ) {
                        enabled = false
                    }
                }
            }
        }
    }
}

val options = listOf(YES, NO)

@Composable
fun OptionButtonYesOrNo(
    label: String,
    answer: String,
    enabled: Boolean,
    onClickFinish: () -> Unit
) {

    var state by remember { mutableStateOf(OptionButtonState.Initial) }


    YesOrNoButtonInternal(
        label = label,
        state = state,
        enabled = enabled
    ) {
        state = if (answer == label) {
            OptionButtonState.Correct
        } else OptionButtonState.Incorrect

        onClickFinish.invoke()
    }
}


@Composable
private fun YesOrNoButtonInternal(
    label: String,
    state: OptionButtonState,
    enabled: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            OptionButtonState.Correct -> Color(0xFF538D4E)
            OptionButtonState.Incorrect -> Color(0xFFBF4040)
            else -> Color.White.copy(alpha = 0.05F)
        }
    )

    Button(
        modifier = Modifier.size(96.dp, 40.dp),
        onClick = onClick::invoke,
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 16.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White,
            disabledContainerColor = backgroundColor,
            disabledContentColor = Color.White
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


@Preview
@Composable
fun YesAndNoButtonPreview() {
    SpeechsmithTheme {
//        YesOrNoCard({},{})
    }
}