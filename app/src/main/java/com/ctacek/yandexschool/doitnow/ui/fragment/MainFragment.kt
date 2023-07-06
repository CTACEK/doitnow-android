package com.ctacek.yandexschool.doitnow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.model.LoadingState
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeCallbackInterface
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeHelper
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemActionListener
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver.Status.Unavailable
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver.Status.Available
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver.Status.Lost
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver.Status.Losing


import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels { requireContext().appComponent.findViewModelFactory() }
    private lateinit var binding: FragmentMainBinding
    private val adapter: ToDoItemAdapter get() = binding.recyclerview.adapter as ToDoItemAdapter
    private var internetState = Unavailable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)
        requireContext().appComponent.injectMainFragment(this)

        if (savedInstanceState != null) {
            when (savedInstanceState.getBoolean("mode")) {
                true -> binding.visibility.setImageResource(R.drawable.visibility_off_24)
                false -> binding.visibility.setImageResource(R.drawable.visibility_24)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launch {
            viewModel.loadingState.collectLatest {
                updateLoadingStatus(it)
            }
        }

        lifecycleScope.launch {
            viewModel.status.collectLatest {
                updateNetworkState(it)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createListeners()
        viewModel.loadLocalData()

        binding.recyclerview.adapter = ToDoItemAdapter(object : ToDoItemActionListener {
            override fun onClickCheck(item: ToDoItem) {
                if (internetState == Available) {
                    viewModel.updateRemoteTask(item)
                } else {
                    Toast.makeText(
                        context,
                        "No internet connection, will upload with later. Continue offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.changeTaskDone(item)
            }

            override fun onClickItem(idItem: String) {
                val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(idItem)
                findNavController().navigate(action)
            }
        })

        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(todoItem: ToDoItem) {
                if (internetState == Available) {
                    viewModel.deleteRemoteTask(todoItem.id)
                } else {
                    Toast.makeText(
                        context,
                        "No internet connection, will upload later. Continue offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.deleteTask(todoItem)
            }

            override fun onChangeDone(todoItem: ToDoItem) {
                if (internetState == Available) {
                    viewModel.updateRemoteTask(todoItem)
                } else {
                    Toast.makeText(
                        context,
                        "No internet connection, will upload later. Continue offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.changeTaskDone(todoItem)
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

    private fun updateLoadingStatus(loadingState: LoadingState<Any>) {
        when (loadingState) {
            is LoadingState.Loading -> {
                binding.recyclerview.visibility = View.GONE
                binding.noResultAnimationView.visibility = View.VISIBLE
            }

            is LoadingState.Success -> {
                binding.recyclerview.visibility = View.VISIBLE
                binding.noResultAnimationView.visibility = View.GONE
            }

            is LoadingState.Error -> {
                binding.recyclerview.visibility = View.VISIBLE
                binding.noResultAnimationView.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    R.string.loading_failed_showing_local_data,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateNetworkState(status: ConnectivityObserver.Status) {
        when (status) {
            Available -> {
                binding.networkStatus.imageTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.green)
                if (internetState != status) {
                    Toast.makeText(context, R.string.available_network_state, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.loadRemoteList()
                }

            }

            Unavailable -> {
                binding.networkStatus.imageTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.color_light_red)
                if (internetState != status) {
                    Toast.makeText(context, R.string.unavailable_network_state, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.loadRemoteList()
                }
            }

            Losing -> {
                binding.networkStatus.imageTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.orange)

                if (internetState != status) {
                    Toast.makeText(context, R.string.losing_network_state, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            Lost -> {
                binding.networkStatus.imageTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.color_light_red)

                if (internetState != status) {
                    Toast.makeText(context, R.string.lost_network_state, Toast.LENGTH_SHORT).show()
                }
            }
        }
        internetState = status
    }

    private fun updateUI(list: List<ToDoItem>) {
        if (viewModel.showAll) {
            adapter.submitList(list)
        } else {
            adapter.submitList(list.filter { !it.done })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", viewModel.showAll)
    }

    private fun createListeners() {

        binding.swipeLayout.setDistanceToTriggerSync(200)

        binding.swipeLayout.setOnRefreshListener {
            if (internetState == Available) {
                viewModel.loadRemoteList()
                Snackbar.make(
                    requireView(),
                    R.string.merging_data, Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    requireView(),
                    R.string.no_internet_try_later,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            binding.swipeLayout.isRefreshing = false
        }

        binding.logoutButton.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(
                ContextThemeWrapper(
                    context,
                    R.style.AlertDialogCustom
                )
            )
            builder.apply {
                val title = if (internetState == Available) {
                    getString(R.string.you_want_get_out)
                } else {
                    getString(R.string.you_want_get_out_offline)
                }
                setMessage(title)
                setPositiveButton(getString(R.string.logout_button)
                ) { _, _ ->
                    viewModel.deleteToken()
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                }
            }
            builder.show()
                .create()

        }

        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
            findNavController().navigate(action)
        }

        binding.visibility.setOnClickListener {
            viewModel.changeDone()
            when (viewModel.showAll) {
                true -> {
                    YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility_24
                        )
                    )
                    binding.recyclerview.scrollToPosition(0)
                }

                false -> {
                    YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                    binding.visibility.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.visibility_off_24
                        )
                    )
                    binding.recyclerview.scrollToPosition(0)
                }
            }

        }
    }

}