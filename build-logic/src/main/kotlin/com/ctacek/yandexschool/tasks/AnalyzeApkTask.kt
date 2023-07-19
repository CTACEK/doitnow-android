package com.ctacek.yandexschool.tasks

import com.ctacek.yandexschool.TelegramApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.IOException
import java.util.jar.JarFile

abstract class AnalyzeApkTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun analyzeApk() {
        val api = TelegramApi(HttpClient(OkHttp))

        api.chatIds.forEach { id ->
            runBlocking {
                apkDir.get().asFile.listFiles()
                    ?.filter { it.name.endsWith(".apk") }
                    ?.forEach { file ->
                        if (file.exists()) {
                            try {
                                var result = "Detailed output of the contents of the apk file:"
                                result += analyzeApkResources(file.toString())

                                api.sendMessage(
                                    result,
                                    api.token,
                                    id
                                )
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
            }
        }
    }

    private fun analyzeApkResources(apkFilePath: String): String {
        val jarFile = JarFile(apkFilePath)
        val entries = jarFile.entries()
        val files = mutableListOf<Pair<String, Double>>()
        var output = ""

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val entryName = entry.name
            val entrySize = entry.size

            if (!entry.isDirectory) {
                val fileSizeMB = entrySize.toDouble() / (1024 * 1024)
                if (fileSizeMB >= 0.1) {
                    files.add(Pair(entryName, fileSizeMB))
                }
            }
        }

        jarFile.close()

        files.sortByDescending { it.second }

        for (file in files) {
            val formattedSize = "%.2f".format(file.second)
            output += "\n- ${file.first} $formattedSize MB"
        }

        return output
    }
}