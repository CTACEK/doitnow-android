package com.ctacek.yandexschool.doitnow.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.databinding.ItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox
import java.text.SimpleDateFormat

class ToDoItemAdapter(val toDoItemActionListener: ToDoItemActionListener) :
    RecyclerView.Adapter<ToDoItemAdapter.ItemViewHolder>() {

    private var items: List<Todoitem> = emptyList()
    fun setData(newData: List<Todoitem>) {
        val personDiffUtil = ItemDiffUtilCallback(
            oldList = items,
            newList = newData
        )
        val diffResult = DiffUtil.calculateDiff(personDiffUtil)
        items = newData
        diffResult.dispatchUpdatesTo(this)
    }

    fun getElement(position: Int): Todoitem {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)
//        binding.isCompleted.setOnClickListener(this)

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

//        holder.itemView.tag = item
//        holder.checkbox.tag = item

        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SimpleDateFormat")
    inner class ItemViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemTaskBinding.bind(itemView)
        private val dataFormat = SimpleDateFormat("d MMMM")


        fun bind(item: Todoitem) {
            binding.title.text = item.description
            binding.isCompleted.isChecked = item.status

            if (item.endDate != null) {
                binding.data.visibility = View.VISIBLE
                binding.data.text =
                    itemView.context.getString(
                        R.string.infodata,
                        item.endDate?.let { dataFormat.format(it) })
            } else {
                binding.data.visibility = View.GONE
            }

            if (item.status) {
                binding.data.visibility = View.GONE
                binding.title.paintFlags = binding.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.isCompleted.buttonTintList = AppCompatResources.getColorStateList(
                    itemView.context,
                    R.color.color_light_green
                )
                binding.priority.visibility = View.GONE
            } else {
                if (item.endDate != null) {
                    binding.data.visibility = View.VISIBLE
                }

                binding.title.paintFlags = binding.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                when (item.priority) {
                    Priority.LOW -> {
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

                    Priority.BASIC -> {
                        binding.priority.visibility = View.GONE
                        binding.isCompleted.buttonTintList =
                            AppCompatResources.getColorStateList(itemView.context, R.color.grey)

                    }

                    Priority.HIGH -> {
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
                item.status = binding.isCompleted.isChecked
                toDoItemActionListener.onItemCheck(item)
                notifyItemChanged(absoluteAdapterPosition)
            }


            itemView.setOnClickListener {
                toDoItemActionListener.onItemDetails(item)
            }
        }
    }

//    override fun onClick(view: View) {
//        val item: Todoitem = view.tag as Todoitem
//
//        when (view.id) {
//            R.id.isCompleted -> {
//                toDoItemActionListener.onItemCheck(item)
//                notifyItemChanged(items.indexOfFirst { it.id == item.id })
//            }
//
//            else -> toDoItemActionListener.onItemDetails(item)
//        }
//    }
}