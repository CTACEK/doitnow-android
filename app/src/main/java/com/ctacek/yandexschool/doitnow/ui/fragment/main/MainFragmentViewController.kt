package com.ctacek.yandexschool.doitnow.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.ctacek.yandexschool.doitnow.ui.activity.MainActivity
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemActionListener
import com.ctacek.yandexschool.doitnow.ui.adapter.ToDoItemAdapter
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeCallbackInterface
import com.ctacek.yandexschool.doitnow.ui.adapter.swipe.SwipeHelper
import com.ctacek.yandexschool.doitnow.utils.Constants.TIMER_END
import com.ctacek.yandexschool.doitnow.utils.Constants.TIMER_ONE_SECOND
import com.ctacek.yandexschool.doitnow.utils.Constants.TIMER_START
import com.ctacek.yandexschool.doitnow.utils.internetchecker.ConnectivityObserver
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragmentViewController(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentMainBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
    private val layoutInflater: LayoutInflater,
) {
    private var internetState = viewModel.status.value
    private val adapter: ToDoItemAdapter get() = binding.recyclerview.adapter as ToDoItemAdapter

    fun setUpViews() {
        createListeners()
        setUpViewModel()
        checkStatusNotification()
    }

    private fun checkStatusNotification() {
        if (viewModel.getStatusNotifications() == null) {
            if (Build.VERSION.SDK_INT >= 33) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showNotificationDialog()
            }
        }
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
                    showSnackbar(todoItem)
                }

                override fun onChangeDone(todoItem: ToDoItem) {
                    viewModel.updateItem(todoItem.copy(done = !todoItem.done))
                }
            }, context)

            helper.attachToRecyclerView(recyclerview)

            fab.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToNewEditTaskFragment(null)
                navController.navigate(action)
            }

            settingsButton.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_settingsFragment)
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
                        adapter.submitList(
                            uiState.data
                                .sortedWith(compareBy<ToDoItem, Long?>(nullsLast()) { it.deadline?.time }
                                    .thenBy { it.createdAt.time })
                        )
                    } else {
                        adapter.submitList(uiState.data.filter { !it.done }
                            .sortedWith(compareBy<ToDoItem, Long?>(nullsLast()) { it.deadline?.time }
                                .thenBy { it.createdAt.time }))
                    }
                    with(binding) {
                        recyclerview.visibility = View.VISIBLE
                        noResultAnimationView.visibility = View.GONE
                    }
                }

                is UiState.Start -> {
                    with(binding) {
                        recyclerview.visibility = View.GONE
                        noResultAnimationView.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> Log.d(
                    MainFragmentViewController::class.simpleName,
                    uiState.cause
                )
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

    @SuppressLint("InflateParams")
    private fun showSnackbar(deletedTask: ToDoItem) {
        val snackbar = Snackbar.make(binding.recyclerview, "", Snackbar.LENGTH_INDEFINITE)

        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        val customize = layoutInflater.inflate(R.layout.custom_snackbar, null)
        snackbar.view.setBackgroundColor(context.getColor(android.R.color.transparent))

        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout
        val timerText = customize.findViewById<TextView>(R.id.timer)
        val timerTitle = customize.findViewById<TextView>(R.id.title)

        timerTitle.text = context.getString(R.string.cancel_deleting, deletedTask.description)
        val cancel = customize.findViewById<TextView>(R.id.cancel)

        cancel.setOnClickListener {
            viewModel.addItem(deletedTask)
            snackbar.dismiss()
        }

        snackBarLayout.addView(customize, 0)

        val timer = object : CountDownTimer(TIMER_START, TIMER_END) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / TIMER_ONE_SECOND + 1).toString()
            }

            override fun onFinish() {
                snackbar.dismiss()
            }
        }

        timer.start()
        snackbar.show()
    }

    private val notificationPermissionLauncher =
        (context as MainActivity).registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.putStatusNotification(isGranted)
        }

    private fun showNotificationDialog() {
        val builder = MaterialAlertDialogBuilder(
            ContextThemeWrapper(
                context, R.style.AlertDialogCustom
            )
        )
        builder.apply {
            setTitle(context.getString(R.string.allow_notifications_dialog_title))
            setMessage(context.getString(R.string.allow_notification_dialog_body))
            setPositiveButton(context.getString(R.string.allow_button)) { _, _ ->
                viewModel.putStatusNotification(true)
            }
            setNegativeButton(context.getString(R.string.deny_button)) { _, _ ->
                viewModel.putStatusNotification(false)
            }
        }
        builder.show().create()
    }

}
