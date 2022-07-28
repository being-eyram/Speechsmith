package io.eyram.speechsmith.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList


class KeyboardState() {

    private val wordsToSpell = listOf(
        "puppy", "raccoon", "dromedary", "oryx",
        "fox", "dingo", "dung beetle", "jackal",
        "anteater", "okapi", "lizard", "dormouse",
        "walrus", "gorilla",
    )
    private val typedCharacters = mutableStateListOf<String>()
    private val wordToSpell = wordsToSpell[1].map {
        it.uppercaseChar().toString()
    }
    private val spellCheckState = SnapshotStateList<SpellCheckState>().apply {
        addAll(MutableList(size = wordToSpell.size, init = { Initial }))
    }

    var keyboardUiState by mutableStateOf(
        KeyboardUiState(
            keyboardCharacters = generateKeyboardLabels(),
            typedCharacters = typedCharacters,
            spellBoxIndicatorPosition = 0,
            spellCheckState = spellCheckState
        )
    )
        private set


    private fun generateKeyboardLabels(): List<String> {
        val keyboardLabelsFromWord = mutableListOf<String>().apply {
            addAll(wordToSpell.distinct())
        }
        while (keyboardLabelsFromWord.size < 15) {
            val random = ('A'..'Z').random().toString()
            if (random !in keyboardLabelsFromWord) keyboardLabelsFromWord.add(random)
        }

        return keyboardLabelsFromWord.shuffled()
    }

    fun onKeyBoardKeyPress(key: String) {
        if (typedCharacters.size < wordToSpell.size) {
            typedCharacters.add(key)
            keyboardUiState = keyboardUiState.copy(
                spellBoxIndicatorPosition = typedCharacters.lastIndex + 1
            )
        }
    }

    fun onBackSpacePress() {
        if (typedCharacters.isNotEmpty()) {
            typedCharacters.removeLast()
            keyboardUiState =
                keyboardUiState.copy(spellBoxIndicatorPosition = typedCharacters.lastIndex + 1)
            // Reset background colors when backspace is pressed after enter press
            spellCheckState[typedCharacters.lastIndex + 1] = Initial
        }
    }

    fun onEnterPress() {
        val wordToSpellCharacterList = wordToSpell

        if (typedCharacters.size == wordToSpellCharacterList.size) {
            wordToSpellCharacterList
                .mapIndexed { index , correctLetter->
                    when (correctLetter) {
                        typedCharacters[index] -> spellCheckState[index] = Matched
                        else -> spellCheckState[index] = Unmatched
                    }
                }
        }
    }
}