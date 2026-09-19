package com.pexwall.app.ui.home

import android.app.WallpaperManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pexwall.app.data.db.WallpaperEntity
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.data.repository.WallpaperRepository
import com.pexwall.app.util.Constants
import com.pexwall.app.util.WallpaperMode
import com.pexwall.app.util.WallpaperSetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentWallpaper: WallpaperEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val wallpaperSetter: WallpaperSetter,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val latest = repository.getLatestWallpaper()
            if (latest == null) {
                changeWallpaperNow()
            }
            repository.getLatestWallpaperFlow().collectLatest { wallpaper ->
                _uiState.update { it.copy(currentWallpaper = wallpaper) }
            }
        }
    }

    fun changeWallpaperNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val mode = preferencesManager.wallpaperMode.first()
                val isRandom = preferencesManager.randomMode.first()
                val orientation = preferencesManager.orientation.first()
                val homeCategories = preferencesManager.homeCategories.first()
                val lockCategories = preferencesManager.lockCategories.first()
                val homeBlur = preferencesManager.homeBlurPercent.first()
                val lockBlur = preferencesManager.lockBlurPercent.first()

                var success = false

                when (mode) {
                    WallpaperMode.HOME_ONLY -> {
                        val photo = repository.getNextWallpaper(homeCategories, isRandom, orientation) ?: throw Exception("No photo found")
                        success = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_SYSTEM, homeBlur)
                        val catId = if (isRandom) Constants.CATEGORIES.random().id else homeCategories.firstOrNull() ?: "nature"
                        if (success) repository.saveToHistory(photo, catId)
                    }
                    WallpaperMode.LOCK_ONLY -> {
                        val photo = repository.getNextWallpaper(lockCategories, isRandom, orientation) ?: throw Exception("No photo found")
                        success = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_LOCK, lockBlur)
                        val catId = if (isRandom) Constants.CATEGORIES.random().id else lockCategories.firstOrNull() ?: "nature"
                        if (success) repository.saveToHistory(photo, catId)
                    }
                    WallpaperMode.SEPARATE_HOME_LOCK -> {
                        val photoHome = repository.getNextWallpaper(homeCategories, isRandom, orientation) ?: throw Exception("No home photo found")
                        val photoLock = repository.getNextWallpaper(lockCategories, isRandom, orientation, setOf(photoHome.id)) ?: throw Exception("No lock photo found")
                        val successHome = wallpaperSetter.setWallpaper(photoHome.src.original, WallpaperManager.FLAG_SYSTEM, homeBlur)
                        val successLock = wallpaperSetter.setWallpaper(photoLock.src.original, WallpaperManager.FLAG_LOCK, lockBlur)
                        success = successHome && successLock
                        val catIdHome = if (isRandom) Constants.CATEGORIES.random().id else homeCategories.firstOrNull() ?: "nature"
                        val catIdLock = if (isRandom) Constants.CATEGORIES.random().id else lockCategories.firstOrNull() ?: "nature"
                        if (successHome) repository.saveToHistory(photoHome, catIdHome)
                        if (successLock) repository.saveToHistory(photoLock, catIdLock)
                    }
                    WallpaperMode.BOTH_SAME -> {
                        val photo = repository.getNextWallpaper(homeCategories, isRandom, orientation) ?: throw Exception("No photo found")
                        val successHome = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_SYSTEM, homeBlur)
                        val successLock = wallpaperSetter.setWallpaper(photo.src.original, WallpaperManager.FLAG_LOCK, lockBlur)
                        success = successHome || successLock
                        val catId = if (isRandom) Constants.CATEGORIES.random().id else homeCategories.firstOrNull() ?: "nature"
                        if (success) repository.saveToHistory(photo, catId)
                    }
                }

                if (!success) {
                    _uiState.update { it.copy(error = "Failed to set wallpaper") }
                }

                repository.pruneOldHistory()
            } catch (e: retrofit2.HttpException) {
                e.printStackTrace()
                if (e.code() == 401) {
                    _uiState.update { it.copy(error = "HTTP 401 Unauthorized: Invalid Pexels API Key. Please update it in Settings.") }
                } else {
                    _uiState.update { it.copy(error = e.localizedMessage ?: "An error occurred") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = e.localizedMessage ?: "An error occurred") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
