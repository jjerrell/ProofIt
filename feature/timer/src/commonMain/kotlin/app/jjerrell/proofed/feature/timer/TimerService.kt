package app.jjerrell.proofed.feature.timer

expect class TimerService {
    fun startTimer(
        timerData: TimerData
    )
    fun updateTimer(
        timerData: TimerData
    )
    fun stopTimer(
        timerData: TimerData
    )
}