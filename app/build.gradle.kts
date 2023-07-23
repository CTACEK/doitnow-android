@file:Suppress("UnstableApiUsage")


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    id("upload-tg-plugin")
}

pluginExtension {
    enableSizeCheck.set(true)
    enableAnalyzeApk.set(true)
    fileSizeLimitInMb.set(30)
}

android {
    namespace = "com.ctacek.yandexschool.doitnow"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ctacek.yandexschool.doitnow"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["YANDEX_CLIENT_ID"] = "0d0970774e284fa8ba9ff70b6b06479a"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
//            isMinifyEnabled = true
//            isShrinkResources = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
        }
        debug {
//            isShrinkResources = true
//            isMinifyEnabled = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }
}

dependencies {

    implementation(platform("androidx.compose:compose-bom:2023.05.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))

    //Core
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viwemodel.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.material)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.coroutines.android)

    implementation(libs.constraintlayout)
    implementation(libs.coordinatorlayout)
    implementation(libs.androidx.swiperefreshlayout)

    //Animations
    implementation(libs.swipedecorator)
    implementation(libs.lottie)
    implementation(libs.yoyo) {
        artifact {
            type = "aar"
        }
    }

    //Jetpack Navigation
    implementation(libs.bundles.navigation)

    //Room
    kapt(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room)

    implementation(libs.gson)

    //Retrofit
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)

    //Yandex auth
    implementation(libs.authsdk)

    //Dagger 2
    kapt(libs.bundles.dagger.compiler)
    implementation(libs.bundles.dagger)

    // Compose
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.material)
}
