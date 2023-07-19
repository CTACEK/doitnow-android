package com.ctacek.yandexschool

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.ctacek.yandexschool.tasks.CheckSizeTask
import com.ctacek.yandexschool.tasks.UploadTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import java.util.Locale

class UploadPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("pluginExtension", PluginExtension::class.java)

        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("'com.android.application' plugin required.")

        val android = project.extensions.findByType(BaseAppModuleExtension::class.java)
            ?: throw GradleException("'com.android.application' plugin required.")

        androidComponents.onVariants { variant ->
            val capVariantName = variant.name.capitalize(Locale.ROOT)
            val apkDit = variant.artifacts.get(SingleArtifact.APK)

            val uploadTask: TaskProvider<UploadTask> =
                project.tasks.register("uploadApkFor$capVariantName", UploadTask::class.java) {
                    apkDir.set(apkDit)
                    variantName.set(variant.name)
                    versionApp.set(android.defaultConfig.versionName)
                }

            if (extension.enableSizeCheck.get()) {
                val checkSizeTask: TaskProvider<CheckSizeTask> =
                    project.tasks.register(
                        "validateApkSizeFor$capVariantName",
                        CheckSizeTask::class.java
                    ) {
                        apkDir.set(apkDit)
                        legalSizeMB.set(extension.fileSizeLimitInMb.get())
                    }


                uploadTask.configure {
                    dependsOn(checkSizeTask)
                }
            }
        }
    }
}
