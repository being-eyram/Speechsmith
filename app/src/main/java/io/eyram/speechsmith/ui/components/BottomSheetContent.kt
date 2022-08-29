package io.eyram.speechsmith.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import io.eyram.speechsmith.R

@Composable
fun BottomSheetItem(
    modifier: Modifier = Modifier,
    content: @Composable ConstraintLayoutScope.() -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .height(64.dp)
            .fillMaxWidth()
            // .border(width = Dp.Hairline .times(0.01F), Color.Gray, shape = RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.06F), shape = RoundedCornerShape(6.dp)),
        content = content
    )
}


@Composable
fun OpButtonGroup(
    modifier: Modifier = Modifier,
    onAddButtonClick: () -> Unit,
    onSubButtonClick: () -> Unit
) {
    Row(modifier = modifier) {
        AddOrSubButton(
            onClick = onSubButtonClick::invoke,
            icon = R.drawable.ic_settings,
            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
        )
        Box(
            modifier = Modifier
                .background(Color(0xFF252525))
                .size(64.dp, 32.dp)
                .border(Dp.Hairline, Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "20",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White, fontSize = 16.sp,
                )
            )
        }
        AddOrSubButton(
            onClick = onAddButtonClick::invoke,
            icon = R.drawable.ic_settings,
            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
        )
    }
}

@Composable
fun AddOrSubButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape,
    @DrawableRes icon: Int,
) {
    OutlinedButton(
        modifier = modifier.size(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.DarkGray
        ),
        contentPadding = PaddingValues(0.dp),
        shape = shape,
        onClick = onClick::invoke,
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun ConstraintLayoutScope.AddNSubOption(
    text: String,
    onAddButtonClick: () -> Unit,
    onSubButtonClick: () -> Unit
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

@Composable
fun ConstraintLayoutScope.DropDown(
    option : String = "",
    text: String,
    onDropDownClick: () -> Unit
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

    Row(modifier = Modifier
        .constrainAs(buttonsRef) {
            end.linkTo(parent.end, margin = 16.dp)
            bottom.linkTo(parent.bottom)
            top.linkTo(parent.top)
        }
        .clickable(onClick = onDropDownClick::invoke)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                .size(104.dp, 32.dp)
                .background(
                    color = Color(0xFF252525),
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                )
                .border(1.dp, Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White, fontSize = 16.sp,
                )
            )
        }
        AddOrSubButton(
            onClick = onDropDownClick::invoke,
            icon = R.drawable.ic_chevron_down,
            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
        )
    }
}

@Composable
fun DragIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = 4.dp)
            .size(36.dp, 4.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(50)),
    )
}