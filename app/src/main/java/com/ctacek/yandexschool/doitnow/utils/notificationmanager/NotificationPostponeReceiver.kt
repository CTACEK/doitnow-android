package com.ctacek.yandexschool.doitnow.utils.notificationmanager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.repository.Repository
import com.ctacek.yandexschool.doitnow.utils.Constants.ONE_DAY_IN_MILLIS
import com.ctacek.yandexschool.doitnow.utils.Constants.TAG_NOTIFICATION_TASK
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class NotificationPostponeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var repository: Repository

    override fun onReceive(context: Context, intent: Intent) {
        context.appComponent.injectNotificationPostponeReceiver(this)
        val gson = Gson()
        val item = gson.fromJson(intent.getStringExtra(TAG_NOTIFICATION_TASK), ToDoItem::class.java)
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(item.id.hashCode())
        try {
            coroutineScope.launch(Dispatchers.IO) {
                if (item.deadline != null) {
                    repository.changeItem(item.copy(deadline = Date(item.deadline!!.time + ONE_DAY_IN_MILLIS)))
                }
            }
        } catch (err: Exception) {
            Log.e(NotificationPostponeReceiver::class.java.simpleName, err.message.toString())
        }
    }
}