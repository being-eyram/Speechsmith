package io.eyram.speechsmith.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            Text(
                text = "4 of 10",
                style = MaterialTheme.typography.labelMedium
            )
        }

        Button(
            modifier = Modifier.size(84.dp, 40.dp),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(0.dp),
            onClick = onHintClick::invoke
        ) {
            Text(
                text = "Hint",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}