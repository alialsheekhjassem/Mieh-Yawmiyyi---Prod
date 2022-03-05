# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#////////////////////////////////////// GLIDE /////////////////////////////////////////////////////////////////
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


# /////////////////////// Log /////////////////////////////////////
-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5

-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** e(...);
    public static *** i(...);
    public static *** w(...);
}


# /////////////// requests and responses ///////////////////////////////////

-keep class com.magma.miyyiyawmiyyi.android.data.remote** { *; }


# /////////////// Support //////////////////////
-keep class android.support. { *; }
-keep interface android.support. { *; }


# ////////////////////////////// okhttp3 /////////////////////
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
## A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn okhttp3.internal.platform.*
-dontwarn org.conscrypt.*


#//////////////////////////////// Gson ////////////////////////////

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
#
## For using GSON @Expose annotation
-keepattributes *Annotation*
#
## Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }
#
## Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
#
## Prevent proguard from stripping interface information from TypeAdapterFactory,
## JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


#///////////////////// Retrofit /////////////////////////////////
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
-dontwarn retrofit2.**
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

#//////////////////////////////// Dagger /////////////////////////

-dontwarn com.google.errorprone.annotations.**

#//////////////// coroutines

-dontwarn kotlinx.atomicfu.**
