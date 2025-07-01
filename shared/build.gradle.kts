plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    id("app.cash.sqldelight") version "2.1.0"
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.skie)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
// Target declarations - add or remove as needed below. These define
// which platforms this KMP module supports.
// See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.peihua.chatbox.shared"
        compileSdk = 35
        minSdk = 24
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

// For iOS targets, this is also where you should
// configure native binary output. For more information, see:
// https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

// A step-by-step guide on how to include this library in an XCode
// project can be found here:
// https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "sharedKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

// Source set declarations.
// Declaring a target automatically creates a source set with the same name. By default, the
// Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
// common to share sources between related targets.
// See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.jetbrains.compose.ui)
//                implementation(compose.ui)
                implementation(libs.jetbrains.compose.foundation)
//                implementation(compose.foundation)
                implementation(libs.jetbrains.compose.components.resources)
//                implementation(compose.components.resources)
                implementation(libs.jetbrains.compose.material3)
//                implementation(compose.material3)
                implementation(libs.jetbrains.compose.material.icons.extended)
//                implementation(compose.materialIconsExtended)
                implementation(libs.jetbrains.compose.animation)
//                implementation(compose.animationGraphics)
                implementation(libs.jetbrains.compose.runtime)
//                implementation(compose.runtime)
                implementation(libs.jetbrains.compose.runtime.saveable)
//                implementation(compose.runtimeSaveable)
//                implementation(compose.desktop)
//                implementation(compose.html.core)
//                implementation(libs.kotlin.stdlib.common)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.navigation.compose)
//                implementation("androidx.navigation:navigation-compose:2.8.8")
//                implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
                implementation(libs.coil3.coil.compose)
                api(libs.androidx.datastore.preferences.core)
                api(libs.androidx.datastore.core.okio)
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.okio)
                implementation(libs.skie.annotations)
                implementation("dev.tclement.fonticons:core:2.1.1")
                implementation(kotlin("stdlib-common"))
                implementation("app.cash.sqldelight:runtime:2.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.0")
                // UI/UX Utils
                var markdown = "0.35.0"
                implementation("com.mikepenz:multiplatform-markdown-renderer:${markdown}")
                implementation("com.mikepenz:multiplatform-markdown-renderer-m3:${markdown}")
                implementation("com.mikepenz:multiplatform-markdown-renderer-code:${markdown}")
                // Offers coil2 (Coil2ImageTransformerImpl)
                implementation("com.mikepenz:multiplatform-markdown-renderer-coil3:${markdown}")

                // Add KMP dependencies here

            }
        }
        jvmMain {
            dependencies {
                // JVM 相关的标准库
                implementation(kotlin("stdlib"))
                implementation(libs.ktor.client.okhttp)
                implementation("app.cash.sqldelight:sqlite-driver:2.1.0")
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(libs.ktor.client.android)
                implementation(libs.ktor.client.core.jvm)
                implementation(libs.kotlin.stdlib)
                implementation(kotlin("stdlib"))
                implementation(libs.ktor.client.okhttp)
                implementation("app.cash.sqldelight:android-driver:2.1.0")
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
                implementation(libs.ktor.client.darwin)
                implementation("app.cash.sqldelight:native-driver:2.1.0")
            }
        }
    }

}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.peihua.chatbox.shared.data.db")

        }
    }
}


room {
    schemaDirectory("$projectDir/schemas")
}

skie {
    features {
        // https://skie.touchlab.co/features/flows-in-swiftui
        enableSwiftUIObservingPreview = true
    }
}
