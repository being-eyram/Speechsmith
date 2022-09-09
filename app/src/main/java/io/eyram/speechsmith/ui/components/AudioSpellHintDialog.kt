package io.eyram.speechsmith.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SwipeableDefaults.StiffResistanceFactor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import kotlin.math.roundToInt

@Composable
fun AudioSpellHintDialog(
    modifier: Modifier = Modifier,
    wordToSpell: String,
    hintImageUrl: String,
    onDismissRequest: () -> Unit
) {

    Dialog(onDismissRequest = onDismissRequest::invoke) {
        Surface(
            modifier = modifier.size(320.dp, 480.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF202020)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                AsyncImage(
                    modifier = modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(265.dp, 353.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(hintImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                DragToReveal(wordToSpell = wordToSpell)
            }
        }
    }
}

@Preview
@Composable
fun AudioSpellHintDialogPreview() {
    // AudioSpellHintDialog()
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DragToReveal(modifier: Modifier = Modifier, wordToSpell: String) {

    val state = rememberSwipeableState(HintState.Hidden)
    val sizePx = with(LocalDensity.current) { 220.dp.toPx() }
    val endAnchor = with(LocalDensity.current) { 220.dp.toPx() - 48.dp.toPx() }
    val anchors = mapOf(
        0f to HintState.Hidden,
        endAnchor to HintState.Revealed
    ) // Maps anchor points (in px) to states
    val transition = updateTransition(targetState = state.currentValue, label = "Hidden")
    val backgroundColor = transition.animateColor(label = "") {
        when (it) {
            HintState.Hidden -> Color(0xFFE0991A)
            HintState.Revealed -> Color(0xFF538D4E)
        }
    }

    // var labelAlpha by remember { mutableStateOf(1F) }

    val labelAlpha = remember((state.progress)) {
        derivedStateOf {
            if (state.offset.value > 0F) 1 - state.progress.fraction else 1F
        }
    }

    Box(
        modifier = modifier
            .swipeable(
                state = state,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.998f) },
                orientation = Orientation.Horizontal,
                resistance = ResistanceConfig(
                    basis = sizePx,
                    factorAtMin = StiffResistanceFactor,
                ),
                velocityThreshold = 999_000_000.dp,
                enabled = state.currentValue != HintState.Revealed
            )
            .size(220.dp, 48.dp)
            .background(color = backgroundColor.value, shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.CenterStart

    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(labelAlpha.value),
            text = "SWIPE TO REVEAL",
            style = MaterialTheme.typography.labelMedium.copy(
                Color.White,
                textAlign = TextAlign.Center
            )
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (state.offset.value > 0f) state.progress.fraction else 0f),
            text = wordToSpell,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(state.offset.value.roundToInt(), 0) }
                .padding(start = 4.dp)
                .size(40.dp)
                .background(Color.Black, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}


@Preview
@Composable
fun DragToRevealPreview() {
    SpeechsmithTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            DragToReveal(wordToSpell = "CHICKEN")
        }
    }
}

enum class HintState { Hidden, Revealed }