package app.jjerrell.proofed.ui.sequence.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import app.jjerrell.proofed.AppBarState
import app.jjerrell.proofed.FabState
import app.jjerrell.proofed.ui.component.MenuItem
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import kotlinx.coroutines.cancelChildren

@Composable
fun CreateProofSequencePage(
    modifier: Modifier = Modifier,
    setAppBarState: (AppBarState) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: EditProofSequenceViewModel
) {
    DisposableEffect(Unit) {
        setAppBarState(
            AppBarState(
                actionItems = listOf(
                    AppBarState.ActionItem(
                        isEnabled = true,
                        description = "Save Sequence",
                        icon = Icons.Default.Check,
                        onClick = {
                            viewModel.saveSequence(
                                onSuccessfulSave = { didAddOrUpdate ->
                                    // TODO: Do something if didAddOrUpdate is false
                                    onNavigateUp()
                                }
                            )
                        }
                    )
                )
            )
        )
        onDispose {
            // Reset FAB state
            setAppBarState(AppBarState())
        }
    }
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        TextField(
            value = viewModel.state.sequenceName,
            onValueChange = viewModel::updateSequenceName,
            label = { Text(text = "Sequence Name") },
            placeholder = { Text(text = "Sequence Name") },
            trailingIcon = {
                // Clear
                IconButton(onClick = { viewModel.updateSequenceName("") }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        // region Existing items
        viewModel.state.steps.forEach { thisStep ->
            val isBeingEdited by derivedStateOf { viewModel.currentStep?.id == thisStep.id }
            val activeStep by derivedStateOf {
                viewModel.currentStep.takeIf { isBeingEdited } ?: thisStep
            }
            EditableProofStepMenuItem(
                step = activeStep,
                isEditing = isBeingEdited,
                onToggleEdit = { isDirty ->
                    if (isBeingEdited) {
                        // TODO: Check if dirty
                        viewModel.cancelEditingStep()
                    } else {
                        viewModel.beginEditingStep(thisStep.id)
                    }
                },
                onSave = viewModel::updateCurrentStep
            )
        }
        // endregion
        // region New Item
        AnimatedVisibility(visible = viewModel.currentStep != null && viewModel.currentStepIsNew) {
            ProofStepEditSection(
                step = viewModel.currentStep ?: ProofStep.EMPTY,
                onCancel = { isDirty ->
                    // TODO: Check if dirty
                    viewModel.cancelEditingStep()
                },
                onFinish = viewModel::updateCurrentStep
            )
        }
        // endregion
        TextButton(onClick = { viewModel.beginEditingStep(null) }) { Text(text = "Add New Step") }
    }
}

@Composable
fun EditableProofStepMenuItem(
    step: ProofStep,
    isEditing: Boolean,
    onToggleEdit: (isDirty: Boolean) -> Unit,
    onSave:
        (EditProofSequenceViewModel.UpdatedStepData) -> EditProofSequenceViewModel.UpdatedStepError?
) {
    MenuItem(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onToggleEdit(false) },
        enabled = !isEditing,
        icon = {},
        content = { Text(step.name) }
    )
    AnimatedVisibility(visible = isEditing) {
        ProofStepEditSection(
            step = step,
            saveButtonText = "Save Step",
            onCancel = onToggleEdit,
            onFinish = onSave
        )
    }
}

@Composable
fun ProofStepEditSection(
    step: ProofStep,
    modifier: Modifier = Modifier,
    saveButtonText: String = "Add Step",
    onCancel: (isDirty: Boolean) -> Unit,
    onFinish:
        (EditProofSequenceViewModel.UpdatedStepData) -> EditProofSequenceViewModel.UpdatedStepError?
) {
    var name by remember { mutableStateOf(step.name) }
    var duration by remember { mutableStateOf("${step.duration.inWholeSeconds}") }
    var frequency by remember { mutableStateOf(step.frequency.name) }
    val isDirty by derivedStateOf {
        name != step.name ||
            duration != "${step.duration.inWholeSeconds}" ||
            frequency != step.frequency.name
    }
    val updatedStepData by derivedStateOf {
        EditProofSequenceViewModel.UpdatedStepData(
            name = name,
            duration = duration,
            frequency = frequency,
            isDirty = isDirty
        )
    }
    var updatedStepError: EditProofSequenceViewModel.UpdatedStepError? by remember {
        mutableStateOf(null)
    }

    Column(modifier = modifier) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Step Name") },
            placeholder = { Text(text = "Step Name") },
            trailingIcon = {
                // Clear
                IconButton(onClick = { name = "" }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                }
            },
            isError = updatedStepError?.nameIsError == true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text(text = "Duration") },
            placeholder = { Text(text = "1800 Seconds") },
            trailingIcon = {
                // Clear
                IconButton(onClick = { duration = "" }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                }
            },
            isError = updatedStepError?.durationIsError == true,
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = frequency,
            onValueChange = { frequency = it },
            label = { Text(text = "Frequency") },
            placeholder = { Text(text = "Once") },
            trailingIcon = {
                // Clear
                IconButton(onClick = { frequency = "" }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                }
            },
            isError = updatedStepError?.frequencyIsError == true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions =
                KeyboardActions(onDone = { updatedStepError = onFinish(updatedStepData) }),
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            TextButton(onClick = { onCancel(isDirty) }) { Text(text = "Cancel") }
            TextButton(onClick = { updatedStepError = onFinish(updatedStepData) }) {
                Text(text = saveButtonText)
            }
        }
    }
}
