import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataApiFeature"
            isStatic = true
        }
    }

    sourceSets {
        all { languageSettings { optIn("kotlin.uuid.ExperimentalUuidApi") } }

        androidMain.dependencies { implementation(libs.androidx.core) }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test.common)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine.test)
        }
    }
}

android {
    namespace = "app.jjerrell.proofed.feature.data.api"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}