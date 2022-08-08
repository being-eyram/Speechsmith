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

    var charsToDisplay = mutableStateListOf<String>() //might move this to keyboard
        private set

    var inputFieldState by mutableStateOf<SpellFieldInputState>(InputStateInComplete)
        private set

    private val initCharMatchList = MutableList(charsToSpell.size) { CharMatchState.Initial }
    var charMatchList = SnapshotStateList<CharMatchState>().apply {
        addAll(initCharMatchList)
    }
        private set


    fun onKeyPress(key: String) {
        if (charsToDisplay.size < charsToSpell.size) {
            charsToDisplay.add(key)
            indicatorPosition = charsToDisplay.lastIndex + 1
        }
    }

    fun onEnterPress() {

        inputFieldState = when {
            isSpellInputFilled() -> {
                matchMaker()
                if (isAllMatchedUp())
                    InputStateComplete.Correct
                else
                    InputStateComplete.InCorrect
            }
            else -> InputStateInComplete
        }
        println(inputFieldState)
    }


    fun onBackSpacePress() {
        if (charsToDisplay.isNotEmpty()) {
            charsToDisplay.removeLast()
            indicatorPosition = charsToDisplay.lastIndex + 1
            charMatchList[charsToDisplay.lastIndex + 1] = CharMatchState.Initial
        }
    }

    private fun matchMaker() {
        charsToDisplay.mapIndexed { idx, inputChar ->
            val isMatched = inputChar == charsToSpell[idx]
            if (isMatched) {
                charMatchList[idx] = CharMatchState.Matched
            } else {
                charMatchList[idx] = CharMatchState.Unmatched
            }
        }
    }

    private fun isAllMatchedUp() = charMatchList.all { it == CharMatchState.Matched }
    private fun isSpellInputFilled() = charsToDisplay.size == charsToSpell.size
}

enum class CharMatchState {
    Initial,
    Matched,
    Unmatched,
}

//TODO : Use in conjuction with a stateflow to update screenstate
sealed class SpellFieldInputState

sealed class InputStateComplete() : SpellFieldInputState() {
    object Correct : InputStateComplete()
    object InCorrect : InputStateComplete()
}

object InputStateInComplete : SpellFieldInputState()