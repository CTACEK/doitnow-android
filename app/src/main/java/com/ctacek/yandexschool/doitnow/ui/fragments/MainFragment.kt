package com.ctacek.yandexschool.doitnow.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.factory
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels{factory()}
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ToDoItemAdapter
    private var showComplited: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.swipeContainer.setOnRefreshListener { binding.swipeContainer.isRefreshing = false }
        binding.completedTasks.text = getString(R.string.completed_title)

        val manager = LinearLayoutManager(context) // LayoutManager

        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
            findNavController().navigate(action)
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


        adapter =
            ToDoItemAdapter(object : ToDoItemAdapter.ToDoItemActionListener {
                override fun onItemCheck(item: Todoitem) {
                    viewModel.updateTask(item.id, !item.status)
                }

                override fun onItemRemove(item: Todoitem) {

                }

                override fun onItemDetails(item: Todoitem) {
                    editTaskInformation(item)
                }


            })


        binding.recyclerview.layoutManager = manager
        binding.recyclerview.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner) { adapter.items = it }

        viewModel.completedTasks.observe(viewLifecycleOwner) {
            binding.completedTasks.text =
                getString(R.string.completed_title, viewModel.completedTasks.value)
        }

    }

    private fun editTaskInformation(task: Todoitem) {
        val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(task.id)
        findNavController().navigate(action)
    }

}