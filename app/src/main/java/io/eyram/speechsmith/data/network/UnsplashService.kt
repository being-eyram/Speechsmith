package io.eyram.speechsmith.data.network

import com.skydoves.sandwich.ApiResponse
import io.eyram.speechsmith.BuildConfig
import io.eyram.speechsmith.data.model.image.Image
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    @GET("search/photos?client_id=${BuildConfig.UNSPLASH_API_KEY}")
    suspend fun getImage(@Query("query") imgQuery: String): ApiResponse<Image>
}