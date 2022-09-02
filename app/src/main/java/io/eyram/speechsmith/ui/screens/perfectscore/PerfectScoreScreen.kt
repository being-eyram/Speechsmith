package io.eyram.speechsmith.ui.screens.perfectscore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun PerfectScore() {
    Box(Modifier.fillMaxSize()) {
        val party = Party(
            speed = 0f,
            maxSpeed = 45f,
            angle = 225,
            damping = 0.8f,
            spread = 20,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(1.0, 0.5),
            emitter = Emitter(duration = 2000, TimeUnit.MILLISECONDS).max(300)
        )
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(party, party.copy(angle = 315, position = Position.Relative(0.0, 0.5))),
        )
    }
}

@Preview
@Composable
fun PerfectScorePreview() {
    PerfectScore()
}