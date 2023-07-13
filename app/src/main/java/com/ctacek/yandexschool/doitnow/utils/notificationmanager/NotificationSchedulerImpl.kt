package com.ctacek.yandexschool.doitnow.utils.notificationmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.utils.Constants.TAG_NOTIFICATION_TASK
import com.ctacek.yandexschool.doitnow.utils.Constants.ONE_HOUR_IN_MILLIS

import javax.inject.Inject

class NotificationSchedulerImpl @Inject constructor(private val context: Context) : NotificationScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: ToDoItem) {
        if (item. deadline != null
            && item. deadline!!. time >= System. currentTimeMillis() + ONE_HOUR_IN_MILLIS
            && !item. done
        ) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(TAG_NOTIFICATION_TASK, item.toString())
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
            item.deadline!!.time - ONE_HOUR_IN_MILLIS,
                PendingIntent.getBroadcast(
                    context,
                    item.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    override fun cancel(item: ToDoItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}