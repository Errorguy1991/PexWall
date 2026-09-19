package com.pexwall.app.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pexwall.app.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesUiState(
    val homeCategories: Set<String> = setOf("nature"),
    val lockCategories: Set<String> = setOf("nature"),
    val randomMode: Boolean = false,
    val wallpaperSource: com.pexwall.app.util.WallpaperSource = com.pexwall.app.util.WallpaperSource.PEXELS
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.homeCategories.collectLatest { cats ->
                _uiState.value = _uiState.value.copy(homeCategories = cats)
            }
        }
        viewModelScope.launch {
            preferencesManager.lockCategories.collectLatest { cats ->
                _uiState.value = _uiState.value.copy(lockCategories = cats)
            }
        }
        viewModelScope.launch {
            preferencesManager.wallpaperSource.collectLatest { source ->
                _uiState.value = _uiState.value.copy(wallpaperSource = source)
            }
        }
        viewModelScope.launch {
            preferencesManager.randomMode.collectLatest { random ->
                _uiState.value = _uiState.value.copy(randomMode = random)
            }
        }
    }

    fun toggleHomeCategory(categoryId: String) {
        viewModelScope.launch {
            val current = _uiState.value.homeCategories.toMutableSet()
            if (current.contains(categoryId)) {
                if (current.size > 1) { // Prevent empty selection
                    current.remove(categoryId)
                }
            } else {
                current.add(categoryId)
            }
            preferencesManager.setHomeCategories(current)
        }
    }

    fun toggleLockCategory(categoryId: String) {
        viewModelScope.launch {
            val current = _uiState.value.lockCategories.toMutableSet()
            if (current.contains(categoryId)) {
                if (current.size > 1) {
                    current.remove(categoryId)
                }
            } else {
                current.add(categoryId)
            }
            preferencesManager.setLockCategories(current)
        }
    }

    fun setRandomMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setRandomMode(enabled)
        }
    }
}
