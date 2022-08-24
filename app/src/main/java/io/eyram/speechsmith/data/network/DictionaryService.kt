package io.eyram.speechsmith.data.network

import io.eyram.speechsmith.BuildConfig
import io.eyram.speechsmith.data.model.Pronunciation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface DictionaryService {
    @Headers("useCanonical=false&limit=1&api_key=${BuildConfig.API_KEY}")
    @GET("{word}/audio")
    suspend fun getPronunciation(@Path("word") word: String): Response<List<Pronunciation>>
}