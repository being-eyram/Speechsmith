package io.eyram.speechsmith.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_NEXT

@Composable
fun PrevNextButton(
    modifier: Modifier = Modifier,
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
                modifier = Modifier.rotate(if (label == LABEL_NEXT) 0F else 180F),
                painter = painterResource(id = R.drawable.ic_arrow_forward),
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