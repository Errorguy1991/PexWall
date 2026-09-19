-if class com.pexwall.app.data.api.UnsplashUser
-keepnames class com.pexwall.app.data.api.UnsplashUser
-if class com.pexwall.app.data.api.UnsplashUser
-keep class com.pexwall.app.data.api.UnsplashUserJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
