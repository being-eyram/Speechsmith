package io.eyram.speechsmith.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.R

@Composable
fun HintRow(modifier: Modifier = Modifier, onHintClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Card(
            modifier = Modifier.size(84.dp, 40.dp),
            shape = RoundedCornerShape(6.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "4 OF 10",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Button(
            modifier = Modifier.size(84.dp, 40.dp),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.White.copy(alpha = 0.05F)
            ),
            onClick = onHintClick::invoke
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                painter = painterResource(id = R.drawable.ic_bulb),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = HINT,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

const val HINT = "HINT"