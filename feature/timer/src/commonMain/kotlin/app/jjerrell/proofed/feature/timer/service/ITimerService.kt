package app.jjerrell.proofed.feature.timer.service

import app.jjerrell.proofed.feature.timer.TimerData

/** Timer service abstraction */
interface ITimerService {
    fun startTimer(timerData: TimerData)

    fun updateTimer(timerData: TimerData)

    fun stopTimer(timerData: TimerData)
}
