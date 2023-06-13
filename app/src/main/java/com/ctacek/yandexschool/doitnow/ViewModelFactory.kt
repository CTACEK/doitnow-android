package com.ctacek.yandexschool.doitnow

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ctacek.yandexschool.doitnow.ui.fragments.MainViewModel
import com.ctacek.yandexschool.doitnow.ui.fragments.NewEditTaskViewModel

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(app.repository)
            }

            NewEditTaskViewModel::class.java -> {
                NewEditTaskViewModel(app.repository)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)