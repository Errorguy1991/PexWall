package com.pexwall.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pexwall.app.data.db.WallpaperEntity
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.data.repository.WallpaperRepository
import com.pexwall.app.util.Constants
import com.pexwall.app.util.WallpaperSetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentWallpaper: WallpaperEntity? = null,
    val isLoading: Boolean = false,
    val isChanging: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val wallpaperSetter: WallpaperSetter,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLatestWallpaperFlow().collect { wallpaper ->
                _uiState.update { it.copy(currentWallpaper = wallpaper, isLoading = false) }
            }
        }
    }

    fun changeWallpaperNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isChanging = true, errorMessage = null) }
            try {
                val photo = repository.getNextWallpaper()
                if (photo != null) {
                    val target = preferencesManager.wallpaperTarget.first()
                    val success = wallpaperSetter.setWallpaper(photo.src.original, target)
                    if (success) {
                        val selectedCategories = preferencesManager.selectedCategories.first()
                        val category = if (preferencesManager.randomMode.first()) {
                            Constants.CATEGORIES.random().id
                        } else {
                            selectedCategories.firstOrNull() ?: "nature"
                        }
                        repository.saveToHistory(photo, category)
                        repository.pruneOldHistory()
                        _uiState.update { it.copy(isChanging = false) }
                    } else {
                        _uiState.update {
                            it.copy(isChanging = false, errorMessage = "Failed to set wallpaper")
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(isChanging = false, errorMessage = "No wallpapers available")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isChanging = false, errorMessage = e.message ?: "An error occurred")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
