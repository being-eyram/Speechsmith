package io.eyram.speechsmith.data.model.image


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class Image(
    val results: List<Result>,
    val total: Int,
    @Json(name = "total_pages")
    val totalPages: Int
)





//Result from unsplash api and it's props. Not really neccesary.

data class Result(
    @Json(name = "alt_description")
    val altDescription: String,
    @Json(name = "blur_hash")
    val blurHash: String,
    val categories: List<Any>,
    val color: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "current_user_collections")
    val currentUserCollections: List<Any>,
    val description: Any,
    val height: Int,
    val id: String,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    val likes: Int,
    val links: Links,
    @Json(name = "promoted_at")
    val promotedAt: Any,
    val sponsorship: Any,
    val tags: List<Any>,
    @Json(name = "topic_submissions")
    val topicSubmissions: TopicSubmissions,
    @Json(name = "updated_at")
    val updatedAt: String,
    val urls: Urls,
    val user: User,
    val width: Int
) {

    data class Links(
        val download: String,
        @Json(name = "download_location")
        val downloadLocation: String,
        val html: String,
        val self: String
    )

    class TopicSubmissions

    data class Urls(
        val full: String,
        val raw: String,
        val regular: String,
        val small: String,
        @Json(name = "small_s3")
        val smallS3: String,
        val thumb: String
    )

    data class User(
        @Json(name = "accepted_tos")
        val acceptedTos: Boolean,
        val bio: Any,
        @Json(name = "first_name")
        val firstName: String,
        @Json(name = "for_hire")
        val forHire: Boolean,
        val id: String,
        @Json(name = "instagram_username")
        val instagramUsername: String,
        @Json(name = "last_name")
        val lastName: String,
        val links: Links,
        val location: Any,
        val name: String,
        @Json(name = "portfolio_url")
        val portfolioUrl: String,
        @Json(name = "profile_image")
        val profileImage: ProfileImage,
        val social: Social,
        @Json(name = "total_collections")
        val totalCollections: Int,
        @Json(name = "total_likes")
        val totalLikes: Int,
        @Json(name = "total_photos")
        val totalPhotos: Int,
        @Json(name = "twitter_username")
        val twitterUsername: Any,
        @Json(name = "updated_at")
        val updatedAt: String,
        val username: String
    ) {

        data class Links(
            val followers: String,
            val following: String,
            val html: String,
            val likes: String,
            val photos: String,
            val portfolio: String,
            val self: String
        )

        data class ProfileImage(
            val large: String,
            val medium: String,
            val small: String
        )

        data class Social(
            @Json(name = "instagram_username")
            val instagramUsername: String,
            @Json(name = "paypal_email")
            val paypalEmail: Any,
            @Json(name = "portfolio_url")
            val portfolioUrl: String,
            @Json(name = "twitter_username")
            val twitterUsername: Any
        )
    }
}