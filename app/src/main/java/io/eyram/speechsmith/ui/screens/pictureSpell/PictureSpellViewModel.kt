package io.eyram.speechsmith.ui.screens.pictureSpell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldState
import javax.inject.Inject

@HiltViewModel
class PictureSpellViewModel @Inject constructor(private val repository: SpeechSmithRepository) :
    ViewModel() {

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))

    var uiState by mutableStateOf(PictureSpellScreenState(spellFieldState, keyboardLabels))
        private set

    init {

        val wordToSpell = repository.getWord()
        spellFieldState = SpellFieldState(wordToSpell)
        keyboardLabels = generateKeyboardLabels(wordToSpell)

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

    fun showNextWord() {
        repository.getWord()
            .apply {
                spellFieldState = SpellFieldState(this)
                keyboardLabels = generateKeyboardLabels(this)
            }.also {
                uiState = uiState.copy(
                    spellFieldState = spellFieldState,
                    keyboardLabels = keyboardLabels
                )
            }
    }

}

data class PictureSpellScreenState(
    val spellFieldState: SpellFieldState,
    val keyboardLabels: List<String>
)

const val NUM_OF_KEYBOARD_LABELS = 15