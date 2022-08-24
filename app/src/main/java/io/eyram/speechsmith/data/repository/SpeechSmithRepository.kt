package io.eyram.speechsmith.data.repository

import io.eyram.speechsmith.data.network.DictionaryService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

class SpeechSmithRepository @Inject constructor() {
    private val guessList = listOf("ant", "cat", "hen", "dog")
    fun getWord() = guessList.random(Random(System.currentTimeMillis()))
}