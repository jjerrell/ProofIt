package app.jjerrell.proofed.ui.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import app.jjerrell.proofed.feature.timer.TimerManager
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.model.ProofStep
import app.jjerrell.proofed.ui.component.MenuItem
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class ProofStepViewModel(private val proofStepId: Uuid) : ViewModel(), KoinComponent {
    private val timerManager: TimerManager by lazy {
        get<TimerManager> {
            parametersOf(
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

@Composable
fun ProofStepMenuItem(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    stepItemId: Uuid,
    onClick: () -> Unit
) {
    val viewModel: ProofStepViewModel =
        koinViewModel(key = stepItemId.toHexString()) { parametersOf(stepItemId) }
    LaunchedEffect(isExpanded) { viewModel.toggleTimer(isExpanded) }
    Column(modifier = modifier) {
        MenuItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            icon = {},
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(viewModel.proofStep.name)
                    Text(viewModel.proofStep.duration.toString())
                }
            }
        )
        if (isExpanded) {
            val remainingTime by viewModel.remainingTime.collectAsState()
            val timerState by viewModel.timerState.collectAsState()
            Text(remainingTime.toString())
            Text(timerState.toString())
        }
    }
}
