package com.ctacek.yandexschool.doitnow.ui.fragment.login

import androidx.lifecycle.ViewModel
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.utils.notificationmanager.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope,
    private val notificationsScheduler: NotificationScheduler
) : ViewModel() {

    fun deleteCurrentItems() {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteCurrentLocalItems()
        }
    }

    fun deleteAllNotifications(){
        notificationsScheduler.cancelAll()
    }
}
