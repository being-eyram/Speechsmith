package io.eyram.speechsmith.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.ui.screens.spellingexercise.KeyboardUiState
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

//KeyboardUiState(KeyboardEvents.generateKeyboardLabels(wordToSpell))
@Composable
fun Keyboard(
    modifier: Modifier = Modifier,
    keyboardEvents: KeyboardEvents,
    keyboardLavels: KeyboardUiState,
) {

    val state = remember { keyboardLavels }

    Column(modifier = modifier) {

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
                    onClick = {
                        keyboardEvents.onKeyPress(state.keyboardLabels[index])
                    }
                )
                {
                    Text(
                        text = state.keyboardLabels[index],
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
                onClick = { keyboardEvents.onEnterPress() }
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
                onClick = { keyboardEvents.onBackSpacePress() }
            ) {
                Text(
                    text = "BACKSPACE",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
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
//        Keyboard()
    }
}



