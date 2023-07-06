package com.ctacek.yandexschool.doitnow.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.ui.adapter.diffutil.TaskDiffUtilCallback

class ToDoItemAdapter(private val toDoItemActionListener: ToDoItemActionListener) :
    ListAdapter<ToDoItem, TaskViewHolder>(TaskDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder.create(parent)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position), toDoItemActionListener)
    }

}

