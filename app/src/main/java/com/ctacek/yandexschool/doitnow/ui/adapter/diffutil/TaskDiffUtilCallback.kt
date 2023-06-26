package com.ctacek.yandexschool.doitnow.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem

class TaskDiffUtilCallback() : DiffUtil.ItemCallback<ToDoItem>() {

    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean = oldItem == newItem

}