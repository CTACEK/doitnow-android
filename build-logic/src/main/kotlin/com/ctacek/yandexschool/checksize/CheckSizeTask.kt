package com.ctacek.yandexschool.checksize

import com.ctacek.yandexschool.TelegramApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class CheckSizeTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract var legalSizeMB: Long

    @TaskAction
    fun checkSizeAndSendMessage() {
        val api = TelegramApi(HttpClient(OkHttp))

        api.chatIds.forEach {
            val id = it
            runBlocking {
                apkDir.get().asFile.listFiles()
                    ?.filter { it.name.endsWith(".apk") }
                    ?.forEach {
                        val sizeInMb: Long = it.length() / (1024 * 1024)

                        println("FILE = ${it.absolutePath}, SIZE = $sizeInMb")

                        api.sendMessage(
                            "Size of current building apk file is $sizeInMb",
                            api.token,
                            id
                        )

                        if (sizeInMb >= legalSizeMB) {
                            api.sendMessage(
                                "Build and uploading this apk file are failed :c \n" +
                                        "File size is $sizeInMb MB, but current limit is $legalSizeMB MB.",
                                api.token,
                                id
                            )
                            throw GradleException("File size is $sizeInMb MB, but the limit is $legalSizeMB MB.")
                        }
                    }
            }
        }
    }
}
