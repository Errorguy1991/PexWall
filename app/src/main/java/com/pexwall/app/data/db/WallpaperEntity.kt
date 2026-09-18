package com.pexwall.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpaper_history")
data class WallpaperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pexelsPhotoId: Long,
    val imageUrl: String,
    val thumbnailUrl: String,
    val photographer: String,
    val photographerUrl: String,
    val category: String,
    val dateSet: Long = System.currentTimeMillis(),
    val avgColor: String? = null
)
