package io.eyram.speechsmith.ui.screens.exercisecomplete

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.screens.destinations.AudioSpellScreenDestination
import io.eyram.speechsmith.ui.screens.destinations.ExerciseCompleteScreenDestination
import io.eyram.speechsmith.ui.screens.destinations.HomeScreenDestination
import io.eyram.speechsmith.ui.screens.destinations.PictureSpellScreenDestination
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ExerciseCompleteScreen(navigator: DestinationsNavigator, from: Screen) {
    Scaffold {
        ExerciseCompleteContent(
            modifier = Modifier.padding(it),
            onHomeClick = {
                navigator.navigate(HomeScreenDestination) {
                    popUpTo(ExerciseCompleteScreenDestination) {
                        inclusive = true
                    }
                }
            },
            onRestartClick = {
                when (from) {
                    Screen.AudioSpell -> navigator.navigate(AudioSpellScreenDestination) {
                        popUpTo(ExerciseCompleteScreenDestination) {
                            inclusive = true
                        }
                    }
                    Screen.PictureSpell -> navigator.navigate(PictureSpellScreenDestination) {
                        popUpTo(ExerciseCompleteScreenDestination) {
                            inclusive = true
                        }
                    }
                }

            }
            //try get the route of of the screen we're moving from.
        )
    }
}

@Composable
fun ExerciseCompleteContent(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onRestartClick: () -> Unit
) {
    val party = Party(
        speed = 0f,
        maxSpeed = 45f,
        angle = 225,
        damping = 0.8f,
        spread = 20,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        position = Position.Relative(1.0, 0.25),
        emitter = Emitter(duration = 2000, TimeUnit.MILLISECONDS).max(300)
    )

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingFromBaseline(top = 104.dp),
                    text = "Congratulations",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingFromBaseline(top = 140.dp, bottom = 48.dp),
                    text = "Exercise Complete",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Image(
                painter = painterResource(id = R.drawable.illus_finish),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                Modifier
                    .padding(horizontal = 56.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularButton(
                    drawable = R.drawable.reset_ic,
                    label = "RESTART",
                    onClick = onRestartClick::invoke
                )
                CircularButton(
                    drawable = R.drawable.ic_home,
                    label = "HOME",
                    onClick = onHomeClick::invoke
                )
            }
        }

        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(
                party,
                party.copy(angle = 315, position = Position.Relative(0.0, 0.25))
            ),
        )
    }
}

@Preview
@Composable
fun ExerciseContentPreview() {
    SpeechsmithTheme {
        ExerciseCompleteContent(onHomeClick = {}) {}
    }
}

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.05F),
                shape = CircleShape
            ),
            onClick = onClick::invoke,
        ) {
            Icon(
                painter = painterResource(id = drawable),
                contentDescription = null,
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier.paddingFromBaseline(22.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(Color.White)
        )
    }
}

enum class Screen {
    AudioSpell,
    PictureSpell
}