package com.pexwall.app.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.util.ApiKeyHolder
import com.pexwall.app.util.Constants
import com.pexwall.app.util.WallpaperMode
import com.pexwall.app.worker.WallpaperWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class SettingsUiState(
    val apiKey: String = "",
    val unsplashApiKey: String = "",
    val frequencyMinutes: Long = 1440L,
    val wifiOnly: Boolean = false,
    val wallpaperMode: WallpaperMode = WallpaperMode.BOTH_SAME,
    val wallpaperSource: com.pexwall.app.util.WallpaperSource = com.pexwall.app.util.WallpaperSource.PEXELS,
    val autoChangeEnabled: Boolean = true,
    val homeCategories: Set<String> = setOf("nature"),
    val lockCategories: Set<String> = setOf("nature"),
    val randomMode: Boolean = false,
    val homeBlurPercent: Int = 0,
    val lockBlurPercent: Int = 0,
    val notificationsEnabled: Boolean = true,
    val orientation: String = "portrait"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.unsplashApiKey.collect { val_ -> _uiState.update { it.copy(unsplashApiKey = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.apiKey.collect { val_ -> _uiState.update { it.copy(apiKey = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.wallpaperSource.collect { val_ -> _uiState.update { it.copy(wallpaperSource = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.frequencyMinutes.collect { val_ -> _uiState.update { it.copy(frequencyMinutes = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.wifiOnly.collect { val_ -> _uiState.update { it.copy(wifiOnly = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.wallpaperMode.collect { val_ -> _uiState.update { it.copy(wallpaperMode = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.autoChangeEnabled.collect { val_ -> _uiState.update { it.copy(autoChangeEnabled = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.homeCategories.collect { val_ -> _uiState.update { it.copy(homeCategories = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.lockCategories.collect { val_ -> _uiState.update { it.copy(lockCategories = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.randomMode.collect { val_ -> _uiState.update { it.copy(randomMode = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.homeBlurPercent.collect { val_ -> _uiState.update { it.copy(homeBlurPercent = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.lockBlurPercent.collect { val_ -> _uiState.update { it.copy(lockBlurPercent = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { val_ -> _uiState.update { it.copy(notificationsEnabled = val_) } }
        }
        viewModelScope.launch {
            preferencesManager.orientation.collect { val_ -> _uiState.update { it.copy(orientation = val_) } }
        }
    }

    fun setApiKey(key: String) {
        ApiKeyHolder.apiKey = key
        viewModelScope.launch { preferencesManager.setApiKey(key) }
    }

    fun setUnsplashApiKey(key: String) {
        ApiKeyHolder.unsplashApiKey = key
        viewModelScope.launch { preferencesManager.setUnsplashApiKey(key) }
    }

    fun setFrequency(minutes: Long) {
        viewModelScope.launch {
            preferencesManager.setFrequencyMinutes(minutes)
            rescheduleWork()
        }
    }

    fun setWifiOnly(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setWifiOnly(enabled)
            rescheduleWork()
        }
    }

    fun setWallpaperSource(source: com.pexwall.app.util.WallpaperSource) {
        viewModelScope.launch { preferencesManager.setWallpaperSource(source) }
    }

    fun setWallpaperMode(mode: WallpaperMode) {
        viewModelScope.launch {
            preferencesManager.setWallpaperMode(mode)
        }
    }

    fun setAutoChangeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAutoChangeEnabled(enabled)
            if (enabled) {
                rescheduleWork()
            } else {
                cancelWork()
            }
        }
    }

    fun setHomeBlurPercent(percent: Int) {
        viewModelScope.launch { preferencesManager.setHomeBlurPercent(percent) }
    }

    fun setLockBlurPercent(percent: Int) {
        viewModelScope.launch { preferencesManager.setLockBlurPercent(percent) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setNotificationsEnabled(enabled) }
    }

    fun setOrientation(orientation: String) {
        viewModelScope.launch { preferencesManager.setOrientation(orientation) }
    }

    private fun rescheduleWork() {
        val state = _uiState.value
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                if (state.wifiOnly) NetworkType.UNMETERED else NetworkType.CONNECTED
            )
            .build()

        val intervalMinutes = maxOf(state.frequencyMinutes, 15L)

        val workRequest = PeriodicWorkRequestBuilder<WallpaperWorker>(
            intervalMinutes, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "PexWallAutoChange",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun cancelWork() {
        WorkManager.getInstance(context).cancelUniqueWork("PexWallAutoChange")
    }
}
