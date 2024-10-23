package app.jjerrell.proofed.feature.timer

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.jjerrell.proofed.feature.notification.ChannelIdentifier
import app.jjerrell.proofed.feature.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Service class for Android timer functionality.
 */
class TimerService : Service(), KoinComponent {

    // TODO: Remove in favor of TimerManager
    private val timerScope = CoroutineScope(Dispatchers.Default + Job()) // Coroutine scope for the timer

    // TODO: Refactor to use TimerManager
    private var remainingTime = 10.minutes

    // TODO: Use this
    private val timerManager by inject<TimerManager>()
    private val notificationHelper by inject<NotificationHelper>()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()  // Ensure the notification channel is created
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                buildNotification(remainingTime),
                FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
            )  // Start the service in the foreground
        } else {
            startForeground(
                NOTIFICATION_ID,
                buildNotification(remainingTime)
            )
        }

        // Start the timer in the coroutine
        timerScope.launch {
            while (remainingTime > Duration.ZERO) {
                delay(1000)  // Wait 1 second
                remainingTime -= 1.seconds  // Decrement the timer

                val notification = buildNotification(remainingTime)
                // Update the notification with the remaining time
                notificationHelper.showNotification(notification, NOTIFICATION_ID)
            }

            // Stop the service when the timer finishes
            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()
        }

        return START_NOT_STICKY  // Ensure the service doesn't restart automatically
    }

    override fun onDestroy() {
        super.onDestroy()
        timerScope.cancel()  // Cancel the coroutine to prevent memory leaks
    }

    override fun onBind(intent: Intent?): IBinder? = null  // Not used for this service

    // Helper function to build the notification
    private fun buildNotification(remainingTime: Duration): Notification =
        NotificationCompat.Builder(this, ChannelIdentifier)
            .setContentTitle("Timer Running")
            .setContentText("Remaining Time: $remainingTime")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)  // Prevents the notification sound from repeating
            .setOngoing(true)  // Keeps the notification persistent
            .build()

    // Helper function to create the notification channel (required for Android 8.0+)
    private fun createNotificationChannel() {
        notificationHelper.createNotificationChannel(
            importance = NotificationManager.IMPORTANCE_HIGH
        )
    }

    companion object {
        const val NOTIFICATION_ID = 1 // TODO: Refactor to use TimerManager's UUID hashcode

        fun startService(context: Context) {
            val intent = Intent(context, TimerService::class.java)
            context.startForegroundService(intent)
        }
    }
}