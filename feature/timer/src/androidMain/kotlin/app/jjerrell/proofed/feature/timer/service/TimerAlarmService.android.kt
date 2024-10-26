package app.jjerrell.proofed.feature.timer.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import app.jjerrell.proofed.feature.notification.NotificationHelper
import app.jjerrell.proofed.feature.timer.TimerData
import app.jjerrell.proofed.feature.timer.util.toBundle
import app.jjerrell.proofed.feature.timer.util.toTimerServiceData
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.milliseconds

internal actual class TimerAlarmService : BroadcastReceiver(), KoinComponent, ITimerService {
    private val context: Context by inject<Context>()
    private val notificationHelper by inject<NotificationHelper>()

    override fun onReceive(context: Context, intent: Intent) {
        val timerData = intent.extras?.toTimerServiceData() ?: return

        // Show the notification using NotificationHelper
        notificationHelper.showNotification(
            timerData.timerId,
            timerData.title,
            timerData.message
        )
    }

    actual override fun startTimer(timerData: TimerData) {
        startService(context, timerData)
    }

    actual override fun updateTimer(timerData: TimerData) { /* No-op */ }

    actual override fun stopTimer(timerData: TimerData) {
        stopService(context)
    }

    companion object {
        private fun startService(context: Context, data: TimerData) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager.canScheduleAlarm()) {
                val intent =
                    Intent(context, TimerAlarmService::class.java).apply {
                        action = "SCHEDULE_ALARM"
                        putExtras(data.toBundle())
                    }
                val pendingIntent =
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    Clock.System.now().plus(data.remaining).toEpochMilliseconds(),
                    pendingIntent
                )
            }
        }

        private fun stopService(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                alarmManager.cancelAll()
            } else {
                alarmManager.cancel(
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(context, TimerAlarmService::class.java),
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            }
        }

        private fun AlarmManager.canScheduleAlarm(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            canScheduleExactAlarms()
        } else {
            true
        }
    }
}
