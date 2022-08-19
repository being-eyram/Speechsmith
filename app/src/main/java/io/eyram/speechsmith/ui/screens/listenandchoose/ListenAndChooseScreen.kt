package io.eyram.speechsmith.ui.screens.listenandchoose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.PrevNextButton
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.spellingexercise.LABEL_PREV
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@Composable
fun ListenAndChooseTextScreen() {

}

@Preview
@Composable
fun ListenNChoosePreview() {
    SpeechsmithTheme {

    }
}

@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onPlaySoundClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(132.dp)
            .fillMaxWidth()
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PrevNextButton(label = LABEL_PREV, onClick = { /*TODO*/ }, enabled = true)
            PlaySoundButton {}
            PrevNextButton(label = LABEL_NEXT, onClick = { /*TODO*/ }, enabled = true)
        }
    }
}

@Preview
@Composable
fun BottomControlsPreview() {
    SpeechsmithTheme {
        BottomControls(onPrevClick = { /*TODO*/ }, onPlaySoundClick = { /*TODO*/ }) {

        }
    }
}

@Composable
fun PlaySoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.size(184.dp, 56.dp),
        onClick = onClick::invoke,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.05F))
    ) {
        Icon(
            modifier = Modifier.padding(end = 12.dp),
            painter = painterResource(id = R.drawable.ic_play_circle),
            contentDescription = null
        )
        Text(
            text = PLAY_SOUND,
            style = MaterialTheme.typography.bodyLarge
        )

    }
}

@Composable
fun OptionCard(
    modifier: Modifier = Modifier,
    optionBody: String,
    onClick: () -> Unit,

    ) {
    Button(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .fillMaxWidth(),
        border = BorderStroke(Dp.Hairline, color = Color.DarkGray),
        contentPadding = PaddingValues(horizontal = 16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
        onClick = onClick::invoke

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OptionLabel(label = "1")
                Text(
                    text = optionBody,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp
                    )
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_correct),
                contentDescription = null
            )
        }
    }
}

@Composable
fun OptionLabel(
    modifier: Modifier = Modifier,
    label: String = ""
) {
    Box(
        modifier = modifier
            .size(24.dp, 32.dp)
            .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Preview
@Composable
fun OptionCardPreview() {
    SpeechsmithTheme {
        OptionCard(optionBody = "Apples") { }
    }
}

@Preview
@Composable
fun PlayButtonPrev() {
    SpeechsmithTheme {
        PlaySoundButton {

        }
    }
}

const val PLAY_SOUND = "PLAY SOUND"