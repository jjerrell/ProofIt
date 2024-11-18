package app.jjerrell.proofed.ui.step

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jjerrell.proofed.feature.timer.TimerState
import app.jjerrell.proofed.ui.component.MenuItem
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProofStepMenuItem(
    modifier: Modifier = Modifier,
    sequence: ProofSequence,
    sequenceStep: ProofStep,
) {
    val viewModel: ProofStepViewModel =
        koinViewModel(key = sequenceStep.id.toHexString()) { parametersOf(sequence, sequenceStep) }
    val remainingTime by viewModel.remainingTime.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    var isExpanded: Boolean by remember { mutableStateOf(timerState == TimerState.Running) }
    Column(modifier = modifier) {
        MenuItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isExpanded = !isExpanded },
            icon = {},
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(viewModel.proofStep.name)
                    AnimatedVisibility(
                        visible = !isExpanded,
                        enter = slideInHorizontally { fullWidth -> fullWidth },
                        exit = slideOutHorizontally { fullWidth -> fullWidth }
                    ) {
                        if (timerState == TimerState.Running) {
                            Text(remainingTime.toString())
                        } else {
                            Text(viewModel.proofStep.duration.toString())
                        }
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = isExpanded,
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(remainingTime.toString())
                    Text(timerState.toString())
                }
                Button(onClick = viewModel::toggleTimer) {
                    Text(
                        text =
                            when (timerState) {
                                TimerState.Running -> "Stop"
                                else -> "Start"
                            }
                    )
                }
            }
        }
    }
}
