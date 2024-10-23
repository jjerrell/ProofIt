package app.jjerrell.proofed.feature.timer

import kotlin.time.Duration
import kotlin.uuid.Uuid

data class TimerData(
    val id: Uuid,
    val duration: Duration,
    var remaining: Duration = duration,
    var state: TimerState = TimerState.Idle
)

sealed interface TimerState {
    data object Idle: TimerState
    data object Running: TimerState
    data object Paused: TimerState
}
