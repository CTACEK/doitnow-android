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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.factory
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { factory() }
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ToDoItemAdapter
    private var showCompleted: Boolean = true

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

        binding.swipeContainer.setOnRefreshListener {
            viewModel.notifyUpdates()
            binding.swipeContainer.isRefreshing = false
        }

        binding.completedTasks.text = getString(R.string.completed_title)

        val manager = LinearLayoutManager(context) // LayoutManager

        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
            findNavController().navigate(action)
        }

        binding.visibility.setOnClickListener {
            run {
                if (showCompleted) {
                    showCompleted = false
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility_off
                        )
                    )
                    viewModel.hideCompletedTasks()
                } else {
                    showCompleted = true
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility
                        )
                    )
                    viewModel.showCompletedTasks()
                    binding.recyclerview.scrollToPosition(0)
                }
            }
        }


        adapter = ToDoItemAdapter(object : ToDoItemAdapter.ToDoItemActionListener {
            override fun onItemCheck(item: Todoitem) {
                viewModel.updateTask(item.id, !item.status)
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
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteTask(adapter.items[position].id)
                        adapter.notifyItemRemoved(position)
//                        Snackbar.make(binding.recyclerview, deletedMovie, Snackbar.LENGTH_LONG)
//                            .setAction("Undo", View.OnClickListener {
//                                moviesList.add(position, deletedMovie)
//                                recyclerAdapter.notifyItemInserted(position)
//                            }).show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        viewModel.updateTask(
                            adapter.items[position].id,
                            !adapter.items[position].status
                        )
                        adapter.notifyItemChanged(position)
//                        Snackbar.make(recyclerView, "$movieName, Archived.", Snackbar.LENGTH_LONG)
//                            .setAction("Undo") {
//                                archivedMovies.remove(archivedMovies.lastIndexOf(movieName))
//                                moviesList.add(position, movieName)
//                                recyclerAdapter.notifyItemInserted(position)
//                            }.show()
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