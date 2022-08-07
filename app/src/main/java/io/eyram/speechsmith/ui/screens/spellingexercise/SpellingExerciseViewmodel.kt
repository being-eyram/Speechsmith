package io.eyram.speechsmith.ui.screens.spellingexercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.eyram.speechsmith.ui.components.SpellFieldState
import kotlin.random.Random

class SpellingExerciseScreenVM() : ViewModel() {

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))

    var uiState by mutableStateOf(
        SpellingExerciseScreenState(spellFieldState, keyboardLabels)
    )
        private set

    private val guessList = listOf(
        "ant", "cat", "hen",
        "dog", "horse", "mouse",
        "goat", "sheep", "koala"
    )

    private fun getWord() = guessList.random(Random(System.currentTimeMillis()))

    init {
        val wordToSpell = getWord()

        spellFieldState = SpellFieldState(wordToSpell)
        keyboardLabels = generateKeyboardLabels(wordToSpell)

        uiState = uiState.copy(
            spellFieldState = spellFieldState,
            keyboardLabels = keyboardLabels
        )
    }


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
        val nxtWordToSpell = getWord()
        spellFieldState = SpellFieldState(nxtWordToSpell)
        keyboardLabels = generateKeyboardLabels(nxtWordToSpell)

        uiState = uiState.copy(
            spellFieldState = spellFieldState,
            keyboardLabels = keyboardLabels
        )
        println(nxtWordToSpell)
    }
}

data class SpellingExerciseScreenState(
    val spellFieldState: SpellFieldState,
    val keyboardLabels: List<String>
)