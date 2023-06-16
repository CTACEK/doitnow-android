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
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.databinding.FragmentNewEditTaskBinding
import com.ctacek.yandexschool.doitnow.factory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class NewEditTaskFragment : Fragment(R.layout.fragment_new_edit_task) {

    private val viewModel: NewEditTaskViewModel by viewModels { factory() }
    private lateinit var binding: FragmentNewEditTaskBinding
    private val args: NewEditTaskFragmentArgs by navArgs()

    @SuppressLint("SimpleDateFormat")
    val dataFormat = SimpleDateFormat("d MMMM y")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewEditTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.newTaskArg != null) {
            viewModel.loadTask(args.newTaskArg.toString())
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

        binding.buttonDeleteTask.setOnClickListener {
            viewModel.deleteTask()
            findNavController().popBackStack()
            Toast.makeText(context, "You are deleted item!", Toast.LENGTH_SHORT).show()
        }

        binding.editText.setOnFocusChangeListener { _, _ -> viewModel.updateEditText(binding.editText.text.toString()) }

        binding.textviewDateBefore.setOnClickListener {
            showDateTimePicker()
        }

        viewModel.editTask.observe(viewLifecycleOwner) {
            binding.editText.setText(it.description)
            if (it.endDate != null) {
                binding.switchDataVisible.isActivated = true
                binding.textviewDateBefore.visibility = View.VISIBLE
                binding.textviewDateBefore.text =
                    it.endDate?.let { date -> dataFormat.format(date) }
            } else {
                binding.switchDataVisible.isActivated = false
                binding.textviewDateBefore.visibility = View.INVISIBLE
            }
            binding.textImportanceBody.text = it.priority.toString()
        }

        binding.switchDataVisible.setOnCheckedChangeListener { _, switched ->
            if (switched) {
                binding.textviewDateBefore.visibility = View.VISIBLE
                binding.textviewDateBefore.text = dataFormat.format(Date())
                showDateTimePicker()
            } else {
                binding.textviewDateBefore.visibility = View.INVISIBLE
                viewModel.deleteData()
            }
        }

        binding.buttonSaveCreate.setOnClickListener {
            if (binding.buttonSaveCreate.text == getString(R.string.save_button)) {
                if (binding.editText.text.isNullOrBlank()) {
                    Toast.makeText(context, "Enter some words!", Toast.LENGTH_SHORT).show()
                    binding.editText.error = "Enter some words!"
                    return@setOnClickListener
                }
                binding.editText.error = null
                viewModel.saveTask()
            } else {
                if (binding.editText.text.isNullOrBlank()) {
                    Toast.makeText(context, "Enter some words!", Toast.LENGTH_SHORT).show()
                    binding.editText.error = "Enter some words!"
                    return@setOnClickListener
                }
                binding.editText.error = null
                viewModel.createTask()
            }
            findNavController().popBackStack()
            Toast.makeText(
                context,
                "You are ${binding.buttonSaveCreate.text.toString().lowercase()} task!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.menuImportance.setOnClickListener {
            showImportancePopupMenu(binding.menuImportance)
        }

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
                    viewModel.updateImportance(Priority.BASIC)
                }

                R.id.menu_importance_low -> {
                    viewModel.updateImportance(Priority.LOW)
                }

                R.id.menu_importance_high -> {
                    viewModel.updateImportance(Priority.HIGH)
                }
            }
            true
        }

        popupMenu.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDateTimePicker() {
        var date = Date()
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        val simpleFormat = SimpleDateFormat("d MMMM")
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            date = calendar.time
            binding.textviewDateBefore.visibility = View.VISIBLE
            binding.textviewDateBefore.text = simpleFormat.format(date)
            viewModel.updateData(date)
            timePicker.show(childFragmentManager, "TAG")
        }

        datePicker.addOnCancelListener {
            viewModel.deleteData()
        }

        timePicker.addOnPositiveButtonClickListener {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            date = cal.time
            binding.textviewDateBefore.text = simpleFormat.format(date)
            viewModel.updateData(date)
        }

        timePicker.addOnCancelListener {
            viewModel.deleteData()
        }

        datePicker.show(childFragmentManager, "TAG")
    }
}