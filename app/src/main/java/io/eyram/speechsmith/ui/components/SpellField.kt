package io.eyram.speechsmith.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SpellField(
    modifier: Modifier = Modifier,
    spellFieldState: SpellFieldState,
    onSpellCheckFinish: () -> Unit
) {

    val spellCheckState = spellFieldState.spellCheckState

    fun getTypedCharacterOrEmpty(index: Int): String {
        val lastIndex = spellFieldState.typedCharacters.lastIndex
        return if (index <= lastIndex) spellFieldState.typedCharacters[index]
        else ""
    }


    Row(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        spellCheckState.forEachIndexed { index, spellBoxState ->
            SpellBox(
                text = getTypedCharacterOrEmpty(index),
                isNext = index == spellFieldState.indicatorPosition,
                state = spellBoxState,
                animationDelayMillis = index * 20,
                onFinishAnimation = {
                        if (spellCheckState.last() != SpellCheckState.Initial) {
                            onSpellCheckFinish.invoke()
                        }
                }
            )
        }
    }
}

@Composable
fun SpellBox(
    modifier: Modifier = Modifier,
    text: String,
    isNext: Boolean = false,
    state: SpellCheckState,
    animationDelayMillis: Int,
    onFinishAnimation: () -> Unit
) {

    val backgroundColor = animateColorAsState(
        targetValue = when (state) {
            SpellCheckState.Matched -> Color(0xFF538D4E)
            SpellCheckState.Unmatched -> Color(0xFF3A3A3C)
            else -> Color.Unspecified
        },
        animationSpec = tween(
            durationMillis = 195,
            delayMillis = animationDelayMillis,
            easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
        ),
        finishedListener = { onFinishAnimation.invoke() }
    )
    val borderColor = animateColorAsState(
        targetValue = if (isNext) Color(0xFFE0991A) else Color(0xFFA4A4A4),
        animationSpec = tween(
            durationMillis = 195,
            easing = CubicBezierEasing(0.61F, 1F, 0.88F, 1F)
        )
    )

    Box(
        modifier = modifier
            .padding(horizontal = 1.dp)
            .background(color = backgroundColor.value)
            .size(width = 32.dp, height = 40.dp)
            .border(
                width = 1.dp,
                color = borderColor.value
            ),
        contentAlignment = Alignment.Center
    ) {
        //TODO : animate the enterexit of the exit of text.
        Text(
            text = text,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}