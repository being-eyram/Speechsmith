package io.eyram.speechsmith.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_HOME
import io.eyram.speechsmith.ui.screens.pictureSpell.LABEL_SETTINGS
import io.eyram.speechsmith.ui.screens.pictureSpell.PictureSpellScreen
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import java.util.*


@Composable
fun SpeechSmithAppBar(onHomeClick: () -> Unit, onSettingsClick: () -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.Black),

        ) {
        val (homeRef, settingsRef) = createRefs()
        AppBarButton(
            modifier = Modifier.constrainAs(homeRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(settingsRef.start)
            },
            icon = R.drawable.ic_home,
            onClick = onHomeClick::invoke,
            label = LABEL_HOME
        )

        AppBarButton(
            modifier = Modifier.constrainAs(settingsRef) {
                start.linkTo(homeRef.end)
                top.linkTo(homeRef.top)
                bottom.linkTo(homeRef.bottom)
                end.linkTo(parent.end)
            },
            icon = R.drawable.ic_settings,
            onClick = onSettingsClick::invoke,
            label = LABEL_SETTINGS
        )
    }
}

@Composable
fun AppBarButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    label: String,
) {
    ConstraintLayout(
        modifier = modifier
            .height(36.dp)
            .wrapContentWidth()
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick::invoke)
            .border(Dp.Hairline, Color.DarkGray, RoundedCornerShape(50)),
    ) {
        val (iconRef, textRef) = createRefs()
        Icon(
            modifier = Modifier.constrainAs(iconRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, 20.dp)
            },
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.constrainAs(textRef) {
                top.linkTo(iconRef.top)
                bottom.linkTo(iconRef.bottom)
                start.linkTo(iconRef.end, 8.dp)
                end.linkTo(parent.end, 20.dp)
            },
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
        )
        Spacer(Modifier.width(20.dp))
    }
}