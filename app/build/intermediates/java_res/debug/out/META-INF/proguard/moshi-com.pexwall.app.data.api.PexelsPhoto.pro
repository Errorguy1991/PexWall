-if class com.pexwall.app.data.api.PexelsPhoto
-keepnames class com.pexwall.app.data.api.PexelsPhoto
-if class com.pexwall.app.data.api.PexelsPhoto
-keep class com.pexwall.app.data.api.PexelsPhotoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.pexwall.app.data.api.PexelsPhoto
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.pexwall.app.data.api.PexelsPhoto
-keepclassmembers class com.pexwall.app.data.api.PexelsPhoto {
    public synthetic <init>(int,int,int,java.lang.String,java.lang.String,java.lang.String,long,java.lang.String,com.pexwall.app.data.api.PhotoSrc,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
