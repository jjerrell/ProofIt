package app.jjerrell.proofed.feature.timer

import kotlin.uuid.Uuid

data class TimerServiceData(
    val timerId: Uuid,
    val title: String,
    val message: String,
    val timestampMillis: Long,
)
