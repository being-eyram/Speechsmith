package io.eyram.speechsmith.ui.screens.audioSpell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.R
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.components.SpellFieldState
import io.eyram.speechsmith.ui.screens.pictureSpell.CORRECT
import io.eyram.speechsmith.ui.screens.pictureSpell.INCOMPLETE
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellInputStateVisualIndicatorState
import io.eyram.speechsmith.ui.screens.pictureSpell.WRONG
import io.eyram.speechsmith.util.generateKeyboardLabels
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioSpellViewModel @Inject constructor(private val repository: SpeechSmithRepository) :
    ViewModel() {

    var uiState by mutableStateOf(AudioSpellScreenState())
        private set

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))

    init {
       showNextWord()
    }

    private fun updateAudio(wordToSpell: String) {
        viewModelScope.launch {
            try {
                val word = wordToSpell.toLowerCase(Locale.current)
                repository.getPronunciation(word).apply {
                    if (isSuccessful) {
                        body()?.let {
                            uiState = uiState.copy(audioUrl = it[0].fileUrl)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun updateKeyboard(wordToSpell: String) {
        keyboardLabels = generateKeyboardLabels(wordToSpell)
        uiState = uiState.copy(keyboardLabels = keyboardLabels)
    }

    private fun updateSpellField(wordToSpell: String) {
        spellFieldState = SpellFieldState(wordToSpell)
        uiState = uiState.copy(spellFieldState = spellFieldState)
    }


    private fun showNextWord() = repository.getWord().apply {
        updateAudio(this)
        updateSpellField(this)
        updateKeyboard(this)
    }

    fun onEnterPress() {
        spellFieldState.run {
            spellCheck()
            getSpellFieldInputState()
        }.also {
            val visualIndicatorState = getVisualIndicatorState(it)
            uiState = uiState.copy(visualIndicatorState = visualIndicatorState)

            viewModelScope.launch {
                uiState = uiState.copy(showFieldStateIndicator = true)
                delay(1500)
                uiState = uiState.copy(showFieldStateIndicator = false)
            }
            viewModelScope.launch {
                delay(1000)
                if (it == SpellFieldInputState.Correct) showNextWord()
            }
        }
    }

    private fun getVisualIndicatorState(state: SpellFieldInputState): SpellInputStateVisualIndicatorState {
        return when (state) {

            SpellFieldInputState.Correct -> SpellInputStateVisualIndicatorState(
                color = Color(0xFF538D4E),
                message = CORRECT,
                icon = R.drawable.ic_correct
            )
            SpellFieldInputState.Incorrect -> SpellInputStateVisualIndicatorState(
                color = Color(0xFFBF4040),
                message = WRONG,
                icon = R.drawable.ic_incorrect
            )
            SpellFieldInputState.InComplete -> SpellInputStateVisualIndicatorState(
                color = Color(0xFF3A3A3C),
                message = INCOMPLETE,
                icon = R.drawable.ic_incomplete
            )
        }
    }
}

data class AudioSpellScreenState(
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val keyboardLabels: List<String> = listOf(),
    val audioUrl: String = "",
    val showFieldStateIndicator: Boolean = false,
    val visualIndicatorState: SpellInputStateVisualIndicatorState =
        SpellInputStateVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)
)