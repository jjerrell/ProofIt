package app.jjerrell.proofed.ui.sequence.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.jjerrell.proofed.AppBarState
import app.jjerrell.proofed.ui.component.MenuItem
import app.jjerrell.proofed.ui.component.ProofAppBar
import app.jjerrell.proofed.ui.navigation.ProofScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditProofSequencePage(
    modifier: Modifier = Modifier,
    viewModel: EditProofSequencePageViewModel,
    onNavigateUp: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(lifecycle) {
        viewModel.initializeState()
        onDispose { }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            ProofAppBar(
                currentScreen = ProofScreen.CreateSequence,
                appBarState = AppBarState(
                    actionItems = listOf(
                        AppBarState.ActionItem(
                            isEnabled = viewModel.action == EditProofSequencePageViewModel.Action.None,
                            description = "Save Sequence",
                            icon = Icons.Default.Check,
                            onClick = {
                                viewModel.validateAndSaveSequence()
                            }
                        )
                    )
                ),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.action == EditProofSequencePageViewModel.Action.None
            ) {
                FloatingActionButton(
                    modifier = Modifier,
                    onClick = { viewModel.addNewStepAction() },
                    content = { Icon(Icons.Default.Add, "") },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { consumedPadding ->
        Column(
            modifier = Modifier
                .padding(consumedPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (val currentState = viewModel.state) {
                is EditProofSequencePageViewModel.State.Loading -> {
                    Text(text = "Loading...")
                }
                is EditProofSequencePageViewModel.State.Success -> {
                    val sequenceNameIsError = remember { mutableStateOf(!viewModel.isValidSequenceName()) }
                    TextField(
                        value = viewModel.sequenceName,
                        onValueChange = {
                            sequenceNameIsError.value = !viewModel.validateAndSetSequenceName(it)
                        },
                        label = { Text(text = "Sequence Name") },
                        placeholder = { Text(text = "Sequence Name") },
                        trailingIcon = {
                            // Clear
                            IconButton(onClick = {
                                sequenceNameIsError.value = !viewModel.validateAndSetSequenceName("")
                            }) {
                                Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                            }
                        },
                        isError = sequenceNameIsError.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    currentState.sequence.steps.forEach { currentStep ->
                        MenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.editStepAction(currentStep) },
                            icon = {},
                            content = { Text(currentStep.name) }
                        )
                        val parameter = when (val currentAction = viewModel.action) {
                            is EditProofSequencePageViewModel.Action.EditStep -> currentAction.step
                            else -> null
                        }
                        val key = viewModel.action.hashCode() + (parameter?.hashCode() ?: 0)
                        AnimatedVisibility(
                            visible = parameter?.id == currentStep.id
                        ) {
                            EditProofStep(
                                viewModel = koinViewModel(key = key.toString()) { parametersOf(parameter) },
                                onCancel = { viewModel.cancelAction() },
                                onComplete = { viewModel.addStep(it) }
                            )
                        }
                    }
                }
                is EditProofSequencePageViewModel.State.Error -> {
                    Text(text = "Error")
                }
            }
            AnimatedVisibility(
                visible = viewModel.action == EditProofSequencePageViewModel.Action.AddStep
            ) {
                val key = viewModel.action.hashCode()
                EditProofStep(
                    viewModel = koinViewModel(key = key.toString()) { parametersOf(null) },
                    onCancel = { viewModel.cancelAction() },
                    onComplete = { viewModel.addStep(it) }
                )
            }
        }

    }
}

