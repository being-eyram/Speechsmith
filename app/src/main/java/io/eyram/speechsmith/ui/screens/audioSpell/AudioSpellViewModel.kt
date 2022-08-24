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
import io.eyram.speechsmith.data.network.DictionaryService
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.components.SpellFieldState
import io.eyram.speechsmith.ui.screens.pictureSpell.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AudioSpellViewModel @Inject constructor(private val repository: SpeechSmithRepository) :
    ViewModel() {

    var uiState by mutableStateOf(AudioSpellScreenState())
        private set

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))



    init {
        val wordToSpell = repository.getWord()

       val deferredResult = viewModelScope.async{
            runCatching {
                repository.getPronunciation(wordToSpell.toLowerCase(Locale.current))
            }
        }

        viewModelScope.launch {
           val result = deferredResult.await()
            if(result.isSuccess){
                result.getOrNull()?.apply {
                    body()?.get(0)?.fileUrl
                }
            }
        }


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

data class AudioSpellScreenState(
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val keyboardLabels: List<String> = listOf(),
    val showFieldStateIndicator: Boolean = false,
    val visualIndicatorState: SpellInputStateVisualIndicatorState =
        SpellInputStateVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)
)