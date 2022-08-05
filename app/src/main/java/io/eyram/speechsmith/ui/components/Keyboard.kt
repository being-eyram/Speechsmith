package io.eyram.speechsmith.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme


@Composable
fun KeyboardUi(modifier: Modifier = Modifier) {
    val wordToSpell = "racoon"
    val spellBoxState = remember { SpellBoxState(wordToSpell) }
    val keyboard = Keyboard(spellBoxState)

    val keyboardUiState = remember(wordToSpell) {
        KeyboardUiState(
            keyboardCharacters = Keyboard.generateKeyboardLabels(wordToSpell)
        )
    }

    val keyboardLabels = keyboardUiState.keyboardCharacters
    val spellBoxUiState = spellBoxState.spellBoxUiState
    val spellBoxStateList = spellBoxUiState.spellCheckState

    //Return the Character typed from the keyboard or an empty string if nothing was
    //entered
    fun getTypedCharacter(index: Int): String {
        return if (index <= spellBoxUiState.typedCharacters.lastIndex)
            spellBoxUiState.typedCharacters[index]
        else ""
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            spellBoxStateList.forEachIndexed { index, spellBoxState ->
                SpellBox(
                    text = getTypedCharacter(index),
                    isNext = index == spellBoxUiState.spellBoxIndicatorPosition,
                    state = spellBoxState
                )
            }
        }

        KeyboardLayout(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .requiredHeightIn(min = 168.dp, max = 224.dp)
        ) {
            repeat(15) { index ->
                Button(
                    border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.background),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .layoutId("key$index")
                        .fillMaxSize(),
                    onClick = { keyboard.onKeyPress(keyboardLabels[index]) }
                )
                {
                    Text(
                        text = keyboardLabels[index],
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            Button(
                border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.background),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .layoutId(ENTER_KEY_ID)
                    .fillMaxSize(),
                onClick = { keyboard.onEnterPress() }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "ENTER",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Button(
                border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.background),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .layoutId(BACKSPACE_KEY_ID)
                    .fillMaxSize(),
                onClick = { keyboard.onBackSpacePress() }
            ) {
                Text(
                    text = "BACKSPACE",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun SpellBox(
    modifier: Modifier = Modifier,
    text: String,
    isNext: Boolean = false,
    state: SpellCheckState
) {
    val borderColor = remember { Animatable(Color(0xFFA4A4A4)) }
    val backgroundColor = remember { Animatable(Color.Transparent) }

    Box(
        modifier = modifier
            .padding(horizontal = 1.dp)
            .background(color = backgroundColor.value)
            .size(width = 32.dp, height = 40.dp)
            .border(
                width = 1.dp,
                color = borderColor.value
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }

    LaunchedEffect(isNext, state) {
        borderColor.animateTo(
            targetValue = if (isNext) Color(0xFFE0991A) else Color(0xFFA4A4A4),
            animationSpec = tween(
                durationMillis = 350,
                easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
            )
        )
        backgroundColor.animateTo(
            targetValue = when (state) {
                SpellCheckState.Matched -> Color(0xFF538D4E)
                SpellCheckState.Unmatched -> Color(0xFF3A3A3C)
                else -> Color.Unspecified
            },
            animationSpec = tween(
                durationMillis = 195,
                easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
            )
        )
    }
}

const val ENTER_KEY_ID = "Enter_Key"
const val BACKSPACE_KEY_ID = "Backspace_Key"

@Preview(device = Devices.NEXUS_5X, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(device = Devices.NEXUS_5, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_2_XL, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    SpeechsmithTheme {
        KeyboardUi()
    }
}


enum class SpellCheckState {
    Initial,
    Matched,
    Unmatched,
}
