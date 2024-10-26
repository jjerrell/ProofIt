package app.jjerrell.proofed.feature.timer.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import app.jjerrell.proofed.feature.notification.NotificationHelper
import app.jjerrell.proofed.feature.timer.TimerData
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

internal actual class TimerAlarmService : BroadcastReceiver(), KoinComponent, ITimerService {
    private val notificationHelper by inject<NotificationHelper>()

    override fun onReceive(context: Context, intent: Intent) {
        // Extract the title and message from the intent (optional)
        val title = intent.getStringExtra("title") ?: "Timer Finished"
        val message = intent.getStringExtra("message") ?: "Your timer has finished."
        val timerId = intent.getStringExtra("timerId")?.let { Uuid.parseHex(it) }
        //        val triggerTime = intent.getLongExtra("triggerTime", -1)
        //            .takeUnless { it < 0 }
        //            ?.let {
        //                Duration.
        //            }

        // Show the notification using NotificationHelper
        notificationHelper.showNotification(timerId ?: Uuid.random(), 0.seconds, title, message)
    }

    actual override fun startTimer(timerData: TimerData) {
        TODO("Not yet implemented")
    }

    actual override fun updateTimer(timerData: TimerData) {
        TODO("Not yet implemented")
    }

    actual override fun stopTimer(timerData: TimerData) {
        TODO("Not yet implemented")
    }

    companion object {
        private fun startService(context: Context, data: TimerData) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerAlarmService::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + data.remaining.inWholeMilliseconds,
                pendingIntent
            )
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

        // TODO: Possibly remove
        private fun scheduleTimerAlarm(
            context: Context,
            timerId: Uuid,
            triggerTime: Instant,
            title: String,
            message: String
        ) {
            val duration =
                Clock.System.now()
                    .until(triggerTime, DateTimeUnit.SECOND)
                    .toDuration(DurationUnit.SECONDS)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            //            createNotificationChannel()

            // Create an intent for the TimerAlarmReceiver
            val intent =
                Intent(context, TimerAlarmService::class.java).apply {
                    action = "SCHEDULE_ALARM"
                    putExtra("timerId", timerId.toHexString())
                    putExtra("triggerTime", triggerTime.toEpochMilliseconds())
                    putExtra("title", title)
                    putExtra("message", message)
                }

            // Create a PendingIntent that will trigger the broadcast receiver
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

            if (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    alarmManager.canScheduleExactAlarms()
                } else {
                    true
                }
            ) {
                // Schedule the alarm
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, // Use RTC_WAKEUP to wake the device if it is asleep
                    triggerTime.toEpochMilliseconds(),
                    pendingIntent
                )
            }
        }
    }
}
