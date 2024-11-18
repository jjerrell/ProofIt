import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.roomDb)
    alias(libs.plugins.ksp)
}

room { schemaDirectory("$projectDir/schemas") }

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataLocalFeature"
            isStatic = true
        }
    }

    sourceSets {
        all { languageSettings { optIn("kotlin.uuid.ExperimentalUuidApi") } }

        androidMain.dependencies { implementation(libs.androidx.core) }
        commonMain.dependencies {
            implementation(projects.feature.domain.api)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.room.runtime)
//            implementation(libs.room.sqlite)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test.common)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "app.jjerrell.proofed.feature.data.local"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}

dependencies {
    listOf(
        "kspAndroid",
        "kspIosSimulatorArm64",
        "kspIosX64",
        "kspIosArm64"
    ).forEach {
       add(it, libs.room.compiler)
    }
}
