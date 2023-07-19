package com.ctacek.yandexschool.checksize

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

private const val SIZE_IN_MB : Long = 30

class CheckSizePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class.java)
            ?: throw GradleException("'com.android.application' plugin required.")

        androidComponents.onVariants { variant ->
            val capVariantName = variant.name.capitalize()
            val apkDit = variant.artifacts.get(SingleArtifact.APK)
            project.tasks.register("validateApkSizeFor$capVariantName", CheckSizeTask::class.java) {
                apkDir.set(apkDit)
                legalSizeMB = SIZE_IN_MB
            }
        }
    }
}