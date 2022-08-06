package io.eyram.speechsmith.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList


class SpellFieldState(wordToSpell: String) : KeyboardEventListener {

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
    var spellFieldUiState by mutableStateOf(
        SpellFieldUiState(
            typedCharacters = typedCharacters,
            spellCheckState = spellCheckState
        )
    )
        private set

    override fun onKeyPress(key: String) {
        if (typedCharacters.size < charsToSpell.size) {
            typedCharacters.add(key)
            spellFieldUiState = spellFieldUiState.copy(
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
            spellFieldUiState = spellFieldUiState.copy(
                spellBoxIndicatorPosition = typedCharacters.lastIndex + 1
            )
            // Reset background colors when backspace is pressed after enter press
            spellCheckState[typedCharacters.lastIndex + 1] = SpellCheckState.Initial
        }
    }
}

data class SpellFieldUiState(
    val typedCharacters: SnapshotStateList<String>,
    val spellBoxIndicatorPosition: Int = 0,
    val spellCheckState: SnapshotStateList<SpellCheckState>
)

enum class SpellCheckState {
    Initial,
    Matched,
    Unmatched,
}