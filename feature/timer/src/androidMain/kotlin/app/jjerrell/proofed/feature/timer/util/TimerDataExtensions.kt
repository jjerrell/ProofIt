package app.jjerrell.proofed.feature.timer.util

import android.os.Bundle
import app.jjerrell.proofed.feature.timer.TimerData
import app.jjerrell.proofed.feature.timer.TimerServiceData
import kotlin.uuid.Uuid

fun TimerData.toBundle(): Bundle {
    return Bundle().apply {
        putString("timerId", id.toHexString())
        putString("title", title)
        putString("message", description)
        putLong("timestampMillis", remaining.inWholeMilliseconds)
    }
}

fun Bundle.toTimerServiceData() = TimerServiceData(
    timerId = getString("timerId")?.let { Uuid.parseHex(it) } ?: Uuid.random(),
    title = getString("title") ?: "Timer Finished",
    message = getString("message") ?: "Your timer has finished.",
    timestampMillis = getLong("timestampMillis"),
)
