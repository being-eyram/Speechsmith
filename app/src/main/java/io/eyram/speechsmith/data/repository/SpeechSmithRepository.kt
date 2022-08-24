package io.eyram.speechsmith.data.repository

import io.eyram.speechsmith.data.network.DictionaryService
import javax.inject.Inject
import kotlin.random.Random

class SpeechSmithRepository @Inject constructor(private val dictionaryService: DictionaryService) {
    private val guessList = listOf("ant", "cat", "hen", "dog")
    fun getWord() = guessList.random(Random(System.currentTimeMillis()))
    suspend fun getPronunciation(word: String) = dictionaryService.getPronunciation(word)
}