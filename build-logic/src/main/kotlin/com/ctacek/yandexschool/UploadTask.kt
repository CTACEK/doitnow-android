package com.ctacek.yandexschool

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UploadTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))


        val token = System.getenv("TG_TOKEN")
        val chatId = System.getenv("TG_CHAT_ID")

        runBlocking {
            apkDir.get().asFile.listFiles()
                ?.filter { it.name.endsWith(".apk") }
                ?.forEach {
                    println("FILE = ${it.absolutePath}")
                    api.uploadFile(
                        it,
                        token,
                        chatId
                    )
                }
        }
    }
}

//        val properties = Properties()
//
//        FileInputStream("local.properties").use { fileInputStream ->
//            properties.load(fileInputStream)
//        }

//        val token = properties.getProperty("TG_TOKEN")
//        val chatId = properties.getProperty("TG_CHAT_ID")
