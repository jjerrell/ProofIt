package app.jjerrell.proofed.ui.step

import androidx.lifecycle.ViewModel
import app.jjerrell.proofed.feature.timer.TimerManager
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.model.ProofStep
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ProofStepViewModel(val sequence: ProofSequence, val proofStep: ProofStep) :
    ViewModel(), KoinComponent {
    private val timerManager: TimerManager by lazy {
        get<TimerManager> {
            org.koin.core.parameter.parametersOf(
                proofStep.id,
                proofStep.duration,
                proofStep.isAlarmOnly,
                sequence.name + " - " + proofStep.name
            )
        }
    }

    val remainingTime = timerManager.remainingTimeFlow
    val timerState = timerManager.timerStateFlow

    fun toggleTimer(isExpanded: Boolean) {
        if (isExpanded) {
            timerManager.start()
        } else {
            timerManager.stop(isForced = true)
        }
    }
}
