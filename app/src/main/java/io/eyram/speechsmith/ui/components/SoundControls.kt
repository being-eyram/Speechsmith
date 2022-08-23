package io.eyram.speechsmith.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.screens.audioToWordMatch.PLAY_SOUND
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_NEXT
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_PREV

@Composable
fun SoundControls(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onPlaySoundClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PrevNextButton(label = LABEL_PREV, onClick = { /*TODO*/ }, enabled = true)
        PlaySoundButton {}
        PrevNextButton(label = LABEL_NEXT, onClick = { /*TODO*/ }, enabled = true)
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
