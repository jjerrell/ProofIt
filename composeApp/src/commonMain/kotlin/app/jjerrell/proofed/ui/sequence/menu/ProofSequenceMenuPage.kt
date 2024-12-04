package app.jjerrell.proofed.ui.sequence.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.jjerrell.proofed.AppBarState
import app.jjerrell.proofed.ui.component.MenuItem
import app.jjerrell.proofed.ui.component.ProofAppBar
import app.jjerrell.proofed.ui.navigation.ProofScreen
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.sequence_count

@Composable
@Preview
fun ProofSequenceMenuPage(
    modifier: Modifier = Modifier,
    viewModel: ProofSequenceMenuPageViewModel,
    onCreateNewSequence: () -> Unit = {},
    onSequenceClick: (ProofSequence) -> Unit = {}
) {
    // Setup to ensure the list is refreshed when the screen is shown
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(lifecycle) {
        viewModel.getAllSequences()
        onDispose {}
    }
    // Setup the app bar
    val appBarState = remember { mutableStateOf(AppBarState()) }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProofAppBar(
                currentScreen = ProofScreen.Start,
                appBarState = appBarState.value,
                canNavigateBack = false,
                navigateUp = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewSequence,
                content = { Icon(Icons.Default.Add, "") },
            )
        }
    ) { consumedPadding ->
        val isLoadingOrError =
            viewModel.state is ProofSequenceMenuPageViewModel.State.Loading ||
                viewModel.state is ProofSequenceMenuPageViewModel.State.Error
        LazyColumn(
            modifier = modifier.padding(consumedPadding),
            verticalArrangement =
                if (isLoadingOrError) {
                    Arrangement.Center
                } else {
                    Arrangement.Top
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val capturedState = viewModel.state) {
                is ProofSequenceMenuPageViewModel.State.Loading,
                is ProofSequenceMenuPageViewModel.State.Success -> {
                    if (capturedState is ProofSequenceMenuPageViewModel.State.Loading) {
                        item { CircularProgressIndicator() }
                    }
                    val sequenceItems = capturedState.sequences
                    if (sequenceItems.isNullOrEmpty()) {
                        item {
                            Text(
                                text =
                                    "No proofing sequences yet! Tap the '+' button to create your first and start baking delicious bread.",
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        menuItems(sequenceItems, onSequenceClick)
                    }
                }
                is ProofSequenceMenuPageViewModel.State.Error -> {
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

private fun LazyListScope.menuItems(
    sequenceItems: List<ProofSequence>,
    onSequenceClick: (ProofSequence) -> Unit
) {
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
