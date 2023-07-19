package com.ctacek.yandexschool.uploadtg

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.VariantOutput
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.plugin.extraProperties

class UploadPlugin : Plugin<Project> {

    override fun apply(project: Project) {

//        val android = project.extensions.findByType(BaseAppModuleExtension::class.java)
//            ?: throw GradleException("'com.android.application' plugin required.")

        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("'com.android.application' plugin required.")

        androidComponents.onVariants { variant ->
            val capVariantName = variant.name.capitalize()
            val apkDit = variant.artifacts.get(SingleArtifact.APK)


            project.tasks.register("uploadApkFor$capVariantName", UploadTask::class.java) {
                apkDir.set(apkDit)
                variantName = variant.name
                versionApp = variant.flavorName ?: "Unknown"
            }

            project.tasks.named("uploadApkFor$capVariantName") {
                dependsOn("validateApkSizeFor$capVariantName")
            }
        }
    }

//    override fun apply(project: Project) {
//        project.plugins.withId("com.android.application") {
//            val android = project.extensions.getByType<BaseAppModuleExtension>()
//
//            android.applicationVariants.all { variant ->
//                val capVariantName = variant.name.capitalize()
//
//                val apkDir = project.objects.directoryProperty()
//                apkDir.set(variant.outputs.single().outputFile.parentFile)
//
//                project.tasks.register("uploadApkFor$capVariantName", UploadTask::class.java) {
//                    this.apkDir.set(apkDir)
//                    this.variantName = variant.name
//                    this.versionApp = variant.versionName
//                }
//
//                project.tasks.named("uploadApkFor$capVariantName") {
//                    dependsOn("validateApkSizeFor$capVariantName")
//                }
//                true
//            }
//        }
//    }
}