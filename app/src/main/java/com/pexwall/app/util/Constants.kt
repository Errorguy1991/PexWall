package com.pexwall.app.util

object Constants {
    // Default API key - users will enter their own on first launch
    const val DEFAULT_API_KEY = "qHfCemtP3xT00RhAoRR9L3LQPfvSJm8Yr7HHyy0LOns0U1jrFsOe36nR"
    const val PEXELS_BASE_URL = "https://api.pexels.com/v1/"
    const val DEFAULT_PER_PAGE = 15
    const val HISTORY_RETENTION_DAYS = 7

    val CATEGORIES = listOf(
        Category("nature", "Nature", "nature landscape"),
        Category("city", "City", "city skyline urban"),
        Category("space", "Space", "space galaxy universe"),
        Category("ocean", "Ocean", "ocean sea beach"),
        Category("mountains", "Mountains", "mountains peaks"),
        Category("abstract", "Abstract", "abstract colorful pattern"),
        Category("animals", "Animals", "animals wildlife"),
        Category("flowers", "Flowers", "flowers botanical"),
        Category("minimal", "Minimal", "minimal simple clean"),
        Category("dark", "Dark", "dark moody noir"),
        Category("technology", "Technology", "technology futuristic"),
        Category("forest", "Forest", "forest trees woods")
    )

    val FREQUENCY_OPTIONS = listOf(
        FrequencyOption("15 Minutes", 15L),
        FrequencyOption("30 Minutes", 30L),
        FrequencyOption("1 Hour", 60L),
        FrequencyOption("6 Hours", 360L),
        FrequencyOption("12 Hours", 720L),
        FrequencyOption("Daily", 1440L)
    )
}

data class Category(
    val id: String,
    val displayName: String,
    val searchQuery: String
)

data class FrequencyOption(
    val displayName: String,
    val intervalMinutes: Long
)

enum class WallpaperMode(val displayName: String) {
    HOME_ONLY("Home Screen Only"),
    LOCK_ONLY("Lock Screen Only"),
    BOTH_SAME("Both (Same Wallpaper)")
}

/** Holds the current API key for runtime use by OkHttp interceptor */
object ApiKeyHolder {
    @Volatile
    var apiKey: String = Constants.DEFAULT_API_KEY
}
