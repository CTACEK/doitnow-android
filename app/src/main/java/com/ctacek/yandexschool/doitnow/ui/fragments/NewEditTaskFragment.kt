package com.ctacek.yandexschool.doitnow.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.databinding.FragmentNewEditTaskBinding
import com.ctacek.yandexschool.doitnow.factory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import java.util.Date


class NewEditTaskFragment : Fragment(R.layout.fragment_new_edit_task) {

    private val viewModel: NewEditTaskViewModel by viewModels{factory()}
    private lateinit var binding: FragmentNewEditTaskBinding
    private val args : NewEditTaskFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewEditTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.newTaskArg != null){
            viewModel.loadTask(args.newTaskArg.toString())
            binding.button.text = "Save"
        } else{
            binding.button.text = "Create"
        }

        viewModel.editTask.observe(viewLifecycleOwner) {
            binding.title.text = it.description
        }

        binding.button.setOnClickListener {
            showDateTimePicker()
        }

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

    }

//    private fun initUpdate() {
//        taskInfo = args.newTaskArg!!.taskInfo
//        categoryInfo = args.newTaskArg!!.categoryInfo[0]
//        binding.fab.text = "Update"
//        colorString = categoryInfo.color
//        prevTaskCategory = TaskCategoryInfo(
//            TaskInfo(
//                taskInfo.id,
//                taskInfo.description,
//                taskInfo.date,
//                taskInfo.priority,
//                taskInfo.status,
//                taskInfo.category
//            ),
//            listOf(CategoryInfo(categoryInfo.categoryInformation, categoryInfo.color))
//        )
//        isCategorySelected = true
//    }

    private fun showDateTimePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
//            taskInfo.date = calendar.time
//            binding.dateAndTimePicker.text = DateToString.convertDateToString(taskInfo.date)
            timePicker.show(childFragmentManager, "TAG")
        }

        timePicker.addOnPositiveButtonClickListener {
            val cal = Calendar.getInstance()
//            cal.time = taskInfo.date
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
//            taskInfo.date = cal.time
//            binding.dateAndTimePicker.text = DateToString.convertDateToString(taskInfo.date)
        }
        datePicker.show(childFragmentManager, "TAG")
    }
}