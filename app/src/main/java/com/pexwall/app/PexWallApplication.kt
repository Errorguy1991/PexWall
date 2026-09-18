package com.pexwall.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.util.ApiKeyHolder
import com.pexwall.app.util.Constants
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class PexWallApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        loadApiKey()
    }

    private fun loadApiKey() {
        applicationScope.launch {
            val savedKey = preferencesManager.apiKey.first()
            ApiKeyHolder.apiKey = if (savedKey.isNotBlank()) savedKey else Constants.DEFAULT_API_KEY
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "wallpaper_updates",
            "Wallpaper Updates",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notifications when wallpaper is automatically changed"
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
