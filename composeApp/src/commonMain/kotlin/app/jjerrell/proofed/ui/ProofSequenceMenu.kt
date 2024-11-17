package app.jjerrell.proofed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.ui.component.MenuItem
import dev.jjerrell.proofed.feature.data.api.model.ProofSequence
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.sequence_count

@Composable
@Preview
fun ProofSequenceMenu(
    modifier: Modifier = Modifier,
    viewModel: ProofSequenceMenuViewModel,
    onSequenceClick: (ProofSequence) -> Unit = {}
) {
    val isLoadingOrError =
        viewModel.state is ProofSequenceMenuViewModel.State.Loading ||
            viewModel.state is ProofSequenceMenuViewModel.State.Error
    if (viewModel.state == null) {
        LaunchedEffect(Unit) { viewModel.getAllSequences() }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement =
                if (isLoadingOrError) {
                    Arrangement.Center
                } else {
                    Arrangement.Top
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val capturedState = viewModel.state) {
                ProofSequenceMenuViewModel.State.Loading -> {
                    item { CircularProgressIndicator() }
                }
                is ProofSequenceMenuViewModel.State.Success -> {
                    val sequenceItems = capturedState.sequences
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
                is ProofSequenceMenuViewModel.State.Error -> {
                    item {
                        Text(text = "We're sorry, but something went wrong. Please try again.")
                        TextButton(onClick = { viewModel.getAllSequences() }) {
                            Text(text = "Try Again")
                        }
                    }
                }
                null -> {}
            }
        }
    }
}
