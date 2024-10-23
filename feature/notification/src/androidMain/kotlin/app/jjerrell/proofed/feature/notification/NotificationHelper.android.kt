package app.jjerrell.proofed.feature.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlinx.datetime.Instant
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
        val notification = NotificationCompat.Builder(context, ChannelIdentifier)
            .setContentTitle(title)
            .setContentText("Remaining time: $remainingTime")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        showNotification(notification)
    }

    fun showNotification(
        notification: Notification,
        notificationId: Int = notification.hashCode(),
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    actual fun scheduleTimerAlarm(
        timerId: Uuid,
        triggerTime: Instant,
        title: String,
        message: String
    ) {
    }

    fun createNotificationChannel(
        channelId: String = ChannelIdentifier,
        name: String = "Timer Notifications",
        description: String = "Notifications for Timer",
        importance: Int = NotificationManager.IMPORTANCE_HIGH
    ) {
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }

        // Register the channel with the system
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}