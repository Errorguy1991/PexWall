-if class com.pexwall.app.data.api.PexelsResponse
-keepnames class com.pexwall.app.data.api.PexelsResponse
-if class com.pexwall.app.data.api.PexelsResponse
-keep class com.pexwall.app.data.api.PexelsResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.pexwall.app.data.api.PexelsResponse
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.pexwall.app.data.api.PexelsResponse
-keepclassmembers class com.pexwall.app.data.api.PexelsResponse {
    public synthetic <init>(int,int,int,java.util.List,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
