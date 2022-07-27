package io.eyram.speechsmith.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import io.eyram.speechsmith.ui.theme.jostFontFamily
import io.eyram.speechsmith.util.SquircleShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCardContainer(
    modifier: Modifier = Modifier,
    color: Color,
    onStartButtonClick: () -> Unit,
    @DrawableRes illustration: Int,
    exerciseType: String,
    showExerciseCard: Boolean,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
                )
            ),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Column(modifier = Modifier.wrapContentSize()) {

            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                ) {
                    Box(modifier = modifier.wrapContentSize()) {
                        Text(
                            modifier = Modifier.paddingFromBaseline(top = 32.dp),
                            text = exerciseType,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                        )
                        Text(
                            modifier = Modifier.paddingFromBaseline(top = 56.dp, bottom = 16.dp),
                            text = """
                    It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.The point
                """.trimIndent(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.87F),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Button(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .height(24.dp),
                        onClick = onStartButtonClick,
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "START",
                            style = MaterialTheme.typography.labelMedium
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_forward),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }


                }
                Spacer(Modifier.width(12.dp))
                Image(
                    modifier = modifier.align(Alignment.Bottom),
                    painter = painterResource(id = illustration),
                    contentDescription = null
                )
            }
        }
        if (showExerciseCard){
            Spacer(Modifier.height(20.dp))
            ExerciseCard()
            Spacer(Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard() {
    Card(
        modifier = Modifier
            .size(152.dp, 208.dp),
        shape = SquircleShape
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                modifier = Modifier.padding(top = 32.dp),
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = "Placeholder",
                tint = Color.Black
            )

            Text(
                modifier = Modifier.paddingFromBaseline(20.dp),
                text = "Spell What You See",
                fontFamily = jostFontFamily,
                fontSize = 16.sp,
                letterSpacing = 0.01.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Button(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(28.dp)
                    .fillMaxWidth(),
                onClick = {},
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "START",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun ExerciseCardPreview() {
    SpeechsmithTheme {
        // A surface container using the 'background' color from the theme

        var showExerciseCard by remember { mutableStateOf(false) }
        ExerciseCardContainer(
            color = Color(0xFF263238),
            onStartButtonClick = { showExerciseCard = !showExerciseCard },
            exerciseType = "Naming Exercise",
            illustration = R.drawable.ic_naming_illus,
            showExerciseCard = showExerciseCard
        )
    }
}


val exerciseMap = mapOf(
    "Naming Exercise" to R.drawable.ic_naming_illus,
    "Listening Exercise" to R.drawable.ic_listening_illus
)