-if class com.pexwall.app.data.api.PhotoSrc
-keepnames class com.pexwall.app.data.api.PhotoSrc
-if class com.pexwall.app.data.api.PhotoSrc
-keep class com.pexwall.app.data.api.PhotoSrcJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
