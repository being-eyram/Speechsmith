package io.eyram.speechsmith.data.network

import com.skydoves.sandwich.ApiResponse
import io.eyram.speechsmith.BuildConfig
import io.eyram.speechsmith.data.model.Pronunciation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface DictionaryService {

   @GET("{word}/audio?useCanonical=false&limit=1&api_key=hello")
    suspend fun getPronunciation(@Path("word") word: String): ApiResponse<List<Pronunciation>>
}