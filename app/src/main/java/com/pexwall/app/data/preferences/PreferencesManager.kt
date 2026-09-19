package com.pexwall.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.pexwall.app.util.WallpaperMode
import com.pexwall.app.util.WallpaperSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pexwall_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_ORIENTATION = stringPreferencesKey("orientation")
        private val KEY_API_KEY = stringPreferencesKey("pexels_api_key")
        private val KEY_UNSPLASH_API_KEY = stringPreferencesKey("unsplash_api_key")
        private val KEY_FREQUENCY_MINUTES = longPreferencesKey("frequency_minutes")
        private val KEY_WIFI_ONLY = booleanPreferencesKey("wifi_only")
        private val KEY_WALLPAPER_MODE = stringPreferencesKey("wallpaper_mode")
        private val KEY_HOME_CATEGORIES = stringSetPreferencesKey("home_categories")
        private val KEY_LOCK_CATEGORIES = stringSetPreferencesKey("lock_categories")
        private val KEY_RANDOM_MODE = booleanPreferencesKey("random_mode")
        private val KEY_AUTO_CHANGE_ENABLED = booleanPreferencesKey("auto_change_enabled")
        private val KEY_HOME_BLUR_PERCENT = intPreferencesKey("home_blur_percent")
        private val KEY_LOCK_BLUR_PERCENT = intPreferencesKey("lock_blur_percent")
        private val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val KEY_CURRENT_PAGE = intPreferencesKey("current_page")
        private val KEY_HAS_SEEN_API_KEY_DIALOG = booleanPreferencesKey("has_seen_api_key_dialog")
        private val KEY_WALLPAPER_SOURCE = stringPreferencesKey("wallpaper_source")
    }

    val apiKey: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_API_KEY] ?: ""
    }
    val unsplashApiKey: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_UNSPLASH_API_KEY] ?: ""
    }

    val frequencyMinutes: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[KEY_FREQUENCY_MINUTES] ?: 1440L
    }

    val wifiOnly: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_WIFI_ONLY] ?: false
    }

    val wallpaperMode: Flow<WallpaperMode> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_WALLPAPER_MODE] ?: WallpaperMode.BOTH_SAME.name
        try { WallpaperMode.valueOf(name) } catch (_: Exception) { WallpaperMode.BOTH_SAME }
    }

    val homeCategories: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_HOME_CATEGORIES] ?: setOf("nature")
    }

    val lockCategories: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_LOCK_CATEGORIES] ?: setOf("nature")
    }

    val randomMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_RANDOM_MODE] ?: false
    }

    val autoChangeEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTO_CHANGE_ENABLED] ?: true
    }

    val homeBlurPercent: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_HOME_BLUR_PERCENT] ?: 0
    }

    val lockBlurPercent: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_LOCK_BLUR_PERCENT] ?: 0
    }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_NOTIFICATIONS_ENABLED] ?: true
    }

    val orientation: Flow<String> = context.dataStore.data.map { prefs -> prefs[KEY_ORIENTATION] ?: "portrait" }

    val currentPage: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_CURRENT_PAGE] ?: 1
    }

    val hasSeenApiKeyDialog: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_HAS_SEEN_API_KEY_DIALOG] ?: false
    }

    val wallpaperSource: Flow<WallpaperSource> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_WALLPAPER_SOURCE] ?: WallpaperSource.BING.name
        try { WallpaperSource.valueOf(name) } catch (e: Exception) { WallpaperSource.BING }
    }

    suspend fun setWallpaperSource(source: WallpaperSource) { context.dataStore.edit { it[KEY_WALLPAPER_SOURCE] = source.name } }
    suspend fun setUnsplashApiKey(key: String) { context.dataStore.edit { it[KEY_UNSPLASH_API_KEY] = key } }
    suspend fun setApiKey(key: String) { context.dataStore.edit { it[KEY_API_KEY] = key } }
    suspend fun setHasSeenApiKeyDialog(seen: Boolean) { context.dataStore.edit { it[KEY_HAS_SEEN_API_KEY_DIALOG] = seen } }
    suspend fun setFrequencyMinutes(minutes: Long) { context.dataStore.edit { it[KEY_FREQUENCY_MINUTES] = minutes } }
    suspend fun setWifiOnly(enabled: Boolean) { context.dataStore.edit { it[KEY_WIFI_ONLY] = enabled } }
    suspend fun setWallpaperMode(mode: WallpaperMode) { context.dataStore.edit { it[KEY_WALLPAPER_MODE] = mode.name } }
    suspend fun setHomeCategories(categories: Set<String>) { context.dataStore.edit { it[KEY_HOME_CATEGORIES] = categories } }
    suspend fun setLockCategories(categories: Set<String>) { context.dataStore.edit { it[KEY_LOCK_CATEGORIES] = categories } }
    suspend fun setRandomMode(enabled: Boolean) { context.dataStore.edit { it[KEY_RANDOM_MODE] = enabled } }
    suspend fun setAutoChangeEnabled(enabled: Boolean) { context.dataStore.edit { it[KEY_AUTO_CHANGE_ENABLED] = enabled } }
    suspend fun setHomeBlurPercent(percent: Int) { context.dataStore.edit { it[KEY_HOME_BLUR_PERCENT] = percent.coerceIn(0, 100) } }
    suspend fun setLockBlurPercent(percent: Int) { context.dataStore.edit { it[KEY_LOCK_BLUR_PERCENT] = percent.coerceIn(0, 100) } }
    suspend fun setNotificationsEnabled(enabled: Boolean) { context.dataStore.edit { it[KEY_NOTIFICATIONS_ENABLED] = enabled } }
    suspend fun setOrientation(orientation: String) { context.dataStore.edit { it[KEY_ORIENTATION] = orientation } }
    suspend fun setCurrentPage(page: Int) { context.dataStore.edit { it[KEY_CURRENT_PAGE] = page } }
}
