package com.pexwall.app.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashUrls,
    val user: UnsplashUser,
    val color: String?
)

@JsonClass(generateAdapter = true)
data class UnsplashUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

@JsonClass(generateAdapter = true)
data class UnsplashUser(
    val name: String,
    val links: UnsplashUserLinks
)

@JsonClass(generateAdapter = true)
data class UnsplashUserLinks(
    val html: String
)

interface UnsplashApi {
    @GET("photos/random")
    suspend fun getRandomPhoto(
        @retrofit2.http.Header("Authorization") authHeader: String,
        @Query("query") query: String,
        @Query("orientation") orientation: String = "portrait"
    ): UnsplashPhoto
}
