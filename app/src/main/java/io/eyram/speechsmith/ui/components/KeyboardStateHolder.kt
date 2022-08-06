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

class KeyboardEvents(private val keyboardEventListener: KeyboardEventListener) {

    fun onKeyPress(key: String) = keyboardEventListener.onKeyPress(key)

    fun onBackSpacePress() = keyboardEventListener.onBackSpacePress()

    fun onEnterPress() = keyboardEventListener.onEnterPress()
}