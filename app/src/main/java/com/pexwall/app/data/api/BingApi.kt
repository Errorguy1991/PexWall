package com.pexwall.app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BingApi {
    @GET("HPImageArchive.aspx?format=js")
    suspend fun getDailyWallpapers(
        @Query("n") count: Int = 8,
        @Query("idx") index: Int = 0,
        @Query("mkt") market: String = "en-US"
    ): BingResponse
}
