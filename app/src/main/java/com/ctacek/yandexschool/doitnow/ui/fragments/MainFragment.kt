package com.ctacek.yandexschool.doitnow.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.factory
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemActionListener
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { factory() }
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ToDoItemAdapter
    private var isSwitched = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", isSwitched)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)

        if (savedInstanceState != null) {
            isSwitched = savedInstanceState.getBoolean("mode")
            when (isSwitched) {
                true -> {
                    binding.visibility.setImageResource(R.drawable.visibility_off)
                }

                false -> {
                    binding.visibility.setImageResource(R.drawable.visibility)
                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeContainer.setOnRefreshListener {
            viewModel.getTasks(isSwitched)
            binding.swipeContainer.isRefreshing = false
        }

        binding.completedTasks.text = getString(R.string.completed_title)

        val manager = LinearLayoutManager(context) // LayoutManager

        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
            findNavController().navigate(action)
        }

        binding.visibility.setOnClickListener {
            if (isSwitched) {
                isSwitched = false
                YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility
                    )
                )
            } else {
                isSwitched = true
                YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility_off
                    )
                )
            }
            viewModel.getTasks(isSwitched)
        }


        adapter = ToDoItemAdapter(object : ToDoItemActionListener {
            override fun onItemCheck(item: Todoitem) {
                viewModel.updateTask(item.id, item.status)
            }

            override fun onItemDetails(item: Todoitem) {
                editTaskInformation(item)
            }
        })


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
                        viewModel.deleteTask(item.id)
                        adapter.notifyDataSetChanged()
                    }

                    ItemTouchHelper.RIGHT -> {
                        viewModel.updateTask(item.id, !item.status)
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
                    .addSwipeRightActionIcon(R.drawable.baseline_add_24)
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

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.recyclerview.layoutManager = manager
        binding.recyclerview.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner) { updateRecycler(it) }

        viewModel.completedTasks.observe(viewLifecycleOwner) {
            binding.completedTasks.text =
                getString(R.string.completed_title, viewModel.completedTasks.value)
        }

    }

    private fun updateRecycler(items: List<Todoitem>) {
        adapter.setData(items)
        binding.recyclerview.scrollToPosition(0)
    }

    private fun editTaskInformation(task: Todoitem) {
        val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(task.id)
        findNavController().navigate(action)
    }

}