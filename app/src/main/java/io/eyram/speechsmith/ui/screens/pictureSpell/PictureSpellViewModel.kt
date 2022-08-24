package io.eyram.speechsmith.ui.screens.pictureSpell

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.R
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

data class PictureSpellScreenState(
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val keyboardLabels: List<String> = listOf(),
    val showFieldStateIndicator: Boolean = false,
    val visualIndicatorState: SpellInputStateVisualIndicatorState =
        SpellInputStateVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)
)


data class SpellInputStateVisualIndicatorState(
    val color: Color,
    val message: String,
    @DrawableRes val icon: Int
)

const val NUM_OF_KEYBOARD_LABELS = 15
const val CORRECT = "CORRECT"
const val WRONG = "WRONG"
const val INCOMPLETE = "INCOMPLETE"