package app.jjerrell.proofed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.ui.step.ProofStepMenuItem
import kotlin.uuid.Uuid

@Composable
fun ProofSequencePage(
    modifier: Modifier = Modifier,
    sequenceId: Uuid,
    viewModel: ProofSequencePageViewModel
) {
    val isLoadingOrError = viewModel.state is ProofSequencePageViewModel.State.Loading || viewModel.state is ProofSequencePageViewModel.State.Error
    if (viewModel.state == null) {
        LaunchedEffect(Unit) {
            viewModel.getSequence(sequenceId)
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = if (isLoadingOrError) {
                Arrangement.Center
            } else {
                Arrangement.Top
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val capturedState = viewModel.state) {
                ProofSequencePageViewModel.State.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                is ProofSequencePageViewModel.State.Success -> items(items = capturedState.sequence.steps, key = { it.id }) { stepItem ->
                    ProofStepMenuItem(sequence = capturedState.sequence, sequenceStep = stepItem)
                }
                is ProofSequencePageViewModel.State.Error -> {
                    item {
                        Text(text = "We're sorry, but something went wrong. Please try again.")
                        TextButton(
                            onClick = { viewModel.getSequence(sequenceId) }
                        ) {
                            Text(text = "Try Again")
                        }

                    }
                }
                null -> {}
            }
        }
    }
}
