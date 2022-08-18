package io.eyram.speechsmith.ui.components

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList


class SpellFieldState(wordToSpell: String) {

    private val charsToSpell = wordToSpell.map { it.uppercaseChar().toString() }

    var charsToDisplay = mutableStateListOf<String>()
        private set

    val indicatorPosition by derivedStateOf{charsToDisplay.lastIndex + 1}

    var charMatchList = SnapshotStateList<CharMatchState>().apply {
        val init = List(charsToSpell.size) { CharMatchState.Initial }
        addAll(init)
    }
        private set


    fun onKeyPress(key: String) {
        if (charsToDisplay.size < charsToSpell.size) {
            charsToDisplay.add(key)
        }
    }

    fun onEnterPress() {
        if(charsToDisplay.size == charsToSpell.size){
            charsToDisplay.mapIndexed { idx, inputChar ->
                if ( inputChar == charsToSpell[idx]) {
                    charMatchList[idx] = CharMatchState.Matched
                } else {
                    charMatchList[idx] = CharMatchState.Unmatched
                }
            }
        }
    }

    fun onBackSpacePress() {
        if (charsToDisplay.isNotEmpty()) {
            charsToDisplay.removeLast()
            charMatchList[charsToDisplay.lastIndex + 1] = CharMatchState.Initial
        }
    }

    fun isSpellingCorrect() = charMatchList.all { it == CharMatchState.Matched }
    fun isSpellInputFilled() = charsToDisplay.size == charsToSpell.size
}

enum class CharMatchState {
    Initial,
    Matched,
    Unmatched,
}

//TODO : Use in conjuction with a stateflow to update screenstate
enum class SpellFieldInputState{
    Correct,
    Incorrect,
    InComplete
}
