package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ctacek.yandexschool.doitnow.TaskNotFoundException
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository
import java.util.Date

class NewEditTaskViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {

    private val _editTask = MutableLiveData<Todoitem>()
    val editTask: LiveData<Todoitem> = _editTask

    private var currentTask = Todoitem(
        "9999", "",
        Priority.BASIC,
        null,
        false,
        Date(),
        Date()
    )

    fun loadTask(id: String) {
        try {
            currentTask = repository.getTaskById(id)
            _editTask.postValue(currentTask)
        } catch (e: TaskNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getDate() : Date? {
        return currentTask.endDate
    }

    fun createTask() {
        val indexOfCreatedItem = repository.getItems().maxBy { it.id }.id + 1
        currentTask.id = indexOfCreatedItem
        repository.createTask(currentTask)
    }

    fun updateData(data: Date){
        currentTask.endDate = data
        _editTask.postValue(currentTask)
    }

    fun updateImportance(newPriority: Priority){
        currentTask.priority = newPriority
        _editTask.postValue(currentTask)
    }

    fun updateEditText(text: String){
        currentTask.description = text
        _editTask.postValue(currentTask)
    }

    fun deleteData() {
        currentTask.endDate = null
        _editTask.postValue(currentTask)
    }
    fun saveTask() {
        repository.saveTask(currentTask)
    }

    fun deleteTask() {
        repository.deleteTask(currentTask.id)
    }

}