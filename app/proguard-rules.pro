# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidSdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
##---------------Begin: proguard configuration for Gson ----------
# Gson uses generic type information stored in a class file when working with
#fields. Proguard removes such information by default, so configure it to keep
#all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class * implements wssj.co.jp.point.model.GsonSerializable { *; }

##---------------End: proguard configuration for Gson ----------
-dontwarn com.yalantis.ucrop**
-dontwarn com.amazonaws.**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-dontwarn okio.**
-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn org.androidannotations.api.rest.**
-dontwarn com.iarcuschin.simpleratingbar.**
-keep class com.iarcuschin.simpleratingbar.** { *; }