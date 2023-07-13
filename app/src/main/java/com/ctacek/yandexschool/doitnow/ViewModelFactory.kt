package com.ctacek.yandexschool.doitnow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.ui.fragment.login.LoginViewModel
import com.ctacek.yandexschool.doitnow.ui.fragment.main.MainViewModel
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.ManageTaskViewModel
import com.ctacek.yandexschool.doitnow.utils.internetchecker.NetworkConnectivityObserver
import com.ctacek.yandexschool.doitnow.utils.notificationmanager.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val repositoryImpl: ToDoItemsRepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope,
    private val notificationsScheduler: NotificationScheduler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> MainViewModel(
                repositoryImpl, connectivityObserver, sharedPreferences, coroutineScope
            )

            LoginViewModel::class.java ->
                LoginViewModel(repositoryImpl, coroutineScope, notificationsScheduler)

            ManageTaskViewModel::class.java ->
                ManageTaskViewModel(repositoryImpl, coroutineScope)

            else -> {
                error("Unknown view model class")
            }
        }
        return viewModel as T
    }
}
