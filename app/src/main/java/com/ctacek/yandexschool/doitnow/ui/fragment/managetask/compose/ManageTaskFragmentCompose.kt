package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.domain.model.ThemeMode
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.eventsholders.ManageAction
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageItemFragmentCompose : Fragment() {

    private val viewModel: ManageTaskComposeViewModel
            by viewModels { requireContext().appComponent.findViewModelFactory() }

    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    private val args: ManageItemFragmentComposeArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireContext().appComponent.injectManageTaskFragment(this)

        val id = args.newTaskArg

        if (id != null) {
            viewModel.getItem(id)
        } else {
            viewModel.setItem()
        }

        return ComposeView(requireContext()).apply {
            setContent {
                YandexTodoTheme(
                    darkTheme =
                    when (sharedPreferences.getThemeMode()) {
                        ThemeMode.DARK -> true
                        ThemeMode.LIGHT -> false
                        ThemeMode.SYSTEM -> isSystemInDarkTheme()
                    }
                ) {

                    ManageTaskScreen(
                        uiState = viewModel.todoItem.collectAsState(),
                        onEvent = viewModel::handleEvent
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.actions.collect(::handleActions)
            }
        }
    }

    private fun handleActions(action: ManageAction) {
        when (action) {
            ManageAction.NavigateBack -> findNavController().popBackStack()
        }
    }
}
