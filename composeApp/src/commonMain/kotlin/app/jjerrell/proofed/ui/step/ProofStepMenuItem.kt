package app.jjerrell.proofed.ui.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.model.ProofStep
import app.jjerrell.proofed.ui.component.MenuItem
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProofStepMenuItem(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    stepItemId: Uuid,
    sequence: ProofSequence,
    sequenceStep: ProofStep,
    onClick: () -> Unit
) {
    val viewModel: ProofStepViewModel =
        koinViewModel(key = stepItemId.toHexString()) { parametersOf(sequence, sequenceStep) }
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
