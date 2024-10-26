package app.jjerrell.proofed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.ui.component.MenuItem

@Composable
fun ProofSequenceMenu(
    modifier: Modifier = Modifier,
    sequences: List<ProofSequence>? = null,
    onSequenceClick: (ProofSequence) -> Unit = {}
) {
    LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val sequenceItems =
            sequences?.takeUnless { it.isEmpty() }
                ?: listOf(ProofSequence.loafSequence, ProofSequence.feedingSequence)
        items(sequenceItems, key = { it.id }) {
            MenuItem(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onClick = { onSequenceClick(it) },
                icon = {
                    it.imageResourceUrl?.let {
                        // TODO: Remote image loading
                    }
                },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it.name)
                        Text("${it.steps.size} steps")
                    }
                }
            )
        }
    }
}
