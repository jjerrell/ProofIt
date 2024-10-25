package app.jjerrell.proofed.feature.timer

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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

class TimerManager(
    id: Uuid,
    duration: Duration,
    private val timerService: TimerService
) : KoinComponent {
    private val dataStateFlow = MutableStateFlow(TimerData(id, duration))

    val timerStateFlow: StateFlow<TimerState> = dataStateFlow.map { it.state }.stateIn(
        CoroutineScope(Dispatchers.Default),
        SharingStarted.Lazily,
        TimerState.Idle
    )
    val remainingTimeFlow: StateFlow<Duration> = dataStateFlow.map { it.remaining }.stateIn(
        CoroutineScope(Dispatchers.Default),
        SharingStarted.Lazily,
        duration
    )

    private var timerJob: Job? = null

    fun start() {
        if (timerJob?.isActive == true) return

        dataStateFlow.update {
            it.copy(state = TimerState.Running).also { updatedState ->
                timerService.startTimer(updatedState)
            }
        }

        timerJob = CoroutineScope(Dispatchers.Default).launch {
            runTimer()
        }
    }

    fun pause() {
        if (dataStateFlow.value.state == TimerState.Running) {
            dataStateFlow.update { it.copy(state = TimerState.Paused) }
            timerJob?.cancel()
            timerJob = null
        }
    }

    fun stop() {
        dataStateFlow.update {
            it.copy(remaining = it.duration, state = TimerState.Idle).also {
                timerService.stopTimer(it)
            }
        }
        timerJob?.cancel()
        timerJob = null
    }

    private suspend fun runTimer() {
        while (dataStateFlow.value.remaining > Duration.ZERO &&
            dataStateFlow.value.state == TimerState.Running) {

            // Update the remaining time before delay to synchronize with tests
            dataStateFlow.update { currentState ->
                val newTime = currentState.remaining - 1.seconds
                currentState.copy(
                    remaining = newTime,
                    state = if (newTime <= Duration.ZERO) TimerState.Idle else TimerState.Running
                ).also {
                    timerService.updateTimer(it)
                }
            }

            delay(1000) // Delay after state update
        }

        stop()
    }
}