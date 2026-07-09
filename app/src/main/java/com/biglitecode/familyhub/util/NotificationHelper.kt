package com.biglitecode.familyhub.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Local notifications for task assignment alerts.
 */
object NotificationHelper {

    const val CHANNEL_ID = "familyhub_tasks"
    private const val CHANNEL_NAME = "Task Notifications"
    private var notificationId = 1000

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when a new task is assigned in FamilyHub"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun showTaskAssignedNotification(
        context: Context,
        taskTitle: String,
        assignedToName: String
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("New Task Assigned")
            .setContentText("$taskTitle has been assigned to $assignedToName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId++, notification)
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS denied — fail silently so the add-task flow still works.
        }
    }
}
