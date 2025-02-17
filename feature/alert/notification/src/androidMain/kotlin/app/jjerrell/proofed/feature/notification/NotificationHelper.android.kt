package app.jjerrell.proofed.feature.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.time.Duration
import kotlin.uuid.Uuid

const val ChannelIdentifier = "timer_channel"

actual class NotificationHelper(private val context: Context) {
    actual fun showNotification(
        timerId: Uuid,
        remainingTime: Duration,
        title: String,
        message: String
    ) {
        val notification =
            NotificationCompat.Builder(context, ChannelIdentifier)
                .setContentTitle(title)
                .setContentText("$remainingTime")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        showNotification(notification, timerId.hashCode())
    }

    actual fun showNotification(timerId: Uuid, title: String, message: String) {
        val notification =
            NotificationCompat.Builder(context, ChannelIdentifier)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        showNotification(notification, timerId.hashCode())
    }

    actual fun clearNotification(timerId: Uuid) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(timerId.hashCode())
    }

    fun showNotification(
        notification: Notification,
        notificationId: Int = notification.hashCode(),
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    fun createNotificationChannel(
        channelId: String = ChannelIdentifier,
        name: String = "Timer Notifications",
        description: String = "Notifications for Timer",
        importance: Int = NotificationManager.IMPORTANCE_HIGH
    ) {
        val channel =
            NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }

        // Register the channel with the system
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
