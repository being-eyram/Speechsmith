package io.eyram.speechsmith.data.model

data class Pronunciation(
    val attributionText: String,
    val attributionUrl: String,
    val audioType: String,
    val commentCount: Int,
    val createdAt: String,
    val createdBy: String,
    val description: String,
    val duration: Float,
    val fileUrl: String,
    val id: Int,
    val voteAverage: Int,
    val voteCount: Int,
    val voteWeightedAverage: Int,
    val word: String
)