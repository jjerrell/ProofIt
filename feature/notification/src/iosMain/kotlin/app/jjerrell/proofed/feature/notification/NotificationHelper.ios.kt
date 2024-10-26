package app.jjerrell.proofed.feature.notification

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

    actual fun showNotification(
        timerId: Uuid,
        title: String,
        message: String
    ) {
    }

    actual fun clearNotification(timerId: Uuid) {

    }
}