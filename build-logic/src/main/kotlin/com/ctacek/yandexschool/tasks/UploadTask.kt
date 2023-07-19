package com.ctacek.yandexschool.tasks

import com.ctacek.yandexschool.TelegramApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UploadTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val variantName: Property<String>

    @get:Input
    abstract val versionApp: Property<String>

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))

        api.chatIds.forEach { id ->
            runBlocking {
                apkDir.get().asFile.listFiles()
                    ?.filter { it.name.endsWith(".apk") }
                    ?.forEach { file ->
                        println("FILE = ${file.absolutePath}")
                        api.uploadFile(
                            file,
                            "todolist-${variantName.get()}-${versionApp.get()}.apk",
                            "Successful apk build!",
                            api.token,
                            id
                        )
                    }
            }
        }

    }
}
