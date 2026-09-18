package com.pexwall.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pexwall.app.data.db.WallpaperEntity
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.data.repository.WallpaperRepository
import com.pexwall.app.util.WallpaperSetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val historyItems: Map<String, List<WallpaperEntity>> = emptyMap(),
    val isLoading: Boolean = true,
    val applyingId: Long? = null,
    val message: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val wallpaperSetter: WallpaperSetter,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllHistory().collect { items ->
                val grouped = items.groupBy { entity ->
                    val date = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                        .format(java.util.Date(entity.dateSet))
                    date
                }
                _uiState.update { it.copy(historyItems = grouped, isLoading = false) }
            }
        }
    }

    fun reapplyWallpaper(wallpaper: WallpaperEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(applyingId = wallpaper.id) }
            try {
                val target = preferencesManager.wallpaperTarget.first()
                val success = wallpaperSetter.setWallpaper(wallpaper.imageUrl, target)
                _uiState.update {
                    it.copy(
                        applyingId = null,
                        message = if (success) "Wallpaper applied!" else "Failed to apply wallpaper"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(applyingId = null, message = e.message)
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
