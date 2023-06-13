package com.ctacek.yandexschool.doitnow.data.datasource

import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.github.javafaker.Faker
import kotlin.random.Random

typealias ItemListener = (items: List<Todoitem>) -> Unit

class RandomToDoItems {

    private val faker = Faker.instance()

    private var tasks = mutableListOf<Todoitem>()

    private var listeners = mutableListOf<ItemListener>()

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

    fun deleteTask(todoitem: Todoitem) {
        val indexToDelete = tasks.indexOfFirst { it.id == todoitem.id }
        if (indexToDelete != -1) {
            tasks.removeAt(indexToDelete)
        }
        notifyChanges()
    }

    fun editTask(id: String, status: Boolean) {
        tasks[id.toInt()].status = status
        notifyChanges()
    }

    private fun getRandomPriority(): Priority {
        val list = listOf(Priority.LOW, Priority.BASIC, Priority.HIGH)
        return list[Random.nextInt(0, 3)]
    }
    private fun notifyChanges() = listeners.forEach { it.invoke(tasks) }

    fun getTasks() = tasks
}