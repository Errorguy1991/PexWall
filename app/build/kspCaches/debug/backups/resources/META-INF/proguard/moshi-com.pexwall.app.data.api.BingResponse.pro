-if class com.pexwall.app.data.api.BingResponse
-keepnames class com.pexwall.app.data.api.BingResponse
-if class com.pexwall.app.data.api.BingResponse
-keep class com.pexwall.app.data.api.BingResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.pexwall.app.data.api.BingResponse
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.pexwall.app.data.api.BingResponse
-keepclassmembers class com.pexwall.app.data.api.BingResponse {
    public synthetic <init>(java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
