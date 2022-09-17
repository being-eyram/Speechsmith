package io.eyram.speechsmith.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.eyram.speechsmith.ui.screens.audioSpell.AudioPlayerState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PictureSpellHintDialog(
    modifier: Modifier = Modifier,
    wordToSpell: String,
    onPlaySoundClick: () -> Unit,
    onDismissRequest: () -> Unit,
    audioPlayerState: AudioPlayerState
) {


    Dialog(onDismissRequest = onDismissRequest::invoke) {
        Surface(
            modifier = modifier.size(320.dp, 240.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF202020)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                PlaySoundButton(
                    modifier = Modifier.width(220.dp),
                    onClick = onPlaySoundClick,
                    audioPlayerState = audioPlayerState
                )
                DragToReveal(wordToSpell = wordToSpell)
            }
        }
    }
}

