package com.ctacek.yandexschool.doitnow.ui.fragment.main

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemActionListener
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeCallbackInterface
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeHelper
import com.ctacek.yandexschool.doitnow.utils.internetchecker.ConnectivityObserver
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragmentViewController(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentMainBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
) {
    private var internetState = viewModel.status.value
    private val adapter: ToDoItemAdapter get() = binding.recyclerview.adapter as ToDoItemAdapter

    fun setUpViews() {
        createListeners()
        setUpViewModel()
    }

    private fun createListeners() {
        with(binding) {
            swipeLayout.setOnRefreshListener {
                if (internetState == ConnectivityObserver.Status.Available) {
                    viewModel.loadNetworkList()
                    Toast.makeText(context, R.string.merging_data, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.no_internet_try_later, Toast.LENGTH_SHORT)
                        .show()
                }
                swipeLayout.isRefreshing = false
            }

            recyclerview.adapter = ToDoItemAdapter(object : ToDoItemActionListener {
                override fun onClickItem(idItem: String) {
                    val action =
                        MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(idItem)
                    navController.navigate(action)
                }

                override fun onClickCheck(item: ToDoItem) {
                    viewModel.updateItem(item.copy(done = !item.done))
                }
            })
            val helper = SwipeHelper(object : SwipeCallbackInterface {
                override fun onDelete(todoItem: ToDoItem) {
                    viewModel.deleteItem(todoItem)
                }

                override fun onChangeDone(todoItem: ToDoItem) {
                    viewModel.updateItem(todoItem.copy(done = !todoItem.done))
                }
            }, context)

            helper.attachToRecyclerView(recyclerview)

            logoutButton.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(
                    ContextThemeWrapper(
                        context, R.style.AlertDialogCustom
                    )
                )
                builder.apply {
                    val title = if (internetState == ConnectivityObserver.Status.Available) {
                        context.getString(R.string.you_want_get_out)
                    } else {
                        context.getString(R.string.you_want_get_out_offline)
                    }
                    setMessage(title)
                    setPositiveButton(context.getString(R.string.logout_button)) { _, _ ->
                        navController.navigate(R.id.action_mainFragment_to_loginFragment)
                    }
                }
                builder.show().create()
            }

            fab.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
                navController.navigate(action)
            }

            visibility.setOnClickListener {
                viewModel.changeMode()
                when (viewModel.visibility.value) {
                    true -> {
                        YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                        binding.visibility.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.visibility_24
                            )
                        )
                    }
                    false -> {
                        YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                        binding.visibility.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.visibility_off_24
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.status.collectLatest {
                    updateNetworkState(it)
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.visibility.collectLatest { visibilityState ->
                    updateStateUI(visibilityState)
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countComplete.collectLatest {
                    binding.completedTasks.text = context.getString(R.string.completed_title, it)
                }
            }
        }
        internetState = viewModel.status.value
    }

    private suspend fun updateStateUI(visibilityState: Boolean) {
        viewModel.tasks.collect { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (visibilityState) {
                        adapter.submitList(uiState.data.sortedBy { it.createdAt })
                    } else {
                        adapter.submitList(uiState.data.filter { !it.done }.sortedBy { it.createdAt })
                    }
                    with(binding) {
                        recyclerview.visibility = View.VISIBLE
                        noResultAnimationView.visibility = View.GONE
                    }
                }

                is UiState.Error -> Log.d("1", uiState.cause)
                is UiState.Start -> {
                    with(binding) {
                        recyclerview.visibility = View.GONE
                        noResultAnimationView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun updateNetworkState(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> {
                binding.networkStatus.imageTintList =
                    AppCompatResources.getColorStateList(context, R.color.green)
                if (internetState != status) {
                    Toast.makeText(context, R.string.available_network_state, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.loadNetworkList()
                }
            }

            else -> {
                binding.networkStatus.imageTintList =
                    AppCompatResources.getColorStateList(context, R.color.color_light_red)

                if (internetState != status) {
                    Toast.makeText(context, R.string.no_internet_try_later, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        internetState = status
    }
}
