package io.eyram.speechsmith.data.network

import retrofit2.Response
import retrofit2.http.GET

interface DictionaryService {
    @GET("jokes/random")
    suspend fun getJoke(): Response<Joke>
}


data class Joke(
    val icon_url: String,
    val id: String,
    val url: String,
    val value: String
)