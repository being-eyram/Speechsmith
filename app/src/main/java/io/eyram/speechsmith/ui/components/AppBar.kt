package io.eyram.speechsmith.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import java.util.*


@Composable
fun SpeechsmithAppBar() {
    Box(
        modifier = Modifier
            .background(color = Color(0xFF1E1E1E))
            .fillMaxWidth()
            .height(272.dp)
            .padding(start = 16.dp),

        ) {
        Image(
            modifier = Modifier.align(Alignment.TopEnd),
            painter = painterResource(id = R.drawable.ic_memphis_decor),
            contentDescription = null
        )

        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = null
            )

            Text(
                modifier = Modifier.paddingFromBaseline(top = 60.dp, bottom = 12.dp),
                text = "Welcome \n Sally,",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
            )

            Text(
                modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 40.dp),
                text = """
                    Press start button to begin an exercise. 
                    Press page 2 to see other exercises. 
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = Color.White.copy(alpha = 0.87F)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .height(28.dp)
                        .weight(1f),
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Page 1".uppercase(Locale.ROOT),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    modifier = Modifier
                        .height(28.dp)
                        .weight(1f),
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_page),
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Page 2".uppercase(Locale.ROOT),
                        style = MaterialTheme.typography.labelMedium
                    )

                }
            }
        }
    }
}


@Preview
@Composable
fun SpeechsmithAppBarPreview() {
    SpeechsmithTheme {
        SpeechsmithAppBar()
    }
}