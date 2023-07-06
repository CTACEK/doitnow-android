package com.ctacek.yandexschool.doitnow.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.databinding.FragmentNewEditTaskBinding
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.utils.Constants.CURRENT_TASK_NAME
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID


class NewEditTaskFragment : Fragment(R.layout.fragment_new_edit_task) {

    private val viewModel: MainViewModel by activityViewModels { requireContext().appComponent.findViewModelFactory() }
    private lateinit var binding: FragmentNewEditTaskBinding
    private val args: NewEditTaskFragmentArgs by navArgs()
    private var currentTask = ToDoItem()
    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    @SuppressLint("SimpleDateFormat")
    val dataFormat = SimpleDateFormat("d MMMM y")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewEditTaskBinding.inflate(layoutInflater)
        requireContext().appComponent.injectAddEditFragment(this)

    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = args.newTaskArg

        if (id != null && savedInstanceState == null) {
            viewModel.loadTask(id)

            lifecycleScope.launch {
                viewModel.currentItem.collect {
                    currentTask = it
                    if (currentTask.id != "-1") {
                        createInitData()
                        createListeners()
                    }
                }
            }

            binding.buttonSaveCreate.text = getString(R.string.save_button)
            binding.buttonDeleteTask.compoundDrawableTintList =
                AppCompatResources.getColorStateList(requireContext(), R.color.color_light_red)
            binding.buttonDeleteTask.setTextColor(
                AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.color_light_red
                )
            )
        } else {
            binding.buttonSaveCreate.text = getString(R.string.create_button)
            binding.buttonDeleteTask.isEnabled = false
            createListeners()
        }

        if (savedInstanceState != null) {
            val gson = Gson()
            currentTask =
                gson.fromJson(savedInstanceState.getString(CURRENT_TASK_NAME), ToDoItem::class.java)
            createInitData()
            createListeners()

        }
        return binding.root
    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun makeImportance(importance: Importance) {
        when (importance) {
            Importance.BASIC -> {
                binding.textImportanceBody.text = importance.toString()
                currentTask.importance = Importance.BASIC
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_dark_gray
                    )
                )
            }

            Importance.LOW -> {
                binding.textImportanceBody.text = importance.toString()
                currentTask.importance = Importance.LOW
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_light_gray
                    )
                )
            }

            Importance.IMPORTANT -> {
                binding.textImportanceBody.text = importance.toString()
                currentTask.importance = Importance.IMPORTANT
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_light_red
                    )
                )
            }
        }
    }

    private fun createListeners() {
        binding.buttonDeleteTask.setOnClickListener {
            if (viewModel.status.value == ConnectivityObserver.Status.Available) {
                viewModel.deleteRemoteTask(currentTask.id)
            } else {
                Toast.makeText(
                    context,
                    R.string.unavailable_network_state_delete_later,
                    Toast.LENGTH_SHORT
                ).show()
            }

            viewModel.deleteTask(currentTask)
            viewModel.clearTask()

            findNavController().popBackStack()
        }

        binding.textviewDateBefore.setOnClickListener {
            showDateTimePicker()
        }

        binding.switchDataVisible.setOnCheckedChangeListener { _, switched ->
            if (switched) {
                binding.textviewDateBefore.visibility = View.VISIBLE
                binding.textviewDateBefore.text = dataFormat.format(Date())
                showDateTimePicker()
            } else {
                deleteDate()
            }
        }

        binding.buttonSaveCreate.setOnClickListener {
            if (binding.editText.text.isNullOrBlank()) {
                Toast.makeText(context, R.string.error_enter_tasks_text, Toast.LENGTH_SHORT).show()
                binding.editText.error = getString(R.string.error_enter_tasks_text)
                return@setOnClickListener
            }

            binding.editText.error = null
            currentTask.description = binding.editText.text.toString()
            currentTask.changedAt = Date()


            if (binding.buttonSaveCreate.text == getString(R.string.save_button)) {
                if (viewModel.status.value == ConnectivityObserver.Status.Available) {
                    viewModel.createRemoteTask(currentTask)
                } else {
                    Toast.makeText(
                        context,
                        R.string.unavailable_network_state_update_later,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.updateTask(currentTask)
            } else {
                if (viewModel.status.value == ConnectivityObserver.Status.Available) {
                    viewModel.updateRemoteTask(currentTask)
                } else {
                    Toast.makeText(
                        context,
                        R.string.unavailable_network_state_create_later,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                currentTask.id = UUID.randomUUID().toString()
                viewModel.createTask(currentTask)
            }

            viewModel.clearTask()
            findNavController().popBackStack()
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.clearTask()
            findNavController().popBackStack()
        }

        binding.menuImportance.setOnClickListener {
            showImportancePopupMenu(binding.menuImportance)
        }

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
            currentTask.deadline = date
        }

        datePicker.addOnNegativeButtonClickListener {
            if (currentTask.deadline == null) deleteDate()
        }

        datePicker.addOnCancelListener {
            if (currentTask.deadline == null) deleteDate()
        }
    }

    private fun createInitData() {
        currentTask.id = currentTask.id
        binding.editText.setText(currentTask.description)
        makeImportance(currentTask.importance)

        if (currentTask.deadline != null) {
            binding.switchDataVisible.isChecked = true
            binding.textviewDateBefore.visibility = View.VISIBLE
            binding.textviewDateBefore.text = currentTask.deadline?.let { dataFormat.format(it) }
        }
    }

    private fun deleteDate() {
        binding.switchDataVisible.isChecked = false
        binding.textviewDateBefore.visibility = View.INVISIBLE
        currentTask.deadline = null
    }

    private fun showImportancePopupMenu(v: View) {
        val popupMenu = PopupMenu(context, v, Gravity.TOP)
        popupMenu.menuInflater.inflate(R.menu.menu_importance, popupMenu.menu)

        val highImportanceItem = popupMenu.menu.getItem(2)
        val spannable = SpannableString(highImportanceItem.title.toString())
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(v.context, R.color.color_light_red)),
            0,
            spannable.length,
            0
        )
        highImportanceItem.title = spannable

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->

            when (menuItem.itemId) {
                R.id.menu_importance_no -> makeImportance(Importance.BASIC)
                R.id.menu_importance_low -> makeImportance(Importance.LOW)
                R.id.menu_importance_high -> makeImportance(Importance.IMPORTANT)
            }
            true
        }
        popupMenu.show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun showDateTimePicker() {
        datePicker.show(requireActivity().supportFragmentManager, "materialDatePicker")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val gson = Gson()
        outState.putString(CURRENT_TASK_NAME, gson.toJson(currentTask))
    }
}