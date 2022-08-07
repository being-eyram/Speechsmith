package io.eyram.speechsmith.ui.screens.spellingexercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.eyram.speechsmith.ui.components.KeyboardEvents
import io.eyram.speechsmith.ui.components.SpellFieldState
import kotlin.random.Random

class SpellingExerciseScreenVM() : ViewModel() {

    private var wordToSpell by mutableStateOf("")
    private val guessList = listOf(
        "ant", "cat", "hen",
        "dog", "horse", "mouse",
        "goat", "sheep", "koala"
    )

    private fun getWord() = guessList.random(Random(System.currentTimeMillis()))

    init {
        wordToSpell = getWord()
    }


    private val spellFieldState = SpellFieldState(wordToSpell)
    private val keyboardEvents = KeyboardEvents(spellFieldState)
    private val keyboardUiState = generateKeyboardLabels(wordToSpell)

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
        wordToSpell = getWord()
    }
}

data class SpellingExerciseScreenState(
    val spellFieldState: SpellFieldState,
    val keyboardEvents: KeyboardEvents,
    val keyboardLabels: List<String>
)

//data class KeyboardUiState(val keyboardLabels: List<String>)