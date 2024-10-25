package app.jjerrell.proofed.feature.notification

import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.uuid.Uuid

expect class NotificationHelper {
    fun showNotification(timerId: Uuid, remainingTime: Duration, title: String, message: String)
    fun scheduleTimerAlarm(timerId: Uuid, triggerTime: Instant, title: String, message: String)
    fun clearNotification(timerId: Uuid)
}