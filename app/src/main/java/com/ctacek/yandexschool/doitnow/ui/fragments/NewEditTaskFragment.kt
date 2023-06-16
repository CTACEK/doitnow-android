package com.ctacek.yandexschool.doitnow.ui.fragments

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.databinding.FragmentNewEditTaskBinding
import com.ctacek.yandexschool.doitnow.factory
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class NewEditTaskFragment : Fragment(R.layout.fragment_new_edit_task) {

    private val viewModel: MainViewModel by viewModels { factory() }
    private lateinit var binding: FragmentNewEditTaskBinding
    private val args: NewEditTaskFragmentArgs by navArgs()
    private var currentTask = ToDoItem()
    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    @SuppressLint("SimpleDateFormat")
    val dataFormat = SimpleDateFormat("d MMMM y")

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewEditTaskBinding.inflate(layoutInflater)

        val id = args.newTaskArg

        if (id != null) {
            currentTask = viewModel.loadTask(args.newTaskArg.toString()).copy()
            createInitData(currentTask)

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
        }

        if (savedInstanceState != null) {
            currentTask = savedInstanceState.getSerializable("currentTask") as ToDoItem
            createInitData(currentTask)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDeleteTask.setOnClickListener {
            viewModel.deleteTask(currentTask.id)
            findNavController().navigate(R.id.action_newEditTaskFragment_to_mainFragment)
            Toast.makeText(context, "You are deleted item!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, "Enter some words!", Toast.LENGTH_SHORT).show()
                binding.editText.error = "Enter some words!"
                return@setOnClickListener
            }

            binding.editText.error = null
            currentTask.description = binding.editText.text.toString()

            if (binding.buttonSaveCreate.text == getString(R.string.save_button)) {
                viewModel.saveTask(currentTask)
            } else {
                viewModel.createTask(currentTask)
            }

            findNavController().navigate(R.id.action_newEditTaskFragment_to_mainFragment)
        }

        binding.toolbar.setNavigationOnClickListener {
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
            currentTask.endDate = date
        }

        datePicker.addOnNegativeButtonClickListener {
            if (currentTask.endDate == null) deleteDate()
        }

    }

    private fun makeImportance(priority: Priority) {
        when (priority) {
            Priority.BASIC -> {
                binding.textImportanceBody.text = priority.toString()
                currentTask.priority = Priority.BASIC
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_dark_gray
                    )
                )
            }

            Priority.LOW -> {
                binding.textImportanceBody.text = priority.toString()
                currentTask.priority = Priority.LOW
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_light_gray
                    )
                )
            }

            Priority.HIGH -> {
                binding.textImportanceBody.text = priority.toString()
                currentTask.priority = Priority.HIGH
                binding.textImportanceBody.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_light_red
                    )
                )
            }
        }
    }

    private fun createInitData(newItem: ToDoItem) {
        currentTask.id = newItem.id
        binding.editText.setText(newItem.description)
        makeImportance(newItem.priority)
        if (newItem.endDate != null) {
            binding.switchDataVisible.isChecked = true
            binding.textviewDateBefore.visibility = View.VISIBLE
            binding.textviewDateBefore.text = newItem.endDate?.let { dataFormat.format(it) }
        }
    }

    private fun deleteDate() {
        binding.switchDataVisible.isChecked = false
        binding.textviewDateBefore.visibility = View.INVISIBLE
        currentTask.endDate = null
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
                R.id.menu_importance_no -> {
                    makeImportance(Priority.BASIC)
                }

                R.id.menu_importance_low -> {
                    makeImportance(Priority.LOW)
                }

                R.id.menu_importance_high -> {
                    makeImportance(Priority.HIGH)
                }
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
        outState.putSerializable("currentTask", currentTask)
    }
}