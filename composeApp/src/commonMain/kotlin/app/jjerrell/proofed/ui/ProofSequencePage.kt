package app.jjerrell.proofed.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.ui.step.ProofStepMenuItem

@Composable
fun ProofSequencePage(modifier: Modifier = Modifier, proofSequence: ProofSequence) {
    LazyColumn(modifier = modifier) {
        items(items = proofSequence.steps, key = { it.id }) { stepItem ->
            var isExpanded by remember { mutableStateOf(false) }
            ProofStepMenuItem(
                isExpanded = isExpanded,
                stepItemId = stepItem.id,
                sequence = proofSequence,
                sequenceStep = stepItem,
                onClick = { isExpanded = !isExpanded }
            )
        }
    }
}
