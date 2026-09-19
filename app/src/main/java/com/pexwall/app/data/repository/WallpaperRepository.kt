package com.pexwall.app.data.repository

import com.pexwall.app.data.api.PexelsApi
import com.pexwall.app.data.api.PexelsPhoto
import com.pexwall.app.data.db.WallpaperDao
import com.pexwall.app.data.db.WallpaperEntity
import com.pexwall.app.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.pexwall.app.data.api.BingApi
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.util.WallpaperSource
import kotlinx.coroutines.flow.first
import com.pexwall.app.data.api.PhotoSrc
import com.pexwall.app.data.api.UnsplashApi
import javax.inject.Singleton

@Singleton
class WallpaperRepository @Inject constructor(
    private val api: PexelsApi,
    private val bingApi: BingApi,
    private val unsplashApi: UnsplashApi,
    private val prefs: PreferencesManager,
    private val dao: WallpaperDao
) {

    fun getAllHistory(): Flow<List<WallpaperEntity>> = dao.getAllHistory()

    fun getHistoryByCategory(category: String): Flow<List<WallpaperEntity>> =
        dao.getHistoryByCategory(category)

    fun getLatestWallpaperFlow(): Flow<WallpaperEntity?> = dao.getLatestWallpaperFlow()

    suspend fun getLatestWallpaper(): WallpaperEntity? = dao.getLatestWallpaper()

    suspend fun fetchWallpapers(query: String, page: Int = 1, orientation: String = "portrait"): List<PexelsPhoto> {
        val response = api.searchPhotos(
            query = "$query desktop wallpaper 4k",
            orientation = if (orientation == "any") null else orientation,
            size = "large",
            perPage = Constants.DEFAULT_PER_PAGE,
            page = page
        )
        return response.photos
    }

    suspend fun fetchCuratedWallpapers(page: Int = 1): List<PexelsPhoto> {
        val response = api.getCuratedPhotos(
            perPage = Constants.DEFAULT_PER_PAGE,
            page = page
        )
        return response.photos
    }

    suspend fun getNextWallpaper(
        selectedCategories: Set<String>,
        isRandom: Boolean,
        orientation: String,
        excludeIds: Set<Long> = emptySet()
    ): PexelsPhoto? {
        val source = prefs.wallpaperSource.first()
        val usedIds = dao.getAllUsedPhotoIds().toSet() + excludeIds

        if (source == WallpaperSource.BING) {
            return try {
                val bingMarketsToUse = if (isRandom || selectedCategories.isEmpty()) {
                    listOf(Constants.BING_MARKETS.random().id)
                } else {
                    Constants.BING_MARKETS.filter { it.id in selectedCategories }.map { it.id }
                }
                val market = bingMarketsToUse.randomOrNull() ?: "en-US"
                val response = bingApi.getDailyWallpapers(market = market)
                val photos = response.images.map { bingImage ->
                    val fullUrl = "https://www.bing.com" + bingImage.urlbase + "_UHD.jpg"
                    PexelsPhoto(
                        id = bingImage.urlbase.hashCode().toLong(),
                        width = 3840,
                        height = 2160,
                        url = fullUrl,
                        photographer = bingImage.copyright,
                        photographerUrl = "",
                        photographerId = 0,
                        avgColor = "#000000",
                        src = PhotoSrc(
                            original = fullUrl,
                            large2x = fullUrl,
                            large = fullUrl,
                            medium = fullUrl,
                            small = fullUrl,
                            portrait = fullUrl,
                            landscape = fullUrl,
                            tiny = fullUrl
                        ),
                        
                        alt = bingImage.title
                    )
                }
                photos.firstOrNull { it.id !in usedIds } ?: photos.randomOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        if (source == WallpaperSource.UNSPLASH) {
                val unsplashCategoriesToUse = if (isRandom || selectedCategories.isEmpty()) {
                    listOf(Constants.UNSPLASH_CATEGORIES.random().id)
                } else {
                    Constants.UNSPLASH_CATEGORIES.filter { it.id in selectedCategories }.map { it.id }
                }
                val query = unsplashCategoriesToUse.randomOrNull() ?: "wallpaper"
                val rawKey = com.pexwall.app.util.ApiKeyHolder.unsplashApiKey.ifEmpty { Constants.UNSPLASH_ACCESS_KEY }
                val response = unsplashApi.getRandomPhoto(
                    authHeader = "Client-ID $rawKey",
                    query = query,
                    orientation = if (orientation == "any") "squarish" else orientation
                )
                
                val fullUrl = response.urls.raw
                val photo = PexelsPhoto(
                    id = response.id.hashCode().toLong(),
                    width = 3840,
                    height = 2160,
                    url = fullUrl,
                    photographer = response.user.name,
                    photographerUrl = response.user.links.html,
                    photographerId = response.user.name.hashCode().toLong(),
                    avgColor = response.color ?: "#000000",
                    src = PhotoSrc(
                        original = response.urls.raw,
                        large2x = response.urls.full,
                        large = response.urls.regular,
                        medium = response.urls.regular,
                        small = response.urls.small,
                        portrait = response.urls.regular,
                        landscape = response.urls.regular,
                        tiny = response.urls.thumb
                    ),
                    alt = "Unsplash Photo by " + response.user.name
                )
                return photo
        }

        val categoriesToUse = if (isRandom || selectedCategories.isEmpty()) {
            listOf(Constants.CATEGORIES.random())
        } else {
            Constants.CATEGORIES.filter { it.id in selectedCategories }
        }

        // Try each selected category until we find an unused photo
        for (category in categoriesToUse.shuffled()) {
            var page = 1
            val maxPages = 5

            while (page <= maxPages) {
                val photos = fetchWallpapers(category.searchQuery, page, orientation)
                if (photos.isEmpty()) { if (page > 1) { page = 1; continue } else break }

                val unusedPhoto = photos.firstOrNull { it.id !in usedIds }
                if (unusedPhoto != null) return unusedPhoto

                page++
            }
        }

        // Fallback: try curated photos
        val curatedPhotos = fetchCuratedWallpapers()
        return curatedPhotos.firstOrNull { it.id !in usedIds } ?: curatedPhotos.firstOrNull()
    }

    suspend fun saveToHistory(photo: PexelsPhoto, category: String) {
        val entity = WallpaperEntity(
            pexelsPhotoId = photo.id,
            imageUrl = photo.src.original,
            thumbnailUrl = photo.src.large2x,
            photographer = photo.photographer,
            photographerUrl = photo.photographerUrl,
            category = category,
            avgColor = photo.avgColor
        )
        dao.insertWallpaper(entity)
    }

    suspend fun pruneOldHistory() {
        val cutoff = System.currentTimeMillis() -
                (Constants.HISTORY_RETENTION_DAYS * 24 * 60 * 60 * 1000L)
        dao.deleteOlderThan(cutoff)
    }
}
