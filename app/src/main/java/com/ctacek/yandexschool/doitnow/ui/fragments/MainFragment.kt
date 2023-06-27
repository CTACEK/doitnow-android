package com.ctacek.yandexschool.doitnow.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.factory
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeCallbackInterface
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeHelper
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemActionListener
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { factory() }
    private lateinit var binding: FragmentMainBinding
    private val adapter: ToDoItemAdapter get() = binding.recyclerview.adapter as ToDoItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)

        if (savedInstanceState != null) {
            when (savedInstanceState.getBoolean("mode")) {
                true -> binding.visibility.setImageResource(R.drawable.visibility_off)
                false -> binding.visibility.setImageResource(R.drawable.visibility)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createListeners()


        binding.recyclerview.adapter = ToDoItemAdapter(object : ToDoItemActionListener {
            override fun onClickCheck(idItem: String, done: Boolean) {
                viewModel.changeTaskDone(idItem, done)
            }

            override fun onClickItem(idItem: String) {
                val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(idItem)
                findNavController().navigate(action)
            }
        })

        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(todoItem: ToDoItem) {
                viewModel.deleteTask(todoItem)
            }

            override fun onChangeDone(todoItem: ToDoItem) {
                viewModel.changeTaskDone(todoItem.id, !todoItem.done)
            }

        }, requireContext())

        helper.attachToRecyclerView(binding.recyclerview)

        lifecycleScope.launch {
            viewModel.tasks.collectLatest {
                updateUI(it)
            }
        }

        lifecycleScope.launch {
            viewModel.countCompletedTask.collectLatest {
                binding.completedTasks.text = getString(R.string.completed_title, it)
            }
        }

    }

    private fun updateUI(list: List<ToDoItem>) {
        if(viewModel.modeAll) {
            adapter.submitList(list)
        }else{
            adapter.submitList(list.filter { !it.done })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", viewModel.modeAll)
    }

    private fun createListeners() {

        binding.swipeLayout.setOnRefreshListener {
            viewModel.startPatch()

            binding.swipeLayout.isRefreshing = false
        }

        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
            findNavController().navigate(action)
        }

        binding.visibility.setOnClickListener {
            viewModel.changeDone()
            when (viewModel.modeAll) {
                true -> {
                    YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility
                        )
                    )
                }

                false -> {
                    YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility_off
                        )
                    )
                }
            }

        }
    }

}