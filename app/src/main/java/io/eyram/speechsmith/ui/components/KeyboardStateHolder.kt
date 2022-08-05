package io.eyram.speechsmith.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

interface KeyboardEventListener {
    fun onKeyPress(key: String)
    fun onEnterPress()
    fun onBackSpacePress()
}

class Keyboard(private val keyboardEventListener: KeyboardEventListener) {

    companion object {
        fun generateKeyboardLabels(wordToSpell: String): List<String> {
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


    fun onKeyPress(key: String) = keyboardEventListener.onKeyPress(key)

    fun onBackSpacePress() = keyboardEventListener.onBackSpacePress()

    fun onEnterPress() = keyboardEventListener.onEnterPress()
}

class SpellBoxState(wordToSpell: String) : KeyboardEventListener {
    private val charsToSpell = wordToSpell.map { it.uppercaseChar().toString() }
    private val typedCharacters = mutableStateListOf<String>()
    private val spellCheckState = SnapshotStateList<SpellCheckState>().apply {
        addAll(
            MutableList(
                size = charsToSpell.size,
                init = { SpellCheckState.Initial }
            )
        )
    }
    var spellBoxUiState by mutableStateOf(
        SpellBoxUiState(
            typedCharacters = typedCharacters,
            spellCheckState = spellCheckState
        )
    )
        private set

    override fun onKeyPress(key: String) {
        if (typedCharacters.size < charsToSpell.size) {
            typedCharacters.add(key)
            spellBoxUiState = spellBoxUiState.copy(
                spellBoxIndicatorPosition = typedCharacters.lastIndex + 1
            )
        }
    }

    override fun onEnterPress() {
        if (typedCharacters.size == charsToSpell.size) {
            charsToSpell.mapIndexed { index, correctLetter ->
                when (correctLetter) {
                    typedCharacters[index] -> spellCheckState[index] = SpellCheckState.Matched
                    else -> spellCheckState[index] = SpellCheckState.Unmatched
                }
            }
        }
    }

    override fun onBackSpacePress() {
        if (typedCharacters.isNotEmpty()) {
            typedCharacters.removeLast()
            spellBoxUiState = spellBoxUiState.copy(
                spellBoxIndicatorPosition = typedCharacters.lastIndex + 1
            )
            // Reset background colors when backspace is pressed after enter press
            spellCheckState[typedCharacters.lastIndex + 1] = SpellCheckState.Initial
        }
    }
}

data class KeyboardUiState(val keyboardCharacters: List<String>)

data class SpellBoxUiState(
    val typedCharacters: SnapshotStateList<String>,
    val spellBoxIndicatorPosition: Int = 0,
    val spellCheckState: SnapshotStateList<SpellCheckState>
)