package com.ctacek.yandexschool.doitnow

import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.RetrofitToDoSource
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestElement
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiResponseElement
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemResponseRequest
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.github.javafaker.Faker
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun main() {
    val service = RetrofitToDoSource().makeRetrofitService()
    val faker = Faker.instance()

    runBlocking {
//        val response = service.addTask(
//            lastKnownRevision = 77, ToDoApiRequestElement(ToDoItemResponseRequest(
//                    id = "789f6409-f94b-464b-9deb-20c79a693f77",
//                    text = faker.harryPotter().quote(),
//                    importance = Importance.IMPORTANT,
//                    deadline = faker.date().future(30, TimeUnit.DAYS).time,
//                    done = Random.nextBoolean(),
//                    created_at = faker.date().past(30, TimeUnit.DAYS).time,
//                    changed_at = Date().time,
//                    color = null,
//                    last_updated_by = "d2"
//                )
//            )
//        )

        val revision = 79

//        val response = service.updateTask(
//            lastKnownRevision = revision,
//            itemId = "789f6409-f94b-464b-9deb-20c79a693f77",
//            body = ToDoApiRequestElement(
//                ToDoItemResponseRequest(
//                    id = "789f6409-f94b-464b-9deb-20c79a693f77",
//                    text = faker.harryPotter().quote(),
//                    importance = Importance.IMPORTANT,
//                    deadline = faker.date().future(30, TimeUnit.DAYS).time,
//                    done = Random.nextBoolean(),
//                    created_at = faker.date().past(30, TimeUnit.DAYS).time,
//                    changed_at = Date().time,
//                    color = null,
//                    last_updated_by = "d2"
//                )
//            )
//        )

        val response = service.deleteTask(
            lastKnownRevision = revision, itemId = "789f6409-f94b-464b-9deb-20c79a693f77"
        )

        try {
            if (response.isSuccessful) {
                println(response.body()?.toString())
            } else {
                println("Error network operation failed with ${response.code()}")
            }

        } catch (e: HttpException) {
            println("Exception ${e.message}")
        } catch (e: Throwable) {
            println("Ooops: Something else went wrong")
        }
    }
}