package com.ctacek.yandexschool.doitnow.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.datasource.ItemListener
import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ToDoItemAdapter
    private var showComplited: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.swipeContainer.setOnRefreshListener { binding.swipeContainer.isRefreshing = false }
        binding.completedTasks.text = getString(R.string.completed_title)

        val manager = LinearLayoutManager(context) // LayoutManager
        adapter =
            ToDoItemAdapter(object : ToDoItemAdapter.ToDoItemActionListener { // Создание объекта
                override fun onItemCheck(item: Todoitem) {
                    viewModel.updateTask(item.id, item.status)
                }

                override fun onItemRemove(item: Todoitem) {
                    TODO("Not yet implemented")
                }

                override fun onItemGetId(item: Todoitem) {
                    TODO("Not yet implemented")
                }


            })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_newEditTaskFragment)
        }

        binding.visibility.setOnClickListener {
            run {
                if (showComplited) {
                    showComplited = false
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility_off
                        )
                    )
                    viewModel.hideCompletedTasks()
                } else {
                    showComplited = true
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility
                        )
                    )
                    viewModel.showCompletedTasks()
                }
            }
        }

        binding.recyclerview.layoutManager = manager
        binding.recyclerview.adapter = adapter

        val listener: ItemListener = { adapter.items = it }

        viewModel.getData().observe(viewLifecycleOwner) { adapter.items = it }

        viewModel.getCountCompleted().observe(viewLifecycleOwner) {
            binding.completedTasks.text =
                getString(R.string.completed_title, viewModel.getCountCompleted().value)
        }

    }

}