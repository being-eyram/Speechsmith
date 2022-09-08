package io.eyram.speechsmith.util

import androidx.compose.ui.graphics.Color
import io.eyram.speechsmith.R
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.screens.pictureSpell.CORRECT
import io.eyram.speechsmith.ui.screens.pictureSpell.INCOMPLETE
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellInputVisualIndicatorState
import io.eyram.speechsmith.ui.screens.pictureSpell.WRONG

fun generateKeyboardLabels(wordToSpell: String): List<String> {
    val charsToSpell = wordToSpell.map { it.uppercaseChar().toString() }

    return mutableListOf<String>().run {
        addAll(charsToSpell.distinct())
        while (size < NUM_OF_KEYBOARD_LABELS) {
            val random = ('A'..'Z').random().toString()
            if (random !in this) add(random)
        }
        shuffled()
    }
}

const val NUM_OF_KEYBOARD_LABELS = 15

fun getVisualIndicatorState(state: SpellFieldInputState): SpellInputVisualIndicatorState {
    return when (state) {

        SpellFieldInputState.Correct -> SpellInputVisualIndicatorState(
            color = Color(0xFF538D4E),
            message = CORRECT,
            icon = R.drawable.ic_correct
        )
        SpellFieldInputState.Incorrect -> SpellInputVisualIndicatorState(
            color = Color(0xFFBF4040),
            message = WRONG,
            icon = R.drawable.ic_incorrect
        )
        SpellFieldInputState.InComplete -> SpellInputVisualIndicatorState(
            color = Color(0xFF3A3A3C),
            message = INCOMPLETE,
            icon = R.drawable.ic_incomplete
        )
    }
}

val defaultViState =
    SpellInputVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)