package com.ctacek.yandexschool.doitnow.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.databinding.ItemTaskBinding
import java.text.SimpleDateFormat

class ToDoItemAdapter(val toDoItemActionListener: ToDoItemActionListener) :
    RecyclerView.Adapter<ToDoItemAdapter.ItemViewHolder>() {

    private var items: List<ToDoItem> = emptyList()
    fun setData(newData: List<ToDoItem>) {
        val personDiffUtil = ItemDiffUtilCallback(
            oldList = items,
            newList = newData
        )
        val diffResult = DiffUtil.calculateDiff(personDiffUtil)
        items = newData
        diffResult.dispatchUpdatesTo(this)
    }

    fun getElement(position: Int): ToDoItem {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SimpleDateFormat")
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemTaskBinding.bind(itemView)
        private val dataFormat = SimpleDateFormat("d MMMM")


        fun bind(item: ToDoItem) {
            binding.title.text = item.description
            binding.isCompleted.isChecked = item.done

            if (item.deadline != null) {
                binding.data.visibility = View.VISIBLE
                binding.data.text =
                    itemView.context.getString(
                        R.string.infodata,
                        item.deadline?.let { dataFormat.format(it) })
            } else {
                binding.data.visibility = View.GONE
            }

            if (item.done) {
                binding.data.visibility = View.GONE
                binding.title.paintFlags = binding.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.isCompleted.buttonTintList = AppCompatResources.getColorStateList(
                    itemView.context,
                    R.color.color_light_green
                )
                binding.priority.visibility = View.GONE
            } else {
                if (item.deadline != null) {
                    binding.data.visibility = View.VISIBLE
                }

                binding.title.paintFlags =
                    binding.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                when (item.importance) {
                    Importance.LOW -> {
                        binding.priority.visibility = View.VISIBLE
                        binding.priority.setImageDrawable(
                            AppCompatResources.getDrawable(
                                itemView.context,
                                R.drawable.priority1
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

                    Importance.HIGH -> {
                        binding.priority.visibility = View.VISIBLE
                        binding.priority.setImageDrawable(
                            AppCompatResources.getDrawable(
                                itemView.context,
                                R.drawable.priority3
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
                item.done = binding.isCompleted.isChecked
                toDoItemActionListener.onItemCheck(item)
                notifyItemChanged(absoluteAdapterPosition)
            }


            itemView.setOnClickListener {
                toDoItemActionListener.onItemDetails(item)
            }
        }
    }

}