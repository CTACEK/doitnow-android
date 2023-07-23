// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.navigation.safe.args) apply false
}

buildscript {
    dependencies {
        classpath(libs.navigation.safe.args.gradle.plugin)
        classpath(libs.coroutines)
    }
    repositories {
        google()
        mavenCentral()
    }
}
