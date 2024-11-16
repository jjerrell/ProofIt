package app.jjerrell.proofed.feature.timer.service

import app.jjerrell.proofed.feature.timer.TimerData

/** Long-term timer notification alarm service */
internal expect class TimerAlarmService : ITimerService {
    override fun startTimer(timerData: TimerData)

    override fun updateTimer(timerData: TimerData)

    override fun stopTimer(timerData: TimerData)
}
