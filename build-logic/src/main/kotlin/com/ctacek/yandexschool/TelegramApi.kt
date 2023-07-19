package com.ctacek.yandexschool

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentDisposition
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File


class TelegramApi(
    private val httpClient: HttpClient
) {

    val BASE_URL = "https://api.telegram.org"

    val token = "6342829209:AAEPTfifxdlW7e370cHErjQaVpGa3nOE6XA"
    val chatIds = listOf(
        "756263716",
//            "541852628"
    )

    suspend fun sendMessage(text: String, token: String, chatId: String) {
        val response = httpClient.post("$BASE_URL/bot${token}/sendMessage") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("text", text)
                    }
                )
            )
        }
        println(response.status.toString())
    }

    suspend fun uploadFile(
        file: File,
        fileName: String,
        caption: String,
        token: String,
        chatId: String
    ) {
        val response = httpClient.post("${BASE_URL}/bot${token}/sendDocument") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("document", file.readBytes(), Headers.build {
                            append(
                                HttpHeaders.ContentDisposition,
                                "${ContentDisposition.Parameters.FileName}=\"${fileName}\""
                            )
                        })
                        append("caption", caption)
                    }
                )
            )
        }
        println(response.status.toString())
    }
}