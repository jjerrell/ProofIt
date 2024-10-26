package app.jjerrell.proofed.feature.notification

import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.uuid.Uuid

expect class NotificationHelper {
    fun showNotification(timerId: Uuid, remainingTime: Duration, title: String, message: String)
    fun showNotification(timerId: Uuid, title: String, message: String)
    fun clearNotification(timerId: Uuid)
}