package com.pexwall.app.ui.history

import android.app.WallpaperManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pexwall.app.data.db.WallpaperEntity
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.data.repository.WallpaperRepository
import com.pexwall.app.util.WallpaperMode
import com.pexwall.app.util.WallpaperSetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class HistoryUiState(
    val historyItems: Map<String, List<WallpaperEntity>> = emptyMap(),
    val isLoading: Boolean = false,
    val applyingId: Long? = null,
    val message: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val wallpaperSetter: WallpaperSetter,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    init {
        viewModelScope.launch {
            repository.getAllHistory().collectLatest { wallpapers ->
                val grouped = wallpapers.groupBy { dateFormat.format(Date(it.dateSet)) }
                _uiState.update { it.copy(historyItems = grouped, isLoading = false) }
            }
        }
    }

    fun reapplyWallpaper(wallpaper: WallpaperEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(applyingId = wallpaper.id, message = null) }
            try {
                val mode = preferencesManager.wallpaperMode.first()
                val homeBlur = preferencesManager.homeBlurPercent.first()
                val lockBlur = preferencesManager.lockBlurPercent.first()

                val success = when (mode) {
                    WallpaperMode.HOME_ONLY -> wallpaperSetter.setWallpaper(wallpaper.imageUrl, WallpaperManager.FLAG_SYSTEM, homeBlur)
                    WallpaperMode.LOCK_ONLY -> wallpaperSetter.setWallpaper(wallpaper.imageUrl, WallpaperManager.FLAG_LOCK, lockBlur)
                    WallpaperMode.BOTH_SAME -> {
                        val h = wallpaperSetter.setWallpaper(wallpaper.imageUrl, WallpaperManager.FLAG_SYSTEM, homeBlur)
                        val l = wallpaperSetter.setWallpaper(wallpaper.imageUrl, WallpaperManager.FLAG_LOCK, lockBlur)
                        h || l
                    }
                }

                if (success) {
                    _uiState.update { it.copy(message = "Wallpaper applied successfully") }
                } else {
                    _uiState.update { it.copy(message = "Failed to apply wallpaper") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(message = e.localizedMessage ?: "Unknown error") }
            } finally {
                _uiState.update { it.copy(applyingId = null) }
            }
        }
    }
    
    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
