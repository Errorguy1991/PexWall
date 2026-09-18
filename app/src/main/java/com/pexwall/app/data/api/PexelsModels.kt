package com.pexwall.app.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PexelsResponse(
    @Json(name = "total_results") val totalResults: Int = 0,
    val page: Int = 0,
    @Json(name = "per_page") val perPage: Int = 0,
    val photos: List<PexelsPhoto> = emptyList(),
    @Json(name = "next_page") val nextPage: String? = null
)

@JsonClass(generateAdapter = true)
data class PexelsPhoto(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    @Json(name = "photographer_url") val photographerUrl: String,
    @Json(name = "photographer_id") val photographerId: Int,
    @Json(name = "avg_color") val avgColor: String? = null,
    val src: PhotoSrc,
    val alt: String? = null
)

@JsonClass(generateAdapter = true)
data class PhotoSrc(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
