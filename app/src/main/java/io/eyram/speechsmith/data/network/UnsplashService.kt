package io.eyram.speechsmith.data.network

import com.skydoves.sandwich.ApiResponse
import io.eyram.speechsmith.data.model.image.Pexels
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UnsplashService {

    @GET("search")
    suspend fun getImage(
        @Query("query") imgQuery: String,
        @Header("Authorization") auth : String = "oXQ8mQX6oiviidZmieCeKf20DYo2hJuOa9eH35JRCODSv3FOQsHqxpGb"
    ): ApiResponse<Pexels>
}