package com.ctacek.yandexschool.doitnow.utils.notificationmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.utils.Constants
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduler: NotificationSchedulerImpl

    @Inject
    lateinit var coroutineScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        context.appComponent.injectNotificationReceiver(this)
        try {
            val gson = Gson()
            val task = gson.fromJson(
                intent.getStringExtra(Constants.TAG_NOTIFICATION_TASK),
                ToDoItem::class.java
            )

            coroutineScope.launch(Dispatchers.IO) {

                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                manager.createNotificationChannel(
                    NotificationChannel(
                        Constants.NOTIFICATION_CHANNEL_ID,
                        Constants.NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )

                val notification =
                    NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.hour_before_task))
                        .setContentText(
                            context.getString(
                                R.string.notification_text,
                                task.importance.toString().lowercase(Locale.ROOT),
                                task.description
                            )
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .addAction(
                            NotificationCompat.Action(
                                R.drawable.baseline_delay_24,
                                context.getString(R.string.postpone_for_a_day),
                                postponeIntent(context, task)
                            )
                        )
                        .setContentIntent(deepLinkIntent(context, task.id))
                        .build()

                scheduler.cancel(task)
                manager.notify(task.id.hashCode(), notification)
            }
        } catch (err: Exception) {
            Log.e(NotificationReceiver::class.java.simpleName, err.stackTraceToString())
        }
    }

    private fun deepLinkIntent(context: Context, newTaskArg: String): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_graph)
            .setDestination(R.id.manageTaskFragment, bundleOf("newTaskArg" to newTaskArg))
            .createPendingIntent()

    private fun postponeIntent(context: Context, item: ToDoItem): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            Intent(
                context,
                NotificationPostponeReceiver::class.java
            ).apply {
                putExtra(Constants.TAG_NOTIFICATION_TASK, item.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}
