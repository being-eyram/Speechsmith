package io.eyram.speechsmith.ui.screens.spellingexercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.eyram.speechsmith.ui.components.KeyboardEvents
import io.eyram.speechsmith.ui.components.SpellFieldState
import kotlin.random.Random

class SpellingExerciseScreenVM() : ViewModel() {
    private val guessList = listOf(
        "chimpanzee",
        "mongoose",
        "salamander",
        "koala"
    )

    private val wts = guessList.random(Random(System.currentTimeMillis()))

    private var wordToSpell by mutableStateOf(wts)
    private val spellFieldState = SpellFieldState(wordToSpell)
    private val keyboardEvents = KeyboardEvents(spellFieldState)
    private val keyboardUiState = KeyboardUiState(generateKeyboardLabels(wordToSpell))

    val uiState = SpellingExerciseScreenState(
        spellFieldState,
        keyboardEvents,
        keyboardUiState
    )

    private fun generateKeyboardLabels(wordToSpell: String): List<String> {
        val charsToSpell = wordToSpell.map { it.uppercaseChar().toString() }
        val keyboardLabelsFromWord = mutableListOf<String>().apply {
            addAll(charsToSpell.distinct())
        }
        while (keyboardLabelsFromWord.size < 15) {
            val random = ('A'..'Z').random().toString()
            if (random !in keyboardLabelsFromWord) keyboardLabelsFromWord.add(random)
        }

        return keyboardLabelsFromWord.shuffled()
    }

    fun showNextWord() {
        wordToSpell = wts
    }
}

data class SpellingExerciseScreenState(
    val spellFieldState: SpellFieldState,
    val keyboardEvents: KeyboardEvents,
    val keyboardUiState: KeyboardUiState
)

data class KeyboardUiState(val keyboardLabels: List<String>)