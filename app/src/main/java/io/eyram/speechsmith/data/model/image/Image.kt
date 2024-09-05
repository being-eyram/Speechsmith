package io.eyram.speechsmith.data.model.image


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class Pexels(
    @Json(name = "next_page")
    val nextPage: String = "",
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "per_page")
    val perPage: Int = 0,
    @Json(name = "photos")
    val photos: List<Photo> = listOf(),
    @Json(name = "total_results")
    val totalResults: Int = 0
)

data class Photo(
    @Json(name = "alt")
    val alt: String = "",
    @Json(name = "avg_color")
    val avgColor: String = "",
    @Json(name = "height")
    val height: Int = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "liked")
    val liked: Boolean = false,
    @Json(name = "photographer")
    val photographer: String = "",
    @Json(name = "photographer_id")
    val photographerId: Int = 0,
    @Json(name = "photographer_url")
    val photographerUrl: String = "",
    @Json(name = "src")
    val src: Src = Src(),
    @Json(name = "url")
    val url: String = "",
    @Json(name = "width")
    val width: Int = 0
)

data class Src(
    @Json(name = "landscape")
    val landscape: String = "",
    @Json(name = "large")
    val large: String = "",
    @Json(name = "large2x")
    val large2x: String = "",
    @Json(name = "medium")
    val medium: String = "",
    @Json(name = "original")
    val original: String = "",
    @Json(name = "portrait")
    val portrait: String = "",
    @Json(name = "small")
    val small: String = "",
    @Json(name = "tiny")
    val tiny: String = ""
)