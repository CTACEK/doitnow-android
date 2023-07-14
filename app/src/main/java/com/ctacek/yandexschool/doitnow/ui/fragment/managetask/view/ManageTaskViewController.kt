package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.databinding.FragmentManageTaskBinding
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import com.ctacek.yandexschool.doitnow.ui.activity.MainActivity
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

class ManageTaskViewController(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentManageTaskBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: ManageTaskViewModel,
) {

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()
    private lateinit var popupMenu: PopupMenu


    @SuppressLint("SimpleDateFormat")
    val dataFormat = SimpleDateFormat("d MMMM y")

    fun setUpViews() {
        setUpViewModel()
    }

    private fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItem.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            updateViewsInfo(state.data)
                            createListeners(state.data)
                            createPopupMenu(state.data)
                        }

                        else -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateViewsInfo(todoItem: ToDoItem) {
        binding.editText.setText(todoItem.description)

        makeImportance(todoItem.importance)

        if (todoItem.deadline != null) {
            binding.textviewDateBefore.visibility = View.VISIBLE
            binding.textviewDateBefore.text = dataFormat.format(todoItem.deadline!!).toString()
            binding.switchDataVisible.isChecked = true
        }


        if (todoItem.id != "-1") {
            binding.buttonSaveCreate.text = context.getString(R.string.save_button)
            binding.buttonDeleteTask.setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    R.color.color_light_red
                )
            )
            TextViewCompat.setCompoundDrawableTintList(
                binding.buttonDeleteTask, AppCompatResources.getColorStateList(
                    context,
                    R.color.color_light_red
                )
            )
            binding.buttonDeleteTask.isActivated = false
        } else {
            binding.buttonSaveCreate.text = context.getString(R.string.create_button)
        }

    }

    private fun createListeners(toDoItem: ToDoItem) {
        with(binding) {

            buttonDeleteTask.setOnClickListener {
                if (toDoItem.id != "-1") {
                    viewModel.deleteItem(toDoItem)
                    navController.popBackStack()
                }
            }

            textviewDateBefore.setOnClickListener { showDateTimePicker() }

            switchDataVisible.setOnCheckedChangeListener { _, switched ->
                if (switched) {
                    binding.textviewDateBefore.visibility = View.VISIBLE
                    binding.textviewDateBefore.text = dataFormat.format(Date())
                    showDateTimePicker()
                } else deleteDate(toDoItem)
            }

            editText.doAfterTextChanged {
                toDoItem.description = binding.editText.text.toString()
            }

            toolbar.setNavigationOnClickListener {
                navController.popBackStack()
            }

            menuImportance.setOnClickListener { popupMenu.show() }

            datePicker.addOnPositiveButtonClickListener {
                val date: Date
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                date = calendar.time
                binding.textviewDateBefore.visibility = View.VISIBLE
                binding.textviewDateBefore.text = dataFormat.format(date)
                toDoItem.deadline = date
            }

            datePicker.addOnNegativeButtonClickListener {
                if (toDoItem.deadline == null) deleteDate(toDoItem)
            }

            datePicker.addOnCancelListener {
                if (toDoItem.deadline == null) deleteDate(toDoItem)
            }

            buttonSaveCreate.setOnClickListener {
                if (binding.editText.text.isNullOrBlank()) {
                    Toast.makeText(context, R.string.error_enter_tasks_text, Toast.LENGTH_SHORT)
                        .show()
                    binding.editText.error = context.getString(R.string.error_enter_tasks_text)
                    return@setOnClickListener
                }
                binding.editText.error = null
                toDoItem.description = binding.editText.text.toString()
                toDoItem.changedAt = Date(System.currentTimeMillis())

                if (toDoItem.id == "-1") {
                    saveNewTask(toDoItem)
                } else {
                    updateTask(toDoItem)
                }

            }
        }
    }

    private fun saveNewTask(toDoItem: ToDoItem) {
        toDoItem.id = UUID.randomUUID().toString()

        viewModel.addItem(toDoItem)
        navController.popBackStack()
    }

    private fun updateTask(toDoItem: ToDoItem) {
        viewModel.updateItem(toDoItem)
        navController.popBackStack()
    }


    private fun deleteDate(toDoItem: ToDoItem) {
        binding.switchDataVisible.isChecked = false
        binding.textviewDateBefore.visibility = View.INVISIBLE
        toDoItem.deadline = null
    }

    private fun createPopupMenu(toDoItem: ToDoItem) {
        popupMenu = PopupMenu(context, binding.menuImportance)
        popupMenu.menuInflater.inflate(R.menu.menu_importance, popupMenu.menu)

        val highImportanceItem = popupMenu.menu.getItem(2)
        val spannable = SpannableString(highImportanceItem.title.toString())
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_light_red)),
            0,
            spannable.length,
            0
        )
        highImportanceItem.title = spannable

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->

            when (menuItem.itemId) {
                R.id.menu_importance_no -> {
                    makeImportance(Importance.BASIC)
                    toDoItem.importance = Importance.BASIC
                }

                R.id.menu_importance_low -> {
                    makeImportance(Importance.LOW)
                    toDoItem.importance = Importance.LOW
                }

                R.id.menu_importance_high -> {
                    makeImportance(Importance.IMPORTANT)
                    toDoItem.importance = Importance.IMPORTANT
                }
            }
            true
        }
    }

    private fun makeImportance(importance: Importance) {
        when (importance) {
            Importance.BASIC -> {
                binding.textImportanceBody.text = importance.toString()
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.color_dark_gray
                    )
                )
            }

            Importance.LOW -> {
                binding.textImportanceBody.text = importance.toString()
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.color_light_gray
                    )
                )
            }

            Importance.IMPORTANT -> {
                binding.textImportanceBody.text = importance.toString()
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.color_light_red
                    )
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDateTimePicker() {
        datePicker.show((context as MainActivity).supportFragmentManager, "materialDatePicker")
    }

}
