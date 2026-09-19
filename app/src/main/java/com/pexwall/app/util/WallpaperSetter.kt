package com.pexwall.app.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperSetter @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Downloads an image and sets it as wallpaper on the specified target.
     * @param imageUrl URL of the image to download
     * @param target WallpaperManager.FLAG_SYSTEM, FLAG_LOCK, or both OR'd together
     * @param blurPercent 0-100, where 0 = no blur and 100 = maximum blur
     */
    suspend fun setWallpaper(
        imageUrl: String,
        target: Int,
        blurPercent: Int = 0
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)
            val inputStream = URL(imageUrl).openStream()
            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap != null) {
                if (blurPercent > 0) {
                    bitmap = applyBlur(bitmap, blurPercent)
                }
                wallpaperManager.setBitmap(bitmap, null, true, target)
                bitmap.recycle()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun downloadBitmap(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val inputStream = URL(imageUrl).openStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Efficient blur via downscale + upscale interpolation.
     * Works on all API levels without RenderScript.
     */
    private fun applyBlur(source: Bitmap, blurPercent: Int): Bitmap {
        if (blurPercent <= 0) return source
        val clamped = blurPercent.coerceIn(1, 100)
        // Downscale first to make StackBlur faster
        val scaleFactor = 0.25f // 1/4 resolution is plenty for a heavy blur
        val scaledW = maxOf(2, (source.width * scaleFactor).toInt())
        val scaledH = maxOf(2, (source.height * scaleFactor).toInt())
        
        val small = Bitmap.createScaledBitmap(source, scaledW, scaledH, true)
        
        // Convert blurPercent (1-100) to a StackBlur radius (1-50 approx)
        val radius = maxOf(1, (clamped * 0.5f).toInt())
        
        val blurredSmall = StackBlur.process(small, radius)
        val finalBitmap = Bitmap.createScaledBitmap(blurredSmall, source.width, source.height, true)
        
        if (small != blurredSmall && small != source) small.recycle()
        if (blurredSmall != finalBitmap && blurredSmall != source) blurredSmall.recycle()
        
        return finalBitmap
    }
}
