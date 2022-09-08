package io.eyram.speechsmith.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.screens.audioSpell.AudioPlayerState
import io.eyram.speechsmith.ui.screens.audioToWordMatch.LOADING
import io.eyram.speechsmith.ui.screens.audioToWordMatch.PLAYING
import io.eyram.speechsmith.ui.screens.audioToWordMatch.PLAY_SOUND
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_PREV
import io.eyram.speechsmith.util.NowPlayingAnimation

@Composable
fun SoundControls(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onPlaySoundClick: () -> Unit,
    onNextClick: () -> Unit,
    audioPlayerState: AudioPlayerState
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PrevNextButton(label = LABEL_PREV, onClick = onPrevClick::invoke, enabled = true)
        PlaySoundButton(onClick = onPlaySoundClick::invoke, audioPlayerState = audioPlayerState)
        PrevNextButton(label = LABEL_NEXT, onClick = onNextClick::invoke, enabled = true)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlaySoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    audioPlayerState: AudioPlayerState
) {
    Button(
        modifier = modifier.size(184.dp, 56.dp),
        onClick = onClick::invoke,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(0.05F),
            contentColor = Color.White
        )
    ) {

        AnimatedContent(targetState = audioPlayerState) { targetState ->
            when (targetState) {
                AudioPlayerState.Playing -> {
                    NowPlayingAnimation(Modifier.padding(end = 12.dp, bottom = 10.dp))
                }
                AudioPlayerState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF3E97E0)
                    )
                }
                else -> {
                    Icon(
                        modifier = Modifier.padding(end = 12.dp),
                        painter = painterResource(id = R.drawable.ic_play_circle),
                        contentDescription = null
                    )
                }
            }
        }
        Text(
            text = when (audioPlayerState) {
                AudioPlayerState.Playing -> PLAYING
                AudioPlayerState.Loading -> LOADING
                else -> PLAY_SOUND
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
