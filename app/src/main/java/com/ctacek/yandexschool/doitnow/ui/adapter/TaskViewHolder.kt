package com.ctacek.yandexschool.doitnow.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.databinding.ItemTaskBinding
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem

@SuppressLint("SimpleDateFormat")
class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
    var todoItem: ToDoItem? = null


    fun bind(item: ToDoItem, listener: ToDoItemActionListener) {
        this.todoItem = item

        binding.title.text = item.description
        binding.isCompleted.isChecked = item.done

        if (item.done) {
            binding.data.visibility = View.GONE
            binding.title.paintFlags = binding.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.isCompleted.buttonTintList = AppCompatResources.getColorStateList(
                itemView.context,
                R.color.color_light_green
            )
            binding.priority.visibility = View.GONE
        } else {
            binding.title.paintFlags = binding.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            if (item.deadline != null) {
                binding.data.visibility = View.VISIBLE
                binding.data.text = DateFormat.format("HH:mm, MMM dd yyyy", item.deadline!!).toString()
            } else {
                binding.data.visibility = View.GONE
            }

            when (item.importance) {
                Importance.LOW -> {
                    binding.priority.visibility = View.VISIBLE
                    binding.priority.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context,
                            R.drawable.priority_low_12
                        )
                    )
                    binding.isCompleted.buttonTintList =
                        AppCompatResources.getColorStateList(itemView.context, R.color.grey)
                }

                Importance.BASIC -> {
                    binding.priority.visibility = View.GONE
                    binding.isCompleted.buttonTintList =
                        AppCompatResources.getColorStateList(itemView.context, R.color.grey)

                }

                Importance.IMPORTANT -> {
                    binding.priority.visibility = View.VISIBLE
                    binding.priority.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context,
                            R.drawable.priority_high_12
                        )
                    )
                    binding.isCompleted.buttonTintList = AppCompatResources.getColorStateList(
                        itemView.context,
                        R.color.color_light_red
                    )
                }
            }
        }

        binding.isCompleted.setOnClickListener {
            listener.onClickCheck(item)
        }


        itemView.setOnClickListener {
            listener.onClickItem(item.id)
        }
    }

    companion object {
        fun create(parent: ViewGroup) = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let(::TaskViewHolder)
    }

}
