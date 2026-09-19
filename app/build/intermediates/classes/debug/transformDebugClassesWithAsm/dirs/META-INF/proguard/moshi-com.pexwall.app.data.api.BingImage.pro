-if class com.pexwall.app.data.api.BingImage
-keepnames class com.pexwall.app.data.api.BingImage
-if class com.pexwall.app.data.api.BingImage
-keep class com.pexwall.app.data.api.BingImageJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
