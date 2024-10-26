package app.jjerrell.proofed.feature.timer.service

import app.jjerrell.proofed.feature.timer.TimerData

/**
 * Short-term timer notification service
 */
internal expect class TimerService : ITimerService {
    override fun startTimer(
        timerData: TimerData
    )
    override fun updateTimer(
        timerData: TimerData
    )
    override fun stopTimer(
        timerData: TimerData
    )
}