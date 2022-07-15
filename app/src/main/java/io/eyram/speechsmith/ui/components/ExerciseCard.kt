package io.eyram.speechsmith.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    color: Color,
    onStartButtonClick: () -> Unit,
    @DrawableRes illustration: Int,
    exerciseType: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(160.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
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
}

@Preview
@Composable
fun ExerciseCardPreview() {
    SpeechsmithTheme {
        // A surface container using the 'background' color from the theme

                ExerciseCard(
                    color = Color(0xFF263238),
                    onStartButtonClick = {},
                    exerciseType = "Naming Exercise",
                    illustration = R.drawable.ic_naming_illus
                )

        }
    }




val exerciseMap = mapOf(
    "Naming Exercise" to R.drawable.ic_naming_illus,
    "Listening Exercise" to R.drawable.ic_listening_illus
)