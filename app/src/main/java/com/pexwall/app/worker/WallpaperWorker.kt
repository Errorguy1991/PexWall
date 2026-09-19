package com.pexwall.app.worker

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pexwall.app.MainActivity
import com.pexwall.app.R
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.data.repository.WallpaperRepository
import com.pexwall.app.util.Constants
import com.pexwall.app.util.WallpaperMode
import com.pexwall.app.util.WallpaperSetter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class WallpaperWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WallpaperRepository,
    private val wallpaperSetter: WallpaperSetter,
    private val preferencesManager: PreferencesManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val autoChangeEnabled = preferencesManager.autoChangeEnabled.first()
            if (!autoChangeEnabled) return Result.success()

            val mode = preferencesManager.wallpaperMode.first()
            val isRandom = preferencesManager.randomMode.first()
            val orientation = preferencesManager.orientation.first()

            val homeCategories = preferencesManager.homeCategories.first()
            val lockCategories = preferencesManager.lockCategories.first()
            
            val homeBlur = preferencesManager.homeBlurPercent.first()
            val lockBlur = preferencesManager.lockBlurPercent.first()

            var successHome = false
            var successLock = false
            var photographer = ""

            when (mode) {
                WallpaperMode.HOME_ONLY -> {
                    val photo = repository.getNextWallpaper(homeCategories, isRandom, orientation) ?: return Result.retry()
                    photographer = photo.photographer
                    successHome = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_SYSTEM, homeBlur)
                    
                    val catId = if (isRandom) Constants.CATEGORIES.random().id else homeCategories.firstOrNull() ?: "nature"
                    if (successHome) repository.saveToHistory(photo, catId)
                }
                WallpaperMode.LOCK_ONLY -> {
                    val photo = repository.getNextWallpaper(lockCategories, isRandom, orientation) ?: return Result.retry()
                    photographer = photo.photographer
                    successLock = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_LOCK, lockBlur)
                    
                    val catId = if (isRandom) Constants.CATEGORIES.random().id else lockCategories.firstOrNull() ?: "nature"
                    if (successLock) repository.saveToHistory(photo, catId)
                }
                WallpaperMode.SEPARATE_HOME_LOCK -> {
                    val photoHome = repository.getNextWallpaper(homeCategories, isRandom, orientation) ?: return Result.retry()
                    val photoLock = repository.getNextWallpaper(lockCategories, isRandom, orientation) ?: return Result.retry()
                    photographer = " & "
                    successHome = wallpaperSetter.setWallpaper(photoHome.src.original, WallpaperManager.FLAG_SYSTEM, homeBlur)
                    successLock = wallpaperSetter.setWallpaper(photoLock.src.original, WallpaperManager.FLAG_LOCK, lockBlur)
                    val catIdHome = if (isRandom) Constants.CATEGORIES.random().id else homeCategories.firstOrNull() ?: "nature"
                    val catIdLock = if (isRandom) Constants.CATEGORIES.random().id else lockCategories.firstOrNull() ?: "nature"
                    if (successHome) repository.saveToHistory(photoHome, catIdHome)
                    if (successLock) repository.saveToHistory(photoLock, catIdLock)
                }
                WallpaperMode.BOTH_SAME -> {
                    // Use home categories as the primary source for "both same"
                    val photo = repository.getNextWallpaper(homeCategories, isRandom, orientation) ?: return Result.retry()
                    photographer = photo.photographer
                    
                    // We apply them separately to support different blur levels
                    successHome = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_SYSTEM, homeBlur)
                    successLock = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_LOCK, lockBlur)
                    
                    val catId = if (isRandom) Constants.CATEGORIES.random().id else homeCategories.firstOrNull() ?: "nature"
                    if (successHome || successLock) repository.saveToHistory(photo, catId)
                }
            }

            repository.pruneOldHistory()

            if (successHome || successLock) {
                val notificationsEnabled = preferencesManager.notificationsEnabled.first()
                if (notificationsEnabled) {
                    showNotification(photographer)
                }
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private fun showNotification(photographer: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) return
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "wallpaper_updates")
            .setSmallIcon(android.R.drawable.ic_menu_gallery)
            .setContentTitle("Wallpaper Changed")
            .setContentText("New wallpaper by $photographer")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1001, notification)
    }
}
