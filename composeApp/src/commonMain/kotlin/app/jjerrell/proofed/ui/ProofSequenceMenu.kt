package app.jjerrell.proofed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.ui.component.MenuItem
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.sequence_count

@Composable
@Preview
fun ProofSequenceMenu(
    modifier: Modifier = Modifier,
    sequences: List<ProofSequence>? = null,
    onSequenceClick: (ProofSequence) -> Unit = {}
) {
    LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val sequenceItems = sequences?.takeUnless { it.isEmpty() } ?: ProofSequence.allSequences
        items(sequenceItems, key = { it.id }) {
            MenuItem(
                modifier = Modifier.fillMaxWidth(),
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
                        Text(
                            pluralStringResource(
                                Res.plurals.sequence_count,
                                it.steps.size,
                                it.steps.size
                            )
                        )
                    }
                }
            )
        }
    }
}
