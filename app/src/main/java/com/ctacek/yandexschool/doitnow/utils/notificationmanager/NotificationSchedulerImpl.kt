package com.ctacek.yandexschool.doitnow.utils.notificationmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.utils.Constants.ONE_HOUR_IN_MILLIS
import com.ctacek.yandexschool.doitnow.utils.Constants.TAG_NOTIFICATION_TASK
import javax.inject.Inject

class NotificationSchedulerImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferencesAppSettings
) : NotificationScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)


    override fun schedule(item: ToDoItem) {
        if (item.deadline != null) {
            if (item.deadline!!.time >= System.currentTimeMillis() + ONE_HOUR_IN_MILLIS
                && !item.done
                && sharedPreferences.getNotificationStatus() == true) {

                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra(TAG_NOTIFICATION_TASK, item.toString())
                }
                sharedPreferences.addNotification(item.id.hashCode().toString())
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
    }

    override fun cancel(item: ToDoItem) {
        sharedPreferences.removeNotification(item.id.hashCode().toString())
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAll() {
        for (task_id in sharedPreferences.getNotificationsIds().split(" ")) {
            sharedPreferences.removeNotification(task_id)
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    task_id.hashCode(),
                    Intent(context, NotificationReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }
}
