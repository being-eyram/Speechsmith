package io.eyram.speechsmith.ui.screens.audioSpell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldState
import io.eyram.speechsmith.ui.screens.pictureSpell.NUM_OF_KEYBOARD_LABELS
import javax.inject.Inject

@HiltViewModel
class AudioSpellViewModel @Inject constructor(repository: SpeechSmithRepository) :
    ViewModel() {

    var uiState by mutableStateOf(AudioSpellScreenState())
        private set

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))


    init {
        val wordToSpell = repository.getWord()

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

        return mutableListOf<String>().run {
            addAll(charsToSpell.distinct())
            while (size < NUM_OF_KEYBOARD_LABELS) {
                val random = ('A'..'Z').random().toString()
                if (random !in this) add(random)
            }
            shuffled()
        }
    }


}

data class AudioSpellScreenState(
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val keyboardLabels: List<String> = listOf()
)