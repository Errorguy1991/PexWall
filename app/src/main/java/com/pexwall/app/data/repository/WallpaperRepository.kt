package com.pexwall.app.data.repository

import com.pexwall.app.data.api.PexelsApi
import com.pexwall.app.data.api.PexelsPhoto
import com.pexwall.app.data.db.WallpaperDao
import com.pexwall.app.data.db.WallpaperEntity
import com.pexwall.app.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperRepository @Inject constructor(
    private val api: PexelsApi,
    private val dao: WallpaperDao
) {

    fun getAllHistory(): Flow<List<WallpaperEntity>> = dao.getAllHistory()

    fun getHistoryByCategory(category: String): Flow<List<WallpaperEntity>> =
        dao.getHistoryByCategory(category)

    fun getLatestWallpaperFlow(): Flow<WallpaperEntity?> = dao.getLatestWallpaperFlow()

    suspend fun getLatestWallpaper(): WallpaperEntity? = dao.getLatestWallpaper()

    suspend fun fetchWallpapers(query: String, page: Int = 1, orientation: String = "portrait"): List<PexelsPhoto> {
        return try {
            val response = api.searchPhotos(
                query = "$query desktop wallpaper 4k",
                orientation = if (orientation == "any") null else orientation,
                size = "large",
                perPage = Constants.DEFAULT_PER_PAGE,
                page = page
            )
            response.photos
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun fetchCuratedWallpapers(page: Int = 1): List<PexelsPhoto> {
        return try {
            val response = api.getCuratedPhotos(
                perPage = Constants.DEFAULT_PER_PAGE,
                page = page
            )
            response.photos
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getNextWallpaper(
        selectedCategories: Set<String>,
        isRandom: Boolean,
        orientation: String
    ): PexelsPhoto? {
        val usedIds = dao.getAllUsedPhotoIds().toSet()

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
                if (photos.isEmpty()) break

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
