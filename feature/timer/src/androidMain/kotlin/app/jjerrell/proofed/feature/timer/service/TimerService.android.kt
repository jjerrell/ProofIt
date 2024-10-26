package app.jjerrell.proofed.feature.timer.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.jjerrell.proofed.feature.notification.ChannelIdentifier
import app.jjerrell.proofed.feature.notification.NotificationHelper
import app.jjerrell.proofed.feature.timer.TimerData
import app.jjerrell.proofed.feature.timer.util.toBundle
import app.jjerrell.proofed.feature.timer.util.toTimerServiceData
import kotlin.uuid.Uuid
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/** Service class for Android timer functionality. */
internal actual class TimerService : Service(), KoinComponent, ITimerService {
    private val context: Context by inject<Context>()
    private val notificationHelper by inject<NotificationHelper>()

    actual override fun startTimer(timerData: TimerData) {
        startService(context, timerData)
    }

    actual override fun updateTimer(timerData: TimerData) {
        if (timerData.remaining <= 0.seconds) {
            notificationHelper.showNotification(
                timerId = timerData.id,
                title = timerData.title,
                message = timerData.description ?: "Your timer has finished."
            )
        } else {
            val notification = buildNotification(timerData.remaining.toString())
            // Update the notification with the remaining time
            notificationHelper.showNotification(notification, timerData.id.hashCode())
        }
    }

    actual override fun stopTimer(timerData: TimerData) {
        stopService(context)
        removeNotification(timerData.id)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel() // Ensure the notification channel is created
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START_SERVICE" -> {
                val timerData = intent.extras?.toTimerServiceData() ?: return START_NOT_STICKY
                val serviceTimerId = timerData.timerId.hashCode()
                val notificationRemaining = timerData.timestampMillis.milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(
                        serviceTimerId,
                        buildNotification(notificationRemaining.toString()),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
                    ) // Start the service in the foreground
                } else {
                    startForeground(serviceTimerId, buildNotification(notificationRemaining.toString()))
                }
            }
            "STOP_SERVICE" -> {
                stopForeground(STOP_FOREGROUND_DETACH)
                stopSelf()
            }
        }

        return START_NOT_STICKY // Ensure the service doesn't restart automatically
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(context) // Ensure the service is stopped when the app is closed
    }

    override fun onBind(intent: Intent?): IBinder? = null // Not used for this service

    // Helper function to build the notification
    private fun buildNotification(remainingTime: String): Notification =
        NotificationCompat.Builder(context, ChannelIdentifier)
            .setContentTitle("Timer Running")
            .setContentText("Remaining Time: $remainingTime")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true) // Prevents the notification sound from repeating
            .setOngoing(true) // Keeps the notification persistent
            .build()

    // Helper function to create the notification channel (required for Android 8.0+)
    private fun createNotificationChannel() {
        notificationHelper.createNotificationChannel(
            importance = NotificationManager.IMPORTANCE_HIGH
        )
    }

    private fun removeNotification(id: Uuid) {
        notificationHelper.clearNotification(id)
    }

    companion object {
        private fun startService(context: Context, data: TimerData) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = "START_SERVICE"
                putExtras(data.toBundle())
            }
            context.startForegroundService(intent)
        }

        private fun stopService(context: Context) {
            val timerIntent = Intent(context, TimerService::class.java)
            timerIntent.action = "STOP_SERVICE" // Define the stop action
            context.startService(timerIntent) // Send the intent to stop the service
        }
    }
}
