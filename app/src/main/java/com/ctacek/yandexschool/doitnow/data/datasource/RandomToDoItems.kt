package com.ctacek.yandexschool.doitnow.data.datasource

import android.util.Log
import com.ctacek.yandexschool.doitnow.TaskNotFoundException
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.github.javafaker.Faker
import kotlin.random.Random


class RandomToDoItems {

    private val faker = Faker.instance()

    private var tasks = mutableListOf<Todoitem>()


    init {
        tasks = (0..40).map {
            Todoitem(
                it.toString(),
                faker.backToTheFuture().character().toString(),
                getRandomPriority(),
                null,
                Random.nextBoolean(),
                faker.date().birthday(),
                null
            )
        }.toMutableList()

        tasks.add(
            Todoitem(
                "50",
                faker.backToTheFuture().character().toString(),
                Priority.LOW,
                faker.date().birthday(),
                false,
                faker.date().birthday(),
                null
            )
        )
    }

    fun deleteTask(id: String) {
        val indexToDelete = tasks.indexOfFirst { it.id == id }
        if (indexToDelete != -1) {
            Log.i("DataSource", tasks[indexToDelete].toString())
            tasks.removeAt(indexToDelete)
        }
    }

    fun getTaskById(id: String): Todoitem {
        return tasks.firstOrNull { it.id == id } ?: throw TaskNotFoundException()
    }

    fun createTask(newToDoItem: Todoitem){
        tasks.add(newToDoItem)
    }

    fun updateStatusTask(id: String, status: Boolean) {
        val indexToUpdate = tasks.indexOfFirst { it.id == id }
        if (indexToUpdate != -1) {
            tasks[indexToUpdate].status = status
        }
    }

    fun saveTask(newToDoItem: Todoitem) {
        val indexToUpdate = tasks.indexOfFirst { it.id == newToDoItem.id }
        if (indexToUpdate != -1) {
            tasks[indexToUpdate] = newToDoItem
        }
    }

    private fun getRandomPriority(): Priority {
        val list = listOf(Priority.LOW, Priority.BASIC, Priority.HIGH)
        return list[Random.nextInt(0, 3)]
    }

    fun getTasks() = tasks
}