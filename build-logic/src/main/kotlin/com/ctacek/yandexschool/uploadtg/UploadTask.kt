package com.ctacek.yandexschool.uploadtg

import com.ctacek.yandexschool.TelegramApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class UploadTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract var variantName: String

    @get:Input
    abstract var versionApp: String

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))

//        val properties = Properties()
//
//        FileInputStream("local.properties").use { fileInputStream ->
//            properties.load(fileInputStream)
//        }

//        val token = properties.getProperty("TG_TOKEN")
//        val chatId = properties.getProperty("TG_CHAT_ID")


        api.chatIds.forEach {
            val id = it
            runBlocking {
                apkDir.get().asFile.listFiles()
                    ?.filter { it.name.endsWith(".apk") }
                    ?.forEach {
                        println("FILE = ${it.absolutePath}")
                        api.uploadFile(
                            it,
                            "todolist-$variantName-$versionApp.apk",
                            "Successful apk build!",
                            api.token,
                            id
                        )
                    }
            }
        }

    }
}