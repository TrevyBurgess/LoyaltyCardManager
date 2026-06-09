import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    androidLibrary {
        namespace = "com.loyaltycard.shared"
        compileSdk = 37
        minSdk = 26
        
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.navigation.compose)
            implementation(compose.materialIconsExtended)
        }
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)
            implementation(libs.google.mlkit.barcode.scanning)
            implementation(libs.zxing.core)
        }
        iosMain.dependencies {
        }
        commonTest.dependencies {
            implementation(libs.junit)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.mockito.kotlin)
        }
    }
}
