# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep ML Kit component registrars to avoid NoSuchMethodException during initialization
-keep class com.google.mlkit.** { *; }
-keep class com.google.firebase.components.ComponentRegistrar
-keep public class * extends com.google.firebase.components.ComponentRegistrar

# Keep line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
