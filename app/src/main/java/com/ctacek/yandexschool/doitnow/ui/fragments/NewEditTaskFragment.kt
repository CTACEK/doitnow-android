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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.databinding.FragmentNewEditTaskBinding
import com.ctacek.yandexschool.doitnow.factory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewEditTaskBinding.inflate(layoutInflater)
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
                viewModel.item.collectLatest {
                    if (currentTask.id == "-1") {
                        currentTask = it
                        createInitData(currentTask)
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
                gson.fromJson(savedInstanceState.getString("currentTask"), ToDoItem::class.java)
            createInitData(currentTask)
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
            viewModel.deleteTask(currentTask)
            val action = NewEditTaskFragmentDirections.actionNewEditTaskFragmentToMainFragment()
            findNavController().navigate(action)
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
                viewModel.updateTask(currentTask)
            } else {
                viewModel.createTask(currentTask)
            }

            val action = NewEditTaskFragmentDirections.actionNewEditTaskFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.toolbar.setNavigationOnClickListener {
            val action = NewEditTaskFragmentDirections.actionNewEditTaskFragmentToMainFragment()
            findNavController().navigate(action)
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
    }

    private fun createInitData(newItem: ToDoItem) {
        currentTask.id = newItem.id
        binding.editText.setText(newItem.description)
        makeImportance(newItem.importance)
        if (newItem.deadline != null) {
            binding.switchDataVisible.isChecked = true
            binding.textviewDateBefore.visibility = View.VISIBLE
            binding.textviewDateBefore.text = newItem.deadline?.let { dataFormat.format(it) }
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
                R.id.menu_importance_no -> {
                    makeImportance(Importance.BASIC)
                }

                R.id.menu_importance_low -> {
                    makeImportance(Importance.LOW)
                }

                R.id.menu_importance_high -> {
                    makeImportance(Importance.IMPORTANT)
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
        val gson = Gson()
        outState.putString("currentTask", gson.toJson(currentTask))
    }
}