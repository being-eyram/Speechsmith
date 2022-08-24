package io.eyram.speechsmith.ui.screens.pictureSpell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.components.SpellFieldState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PictureSpellViewModel @Inject constructor(
    private val repository: SpeechSmithRepository
) : ViewModel() {

    var uiState by mutableStateOf(PictureSpellScreenState())
        private set

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))
    private var showFieldStateIndicator by mutableStateOf(false)

    init {
        val wordToSpell = repository.getWord()

        spellFieldState = SpellFieldState(wordToSpell)
        keyboardLabels = generateKeyboardLabels(wordToSpell)
        uiState = uiState.copy(
            spellFieldState = spellFieldState,
            keyboardLabels = keyboardLabels,
            showFieldStateIndicator = showFieldStateIndicator,
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

    private fun showNextWord() = repository.getWord().apply {
        spellFieldState = SpellFieldState(this)
        keyboardLabels = generateKeyboardLabels(this)
    }.also {
        uiState = uiState.copy(
            spellFieldState = spellFieldState,
            keyboardLabels = keyboardLabels
        )
    }

    fun onEnterPress() = spellFieldState.run {
        this.onEnterPress()

        if (isSpellInputFilled()) {
            if (isSpellingCorrect())
                SpellFieldInputState.Correct
            else
                SpellFieldInputState.Incorrect
        } else {
            SpellFieldInputState.InComplete
        }
    }.also {
        uiState = uiState.copy(spellFieldInputState = it)

        viewModelScope.launch {
            uiState = uiState.copy(showFieldStateIndicator = true)
            delay(1200)
            uiState = uiState.copy(showFieldStateIndicator = false)
        }
        viewModelScope.launch {
            delay(1000)
            if (it == SpellFieldInputState.Correct) showNextWord()
        }
    }
}


data class PictureSpellScreenState(
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val keyboardLabels: List<String> = listOf(),
    val showFieldStateIndicator: Boolean = false,
    val spellFieldInputState: SpellFieldInputState = SpellFieldInputState.InComplete
)

const val NUM_OF_KEYBOARD_LABELS = 15