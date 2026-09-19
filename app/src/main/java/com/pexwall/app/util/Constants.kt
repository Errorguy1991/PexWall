package com.pexwall.app.util

object Constants {
    // Default API key - users will enter their own on first launch
    const val DEFAULT_API_KEY = "qHfCemtP3xT00RhAoRR9L3LQPfvSJm8Yr7HHyy0LOns0U1jrFsOe36nR"
    const val PEXELS_BASE_URL = "https://api.pexels.com/v1/"
    const val DEFAULT_PER_PAGE = 15
    const val HISTORY_RETENTION_DAYS = 7

    val CATEGORIES = listOf(
        Category("abstract", "Abstract", "abstract"),
        Category("art", "Art", "art"),
        Category("minimal", "Minimal", "minimal"),
        Category("texture", "Texture", "texture"),
        Category("nature", "Nature", "nature"),
        Category("space", "Space", "space"),
        Category("architecture", "Architecture", "architecture"),
        Category("city", "City", "city"),
        Category("animals", "Animals", "animals"),
        Category("food", "Food", "food")
    )

    val BING_MARKETS = listOf(
        Category("en-US", "United States", "en-US"),
        Category("zh-CN", "China", "zh-CN"),
        Category("ja-JP", "Japan", "ja-JP"),
        Category("en-IN", "India", "en-IN"),
        Category("de-DE", "Germany", "de-DE"),
        Category("fr-FR", "France", "fr-FR"),
        Category("en-GB", "United Kingdom", "en-GB"),
        Category("es-ES", "Spain", "es-ES"),
        Category("it-IT", "Italy", "it-IT")
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
    BOTH_SAME("Both (Same Wallpaper)"),
    SEPARATE_HOME_LOCK("Separate Home & Lock screen")
}

enum class WallpaperSource(val displayName: String) {
    PEXELS("Pexels (High Quality)"),
    BING("Bing Daily (Varied)")
}

/** Holds the current API key for runtime use by OkHttp interceptor */
object ApiKeyHolder {
    @Volatile
    var apiKey: String = Constants.DEFAULT_API_KEY
}
