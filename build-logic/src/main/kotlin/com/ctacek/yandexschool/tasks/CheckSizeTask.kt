package com.ctacek.yandexschool.tasks

import com.ctacek.yandexschool.TelegramApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class CheckSizeTask : DefaultTask() {

    @get:Input
    abstract val legalSizeMB: Property<Int>

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun checkSizeAndSendMessage() {
        val api = TelegramApi(HttpClient(OkHttp))

        runBlocking {
            apkDir.get().asFile.listFiles()
                ?.filter { it.name.endsWith(".apk") }
                ?.forEach {
                    val sizeInMb: Long = it.length() / (1024 * 1024)

                    println("FILE = ${it.absolutePath}, SIZE = $sizeInMb")

                    api.sendMessage("Size of current building apk file is $sizeInMb")

                    if (sizeInMb >= legalSizeMB.get()) {
                        api.sendMessage(
                            "Build and uploading this apk file are failed :c \n" +
                                    "File size is $sizeInMb MB, but current limit is ${legalSizeMB.get()} MB.",
                        )
                        throw GradleException("File size is $sizeInMb MB, but the limit is ${legalSizeMB.get()} MB.")
                    }
                }
        }
    }
}
