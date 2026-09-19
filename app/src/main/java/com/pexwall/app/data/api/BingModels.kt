package com.pexwall.app.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BingResponse(
    val images: List<BingImage> = emptyList()
)

@JsonClass(generateAdapter = true)
data class BingImage(
    val urlbase: String,
    val copyright: String,
    val title: String,
    val startdate: String
)
