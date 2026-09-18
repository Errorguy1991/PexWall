package com.pexwall.app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApi {

    @GET("search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("orientation") orientation: String? = null,
        @Query("size") size: String = "large",
        @Query("per_page") perPage: Int = 15,
        @Query("page") page: Int = 1
    ): PexelsResponse

    @GET("curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int = 15,
        @Query("page") page: Int = 1
    ): PexelsResponse
}
