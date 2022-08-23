package io.eyram.speechsmith.ui.screens.audioSpell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.network.DictionaryService
import io.eyram.speechsmith.ui.components.SpellFieldState
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AudioSpellViewModel @Inject constructor(private val dictionaryService: DictionaryService) :
    ViewModel() {

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))

    var uiState by mutableStateOf(AudioSpellScreenState(spellFieldState, keyboardLabels))
        private set

    private val guessList = listOf("ant", "cat", "hen", "dog")

    private fun getWord() = guessList.random(Random(System.currentTimeMillis()))

    init {
        val wordToSpell = getWord()

        spellFieldState = SpellFieldState(wordToSpell)
        keyboardLabels = generateKeyboardLabels(wordToSpell)
        //getSound overhere, should add sound as field in uiState.

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

}

data class AudioSpellScreenState(
    val spellFieldState: SpellFieldState,
    val keyboardLabels: List<String>
)