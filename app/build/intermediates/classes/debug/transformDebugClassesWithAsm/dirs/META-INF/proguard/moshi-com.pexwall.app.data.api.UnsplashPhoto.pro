-if class com.pexwall.app.data.api.UnsplashPhoto
-keepnames class com.pexwall.app.data.api.UnsplashPhoto
-if class com.pexwall.app.data.api.UnsplashPhoto
-keep class com.pexwall.app.data.api.UnsplashPhotoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
