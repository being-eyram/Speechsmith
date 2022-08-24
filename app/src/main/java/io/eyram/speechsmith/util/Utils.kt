package io.eyram.speechsmith.util

fun generateKeyboardLabels(wordToSpell: String): List<String> {
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

const val NUM_OF_KEYBOARD_LABELS = 15