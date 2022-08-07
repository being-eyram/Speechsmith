package io.eyram.speechsmith.ui.screens.spellingexercise

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.eyram.speechsmith.ui.components.SpellFieldState
import kotlin.random.Random

class SpellingExerciseScreenVM() : ViewModel() {

    var uiState: MutableState<SpellingExerciseScreenState>
        private set

    private var wordToSpell by mutableStateOf("")
    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels: List<String>

    private val guessList = listOf(
        "ant", "cat", "hen",
        "dog", "horse", "mouse",
        "goat", "sheep", "koala"
    )

    private fun getWord() = guessList.random(Random(System.currentTimeMillis()))

    init {
        wordToSpell = getWord()

        spellFieldState = SpellFieldState(wordToSpell)
        keyboardLabels = generateKeyboardLabels(wordToSpell)

        uiState = mutableStateOf(
            SpellingExerciseScreenState(
                spellFieldState,
                keyboardLabels
            )
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
        wordToSpell = getWord()
        println(wordToSpell)
    }
}

data class SpellingExerciseScreenState(
    val spellFieldState: SpellFieldState,
    val keyboardLabels: List<String>
)