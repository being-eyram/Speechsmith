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


data class KeyboardUiState(val keyboardCharacters: List<String>)