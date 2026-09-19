package com.pexwall.app.util

object Constants {
    const val UNSPLASH_ACCESS_KEY = ""
    const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"
    const val DEFAULT_API_KEY = ""
    const val PEXELS_BASE_URL = "https://api.pexels.com/v1/"
    const val DEFAULT_PER_PAGE = 15
    const val HISTORY_RETENTION_DAYS = 7

    val CATEGORIES = listOf(
        Category("all", "All", "wallpaper", "General"),
        Category("nature", "Nature", "nature landscape", "General"),
        Category("urban", "Urban", "urban city street", "General"),
        Category("dark", "Dark / AMOLED", "dark amoled black", "General"),
        Category("minimal", "Minimal", "minimal simple", "General"),
        Category("art", "Art", "art digital-art", "General"),
        
        Category("cyberpunk", "Cyberpunk / Neon", "cyberpunk neon nightlife", "Aesthetic & Mood"),
        Category("lofi", "Lo-Fi / Cozy", "lofi cozy aesthetic room", "Aesthetic & Mood"),
        Category("pastel", "Pastel / Soft", "pastel soft aesthetic", "Aesthetic & Mood"),
        Category("vintage", "Vintage / Retro", "vintage retro 90s aesthetic", "Aesthetic & Mood"),
        Category("moody", "Moody / Cinematic", "moody cinematic fog", "Aesthetic & Mood"),
        
        Category("space", "Space & Cosmos", "space cosmos universe galaxy", "Subject & Objects"),
        Category("vehicles", "Vehicles / Automotive", "vehicles supercar", "Subject & Objects"),
        Category("gaming", "Gaming", "gaming esports setup", "Subject & Objects"),
        Category("anime", "Anime & Manga", "anime aesthetic manga", "Subject & Objects"),
        Category("wildlife", "Wildlife / Fauna", "wildlife animals nature", "Subject & Objects"),
        Category("typography", "Typography & Quotes", "typography quotes minimalist", "Subject & Objects"),
        
        Category("3d_renders", "3D Renders / CGI", "3d render abstract cgi", "Form & Material"),
        Category("textures", "Textures & Patterns", "textures patterns background", "Form & Material"),
        Category("aerial", "Aerial / Satellite", "aerial drone view", "Form & Material"),
        Category("gradients", "Gradients / Mesh", "gradient mesh smooth", "Form & Material")
    )

    val BING_MARKETS = listOf(
        Category("en-US", "United States", "en-US", "Bing Regions"),
        Category("ja-JP", "Japan", "ja-JP", "Bing Regions"),
        Category("en-GB", "United Kingdom", "en-GB", "Bing Regions"),
        Category("de-DE", "Germany", "de-DE", "Bing Regions"),
        Category("fr-FR", "France", "fr-FR", "Bing Regions"),
        Category("it-IT", "Italy", "it-IT", "Bing Regions"),
        Category("en-CA", "Canada", "en-CA", "Bing Regions"),
        Category("zh-CN", "China", "zh-CN", "Bing Regions"),
        Category("es-ES", "Spain", "es-ES", "Bing Regions"),
        Category("en-IN", "India", "en-IN", "Bing Regions")
    )

    val UNSPLASH_CATEGORIES = listOf(
        Category("nature", "Nature", "nature", "Unsplash Categories"),
        Category("animals", "Animals", "animals", "Unsplash Categories"),
        Category("art", "Art", "art", "Unsplash Categories"),
        Category("design", "Design", "design", "Unsplash Categories"),
        Category("minimalism", "Minimalism", "minimalism", "Unsplash Categories"),
        Category("travel", "Travel", "travel", "Unsplash Categories"),
        Category("architecture", "Architecture", "architecture", "Unsplash Categories"),
        Category("cars", "Cars", "cars", "Unsplash Categories"),
        Category("sports", "Sports", "sports", "Unsplash Categories"),
        Category("people", "People", "people", "Unsplash Categories"),
        Category("feeling", "Feeling", "feeling", "Unsplash Categories"),
        Category("culture", "Culture", "culture", "Unsplash Categories")
    )

    val FREQUENCY_OPTIONS = listOf(
        FrequencyOption("15 Mins", 15L),
        FrequencyOption("1 Hour", 60L),
        FrequencyOption("6 Hours", 360L),
        FrequencyOption("12 Hours", 720L),
        FrequencyOption("Daily", 1440L)
    )
}

data class Category(
    val id: String,
    val displayName: String,
    val searchQuery: String,
    val group: String = "General"
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
    BING("Bing Daily (Varied)"),
    UNSPLASH("Unsplash (Curated)")
}

object ApiKeyHolder {
    @Volatile
    var apiKey: String = Constants.DEFAULT_API_KEY
    @Volatile
    var unsplashApiKey: String = Constants.UNSPLASH_ACCESS_KEY
}
