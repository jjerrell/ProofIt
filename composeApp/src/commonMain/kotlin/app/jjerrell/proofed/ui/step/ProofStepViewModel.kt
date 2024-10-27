package app.jjerrell.proofed.ui.step

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import app.jjerrell.proofed.feature.timer.TimerManager
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.model.ProofStep
import kotlin.uuid.Uuid
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ProofStepViewModel(private val proofStepId: Uuid) : ViewModel(), KoinComponent {
    private val timerManager: TimerManager by lazy {
        get<TimerManager> {
            org.koin.core.parameter.parametersOf(
                proofStepId,
                proofStep.duration,
                proofStep.isAlarmOnly,
                proofSequence.name + " - " + proofStep.name
            )
        }
    }

    val proofSequence: ProofSequence by derivedStateOf {
        ProofSequence.allSequences.first { sequence -> sequence.steps.any { it.id == proofStepId } }
    }

    val proofStep: ProofStep by derivedStateOf {
        proofSequence.steps.first { it.id == proofStepId }
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
