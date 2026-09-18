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
    val frequencyMinutes: Long = 1440L,
    val wifiOnly: Boolean = false,
    val wallpaperMode: WallpaperMode = WallpaperMode.BOTH_SAME,
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
            combine(
                preferencesManager.apiKey,
                preferencesManager.frequencyMinutes,
                preferencesManager.wifiOnly,
                preferencesManager.wallpaperMode,
                preferencesManager.autoChangeEnabled,
                preferencesManager.homeCategories,
                preferencesManager.lockCategories,
                preferencesManager.randomMode,
                preferencesManager.homeBlurPercent,
                preferencesManager.lockBlurPercent,
                preferencesManager.notificationsEnabled,
                preferencesManager.orientation
            ) { values ->
                SettingsUiState(
                    apiKey = values[0] as String,
                    frequencyMinutes = values[1] as Long,
                    wifiOnly = values[2] as Boolean,
                    wallpaperMode = values[3] as WallpaperMode,
                    autoChangeEnabled = values[4] as Boolean,
                    homeCategories = values[5] as Set<String>,
                    lockCategories = values[6] as Set<String>,
                    randomMode = values[7] as Boolean,
                    homeBlurPercent = values[8] as Int,
                    lockBlurPercent = values[9] as Int,
                    notificationsEnabled = values[10] as Boolean,
                    orientation = values[11] as String
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setApiKey(key: String) {
        viewModelScope.launch {
            val validKey = if (key.isBlank()) Constants.DEFAULT_API_KEY else key
            ApiKeyHolder.apiKey = validKey
            preferencesManager.setApiKey(validKey)
        }
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
