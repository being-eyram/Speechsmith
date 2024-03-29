package io.eyram.speechsmith.data.repository

import io.eyram.speechsmith.data.network.DictionaryService
import io.eyram.speechsmith.data.network.UnsplashService
import retrofit2.http.Query
import javax.inject.Inject
import kotlin.random.Random

class SpeechSmithRepository @Inject constructor(
    private val dictionaryService: DictionaryService,
    private val unsplashService: UnsplashService
) {
    private val guessList = listOf(
        "Buffalo", "Bull", "Camel", "Donkey",
        "Cat", "Chicken", "Cow", "Deer", "Dog",
        "Dove", "Duck", "Fish", "Goat", "Goose",
        "Hamster", "Hen", "Horse", "Llama", "Mule",
        "Ostrich", "Ox", "Parrot", "Pig", "Rabbit", "Sheep",
    )

    fun getWord() = guessList.random(Random(System.currentTimeMillis()))
    suspend fun getPronunciation(word: String) = dictionaryService.getPronunciation(word)
    suspend fun getImage(imgQuery: String) = unsplashService.getImage(imgQuery)


    fun getWordsToSpell(totalNumber: Int) = mutableListOf<String>().run {
        while (size < totalNumber) {
            getWord().also {
                if (it !in this) add(it)
            }
        }
        this
    }
}