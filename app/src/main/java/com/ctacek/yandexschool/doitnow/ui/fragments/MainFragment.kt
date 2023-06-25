package com.ctacek.yandexschool.doitnow.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.factory
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemActionListener
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { factory() }
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ToDoItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)

//        viewModel.completedTasks.observe(viewLifecycleOwner) {
//            binding.completedTasks.text =
//                getString(R.string.completed_title, viewModel.completedTasks.value)
//        }

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

        val manager = LinearLayoutManager(context)

        adapter = ToDoItemAdapter(object : ToDoItemActionListener {
            override fun onItemCheck(item: ToDoItem) {
                viewModel.changeTaskDone(item.id, item.done)
            }

            override fun onItemDetails(item: ToDoItem) {
                editTaskInformation(item)
            }
        })

        viewModel.getData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collect {
                adapter.setData(it)
            }
        }

        val itemTouchHelper = ItemTouchHelper(createCallbackSwipe())
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.recyclerview.layoutManager = manager
        binding.recyclerview.adapter = adapter

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", viewModel.modeAll)
    }

    private fun createCallbackSwipe(): ItemTouchHelper.SimpleCallback {
        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val item = adapter.getElement(position)
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteTask(item)
                    }

                    ItemTouchHelper.RIGHT -> {
                        viewModel.changeTaskDone(item.id, !item.done)
                        adapter.notifyItemChanged(position)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    requireContext(),
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_light_red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_light_green
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.baseline_check)
                    .setActionIconTint(
                        ContextCompat.getColor(
                            recyclerView.context,
                            android.R.color.white
                        )
                    )
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        return simpleCallback
    }

    private fun createListeners() {
        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
            findNavController().navigate(action)
        }

        binding.visibility.setOnClickListener {
            viewModel.changeMode()
            when (viewModel.modeAll){
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
                    binding.recyclerview.scrollToPosition(0)
                }
            }

        }
    }

    private fun editTaskInformation(task: ToDoItem) {
        val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(task.id)
        findNavController().navigate(action)
    }

}