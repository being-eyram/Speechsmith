package io.eyram.speechsmith.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    text: String,
    onAddButtonClick: () -> Unit,
    onSubButtonClick: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .height(64.dp)
            .fillMaxWidth()
            .border(width = Dp.Hairline, Color.Gray)
            .background(Color.White.copy(alpha = 0.05F), shape = RoundedCornerShape(6.dp))
    ) {
        val (textRef, buttonsRef) = createRefs()

        Text(
            modifier = Modifier.constrainAs(textRef) {
                start.linkTo(parent.start, margin = 16.dp)
                top.linkTo(buttonsRef.top)
                bottom.linkTo(buttonsRef.bottom)
            },
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                Color.White, fontSize = 16.sp,
            )
        )

        OpButtonGroup(
            modifier = Modifier.constrainAs(buttonsRef) {
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom)
                top.linkTo(parent.top)
            },
            onAddButtonClick = onAddButtonClick::invoke,
            onSubButtonClick = onSubButtonClick::invoke
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BottomSheetContentPreview() {
    SpeechsmithTheme(darkTheme = true) {
        BottomSheetContent(Modifier, text = "Word Group", {}, {})
    }
}

@Composable
fun OpButtonGroup(
    modifier: Modifier = Modifier,
    onAddButtonClick: () -> Unit,
    onSubButtonClick: () -> Unit
) {
    Row(modifier = modifier) {
        OpButton(
            onClick = onSubButtonClick,
            icon = R.drawable.ic_settings,
            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
        )
        Box(
            modifier = modifier
                .background(Color(0xFF252525))
                .size(64.dp, 32.dp)
                .border(Dp.Hairline, Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "20",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White, fontSize = 16.sp,
                )
            )
        }
        OpButton(
            onClick = onAddButtonClick,
            icon = R.drawable.ic_settings,
            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
        )
    }
}

@Composable
fun OpButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape,
    @DrawableRes icon: Int,
) {
    OutlinedButton(
        modifier = modifier.size(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp),
        shape = shape,
        onClick = onClick::invoke,
        border = BorderStroke(Dp.Hairline, Color.Gray)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White
        )
    }
}