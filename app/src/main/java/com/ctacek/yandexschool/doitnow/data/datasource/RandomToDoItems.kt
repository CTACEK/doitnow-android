package com.ctacek.yandexschool.doitnow.data.datasource

import android.util.Log
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.github.javafaker.Faker
import kotlin.random.Random


class RandomToDoItems {

    private val faker = Faker.instance()

    private var tasks = hashMapOf<Int, ToDoItem>()


    init {
        val tempTasks = (0..50).map {
            ToDoItem(
                it.toString(),
                if (Random.nextBoolean()) faker.backToTheFuture().character()
                    .toString() else faker.harryPotter().quote(),
                getRandomPriority(),
                if (Random.nextBoolean()) faker.date().birthday() else null,
                Random.nextBoolean(),
                faker.date().birthday(),
                null
            )
        }.toMutableList()

        for (task in tempTasks) {
            tasks[task.id.toInt()] = task
        }
    }

    fun deleteTask(id: Int) {
        Log.i("DataSource", tasks[id].toString())
        tasks.remove(id)
    }

    fun getTaskById(id: Int): ToDoItem {
        return tasks[id] ?: ToDoItem()
    }

    fun createTask(newToDoItem: ToDoItem) {
        newToDoItem.id = (tasks.size + 1).toString()
        tasks[tasks.size + 1] = newToDoItem
    }

    fun updateStatusTask(id: String, status: Boolean) {
        tasks[id.toInt()]?.status = status
    }

    fun saveTask(newToDoItem: ToDoItem) {
        tasks[newToDoItem.id.toInt()] = newToDoItem
    }

    private fun getRandomPriority(): Priority {
        val list = listOf(Priority.LOW, Priority.BASIC, Priority.HIGH)
        return list[Random.nextInt(0, 3)]
    }

    fun getTasks() = tasks
}