package io.eyram.speechsmith.network

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

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