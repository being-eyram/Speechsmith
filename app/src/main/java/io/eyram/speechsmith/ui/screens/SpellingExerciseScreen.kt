package io.eyram.speechsmith.ui.screens


import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.KeyboardUi
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellingExerciseScreen() {
    Scaffold(
        topBar = { SpellingExerciseAppBar(currentExerciseNumber = "9/10") }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageView(modifier = Modifier.padding(top = 12.dp))
            KeyboardUi(modifier = Modifier.padding(top = 16.dp, bottom = 12.dp))
        }
    }
}

@Composable
fun ImageView(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(256.dp, 280.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.Gray
    ) {}
}

@Composable
fun SpellingExerciseAppBar(currentExerciseNumber: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
        ,
        color = Color.Black,
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp, start = 12.dp, end = 12.dp)
                .fillMaxSize()
               ,
        ) {
            AppBarButton(
                modifier = Modifier.align(Alignment.TopStart),
                icon = R.drawable.ic_home,
                backgroundColor = Color.White.copy(alpha = 0.1F),
                onClick = {},
                label = "home".uppercase(Locale.ROOT)
            )

            Box(
                Modifier
                    .size(120.dp, 40.dp)
                    .align(Alignment.TopCenter)
                    .border(
                        width = Dp.Hairline,
                        color = Color(0xFF82DD8B),
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentExerciseNumber,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 16.sp)
                )
            }

            AppBarButton(
                modifier = Modifier.align(Alignment.TopEnd),
                icon = R.drawable.ic_settings,
                backgroundColor = Color(0xFFFF8717),
                onClick = {},
                label = "settings".uppercase(Locale.ROOT)
            )
        }
    }
}


@Preview
@Composable
fun SpellingExerciseScreenPreview() {
    SpeechsmithTheme {
        SpellingExerciseScreen()
    }
}

@Composable
fun AppBarButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    backgroundColor: Color,
    onClick: () -> Unit,
    label: String,
) {
    Column(
        modifier = modifier.clickable(
            onClick = { onClick() },
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColorFor(backgroundColor = backgroundColor)
            ),
            onClick = { }
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Text(
            modifier = Modifier.paddingFromBaseline(top = 16.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
    }

}