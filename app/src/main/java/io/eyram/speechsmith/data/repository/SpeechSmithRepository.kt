package io.eyram.speechsmith.data.repository

import javax.inject.Inject
import kotlin.random.Random

class SpeechSmithRepository @Inject constructor() {
    private val guessList = listOf("ant", "cat", "hen", "dog")
    fun getWord() = guessList.random(Random(System.currentTimeMillis()))
}