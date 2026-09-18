package com.pexwall.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: WallpaperEntity)

    @Query("SELECT * FROM wallpaper_history ORDER BY dateSet DESC")
    fun getAllHistory(): Flow<List<WallpaperEntity>>

    @Query("SELECT * FROM wallpaper_history WHERE category = :category ORDER BY dateSet DESC")
    fun getHistoryByCategory(category: String): Flow<List<WallpaperEntity>>

    @Query("SELECT * FROM wallpaper_history ORDER BY dateSet DESC LIMIT 1")
    suspend fun getLatestWallpaper(): WallpaperEntity?

    @Query("SELECT * FROM wallpaper_history ORDER BY dateSet DESC LIMIT 1")
    fun getLatestWallpaperFlow(): Flow<WallpaperEntity?>

    @Query("SELECT pexelsPhotoId FROM wallpaper_history")
    suspend fun getAllUsedPhotoIds(): List<Long>

    @Query("DELETE FROM wallpaper_history WHERE dateSet < :cutoffTime")
    suspend fun deleteOlderThan(cutoffTime: Long)

    @Query("SELECT COUNT(*) FROM wallpaper_history WHERE pexelsPhotoId = :photoId")
    suspend fun isPhotoUsed(photoId: Long): Int
}
