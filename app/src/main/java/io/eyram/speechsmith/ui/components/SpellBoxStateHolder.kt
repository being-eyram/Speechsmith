package io.eyram.speechsmith.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList


class SpellFieldState(wordToSpell: String) {

    private val charsToSpell = wordToSpell.map {
        it.uppercaseChar().toString()
    }

    var indicatorPosition by mutableStateOf(0)
        private set

    var typedCharacters = mutableStateListOf<String>() //might move this to keyboard
        private set

    private val initSpellCheckState = MutableList(charsToSpell.size) { SpellCheckState.Initial }
    var spellCheckState = SnapshotStateList<SpellCheckState>().apply {
        addAll(initSpellCheckState)
    }
        private set

    fun onKeyPress(key: String) {
        if (typedCharacters.size < charsToSpell.size) {
            typedCharacters.add(key)
            indicatorPosition = typedCharacters.lastIndex + 1
        }
    }

    fun onEnterPress() {
        if (typedCharacters.size == charsToSpell.size) {
            charsToSpell.mapIndexed { idx, correctLetter ->
                when (correctLetter) {
                    typedCharacters[idx] -> spellCheckState[idx] = SpellCheckState.Matched
                    else -> spellCheckState[idx] = SpellCheckState.Unmatched
                }
            }
        }
    }

    fun onBackSpacePress() {
        if (typedCharacters.isNotEmpty()) {
            typedCharacters.removeLast()
            indicatorPosition = typedCharacters.lastIndex + 1
            spellCheckState[typedCharacters.lastIndex + 1] = SpellCheckState.Initial
        }
    }
}

enum class SpellCheckState {
    Initial,
    Matched,
    Unmatched,
}

//TODO : Use in conjuction with a stateflow to update screenstate
enum class SpellFieldInputState {
    Complete,
    Uncomplete
}