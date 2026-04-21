# Add project specific ProGuard rules here.

# Keep ML Kit component registrars to avoid NoSuchMethodException during initialization
-keep class com.google.mlkit.** {
    public protected *;
}
-keep class com.google.android.gms.internal.mlkit_vision_barcode.** {
    public protected *;
}
-keep class com.google.firebase.components.ComponentRegistrar
-keep public class * extends com.google.firebase.components.ComponentRegistrar

# Keep line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Ignore warnings from some dependencies if they are not critical
-dontwarn com.google.android.gms.internal.**
-dontwarn org.json.**
