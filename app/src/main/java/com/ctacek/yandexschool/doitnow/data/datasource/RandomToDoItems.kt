package com.ctacek.yandexschool.doitnow.data.datasource

import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.github.javafaker.Faker
import kotlin.random.Random

typealias ItemListener = (persons: List<Todoitem>) -> Unit

class RandomToDoItems {

    private val faker = Faker.instance()

    private var tasks = mutableListOf<Todoitem>()

    private var listeners = mutableListOf<ItemListener>()

    init {
        tasks = (1..40).map {
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

    fun getToDoItems(): MutableList<Todoitem> {
        return tasks
    }

    fun editTaks(id: String, status: Boolean){
        tasks[id.toInt()].status = status
    }

    private fun getRandomPriority(): Priority {
        val list = listOf(Priority.LOW, Priority.STANDARD, Priority.HIGH)
        return list[Random.nextInt(0, 3)]
    }

    fun addListener(listener: ItemListener) {
        listeners.add(listener)
        listener.invoke(tasks)
    }

    fun removeListener(listener: ItemListener) {
        listeners.remove(listener)
        listener.invoke(tasks)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(tasks) }

    fun getTasks() = tasks
}