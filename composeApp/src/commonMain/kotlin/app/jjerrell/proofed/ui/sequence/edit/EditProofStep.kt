package app.jjerrell.proofed.ui.sequence.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.PopupProperties
import app.jjerrell.proofed.model.titleResource
import dev.jjerrell.proofed.feature.domain.api.model.Frequency
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import org.jetbrains.compose.resources.stringResource
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.cancel
import proofit.composeapp.generated.resources.clear
import proofit.composeapp.generated.resources.loading
import proofit.composeapp.generated.resources.save
import proofit.composeapp.generated.resources.select_frequency
import proofit.composeapp.generated.resources.step_duration
import proofit.composeapp.generated.resources.step_name

@Composable
fun EditProofStep(
    modifier: Modifier = Modifier,
    viewModel: EditProofStepViewModel,
    onCancel: () -> Unit,
    onComplete: (ProofStep?) -> Unit
) {
    LaunchedEffect(Unit) { viewModel.initializeState() }
    Column(modifier = modifier) {
        when (val state = viewModel.state) {
            is EditProofStepViewModel.State.Loading -> {
                Text(text = stringResource(Res.string.loading))
            }
            is EditProofStepViewModel.State.Success -> {
                TextField(
                    value = viewModel.stepName,
                    onValueChange = { viewModel.validateAndSetName(it) },
                    label = { Text(text = stringResource(Res.string.step_name)) },
                    placeholder = { Text(text = stringResource(Res.string.step_name)) },
                    trailingIcon = {
                        // Clear
                        IconButton(onClick = { viewModel.validateAndSetName("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(Res.string.clear)
                            )
                        }
                    },
                    isError = !viewModel.stepNameIsValid,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = viewModel.stepDuration,
                    onValueChange = { viewModel.validateAndSetDuration(it) },
                    label = { Text(text = stringResource(Res.string.step_duration)) },
                    placeholder = { Text(text = "1800 Seconds") },
                    trailingIcon = {
                        // Clear
                        IconButton(onClick = { viewModel.validateAndSetDuration("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(Res.string.clear)
                            )
                        }
                    },
                    isError = !viewModel.stepDurationIsValid,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                    modifier = Modifier.fillMaxWidth()
                )
                val isFrequencyExpanded = remember { mutableStateOf(false) }
                Box {
                    TextButton(onClick = { isFrequencyExpanded.value = true }) {
                        Text(
                            text = viewModel.stepFrequency
                                .ifBlank {
                                    stringResource(Res.string.select_frequency)
                                }
                        )
                    }
                    DropdownMenu(
                        expanded = isFrequencyExpanded.value,
                        onDismissRequest = { isFrequencyExpanded.value = false }
                    ) {
                        Frequency.entries.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.validateAndSetFrequency(it.name).also {
                                        isFrequencyExpanded.value = false
                                    }
                                }
                            ) {
                                Text(text = it.titleResource)
                            }
                        }
                    }
                }
                Row {
                    TextButton(onClick = { onCancel() }) {
                        Text(text = stringResource(Res.string.cancel))
                    }
                    TextButton(
                        enabled = viewModel.stepIsValid,
                        onClick = { onComplete(viewModel.validateAndSaveStep()) }
                    ) {
                        Text(text = stringResource(Res.string.save))
                    }
                }
            }
            is EditProofStepViewModel.State.Error -> {
                Text(text = "Error: ${state.error.message}")
            }
        }
    }
}
