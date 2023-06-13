package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ctacek.yandexschool.doitnow.App
import com.ctacek.yandexschool.doitnow.TaskNotFoundException
import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository
import java.util.Date
import kotlin.random.Random

class NewEditTaskViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {

    private val _editTask = MutableLiveData<Todoitem>()
    val editTask: LiveData<Todoitem> = _editTask


    fun loadTask(id: String) {
        try {
            _editTask.postValue(repository.getTaskById(id))
        } catch (e: TaskNotFoundException) {
            e.printStackTrace()
        }

    }

    fun createNewTask() {
        val index = repository.getItems().size
        _editTask.postValue(
            Todoitem(
                index.toString(),
                "",
                Priority.BASIC,
                null,
                false,
                Date(),
                null
            )
        )
    }

}