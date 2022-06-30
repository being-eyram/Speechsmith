package io.eyram.speechsmith.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    color: Color
) {
    Card(
        modifier = modifier.size(width = 328.dp, 152.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text("Title")
                Text("Body")
            }
            Image(
                painter = painterResource(id = R.drawable.ic_listening_illus),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ExerciseCardPreview() {
    ExerciseCard(color = Color.Black)
}