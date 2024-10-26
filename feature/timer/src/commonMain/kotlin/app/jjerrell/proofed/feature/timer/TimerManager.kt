package app.jjerrell.proofed.feature.timer

import app.jjerrell.proofed.feature.timer.service.TimerAlarmService
import app.jjerrell.proofed.feature.timer.service.TimerService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class TimerManager
internal constructor(
    id: Uuid,
    duration: Duration,
    isAlarm: Boolean,
    private val timerService: TimerService,
    private val alarmService: TimerAlarmService,
) : KoinComponent {
    private val dataStateFlow =
        MutableStateFlow(TimerData(id = id, duration = duration, isAlarm = isAlarm))

    val timerStateFlow: StateFlow<TimerState> =
        dataStateFlow
            .map { it.state }
            .stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Lazily, TimerState.Idle)
    val remainingTimeFlow: StateFlow<Duration> =
        dataStateFlow
            .map { it.remaining }
            .stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Lazily, duration)

    private var timerJob: Job? = null

    fun start() {
        if (timerJob?.isActive == true) return

        dataStateFlow.update {
            it.copy(state = TimerState.Running).also { updatedTimer ->
                if (updatedTimer.isAlarm) {
                    alarmService.startTimer(updatedTimer)
                } else {
                    timerService.startTimer(updatedTimer)
                }
            }
        }

        timerJob = CoroutineScope(Dispatchers.Default).launch { runTimer() }
    }

    fun pause() {
        if (dataStateFlow.value.state == TimerState.Running) {
            dataStateFlow.update {
                it.copy(state = TimerState.Paused).also { updatedTimer ->
                    // Stop the alarm since any resume will result in a new trigger time
                    if (updatedTimer.isAlarm) {
                        alarmService.stopTimer(updatedTimer)
                    }
                }
            }
            timerJob?.cancel()
            timerJob = null
        }
    }

    fun stop(isForced: Boolean = false) {
        dataStateFlow.update {
            it.copy(remaining = it.duration, state = TimerState.Idle).also { updatedTimer ->
                if (isForced) {
                    if (updatedTimer.isAlarm) {
                        alarmService.stopTimer(updatedTimer)
                    } else {
                        timerService.stopTimer(updatedTimer)
                    }
                }
            }
        }
        timerJob?.cancel()
        timerJob = null
    }

    private suspend fun runTimer() {
        while (
            dataStateFlow.value.remaining > Duration.ZERO &&
                dataStateFlow.value.state == TimerState.Running
        ) {

            // Update the remaining time before delay to synchronize with tests
            dataStateFlow.update {
                val newTime = it.remaining - 1.seconds
                it.copy(
                        remaining = newTime,
                        state =
                            if (newTime <= Duration.ZERO) TimerState.Idle else TimerState.Running
                    )
                    .also { updatedTimer ->
                        if (updatedTimer.isAlarm) {
                            alarmService.updateTimer(updatedTimer)
                        } else {
                            timerService.updateTimer(updatedTimer)
                        }
                    }
            }

            delay(1000) // Delay after state update
        }

        stop()
    }
}
