package app.jjerrell.proofed.feature.notification

import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.uuid.Uuid

actual class NotificationHelper {
    actual fun showNotification(
        timerId: Uuid,
        remainingTime: Duration,
        title: String,
        message: String
    ) {
    }

    actual fun scheduleTimerAlarm(
        timerId: Uuid,
        triggerTime: Instant,
        title: String,
        message: String
    ) {
    }
}