package app.jjerrell.proofed.ui.sequence.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep

@Composable
fun EditProofStep(
    modifier: Modifier = Modifier,
    viewModel: EditProofStepViewModel,
    onCancel: () -> Unit,
    onComplete: (ProofStep?) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.initializeState()
    }
    Column(
        modifier = modifier
    ) {
        when (val state = viewModel.state) {
            is EditProofStepViewModel.State.Loading -> {
                Text(text = "Loading...")
            }
            is EditProofStepViewModel.State.Success -> {
                val stepNameIsError = remember { mutableStateOf(!viewModel.isValidName()) }
                val stepDurationIsError = remember { mutableStateOf(!viewModel.isValidDuration()) }
                val stepFrequencyIsError = remember { mutableStateOf(!viewModel.isValidFrequency()) }

                TextField(
                    value = viewModel.stepName,
                    onValueChange = {
                        stepNameIsError.value = !viewModel.validateAndSetName(it)
                    },
                    label = { Text(text = "Step Name") },
                    placeholder = { Text(text = "Step Name") },
                    trailingIcon = {
                        // Clear
                        IconButton(onClick = { stepNameIsError.value = !viewModel.validateAndSetName("") }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                        }
                    },
                    isError = stepNameIsError.value,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = viewModel.stepDuration,
                    onValueChange = {
                        stepDurationIsError.value = !viewModel.validateAndSetDuration(it)
                    },
                    label = { Text(text = "Duration") },
                    placeholder = { Text(text = "1800 Seconds") },
                    trailingIcon = {
                        // Clear
                        IconButton(onClick = {
                            stepDurationIsError.value = !viewModel.validateAndSetDuration("")
                        }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                        }
                    },
                    isError = stepDurationIsError.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = viewModel.stepFrequency,
                    onValueChange = {
                        stepFrequencyIsError.value = !viewModel.validateAndSetFrequency(it)
                    },
                    label = { Text(text = "Frequency") },
                    placeholder = { Text(text = "Once") },
                    trailingIcon = {
                        // Clear
                        IconButton(onClick = {
                            stepFrequencyIsError.value = !viewModel.validateAndSetFrequency("")
                        }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Clear")
                        }
                    },
                    isError = stepFrequencyIsError.value,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions =
                    KeyboardActions(
                        onDone = {
                            viewModel.validateAndSaveStep()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row {
                    TextButton(onClick = { onCancel() }) { Text(text = "Cancel") }
                    TextButton(
                        enabled = !stepNameIsError.value && !stepDurationIsError.value && !stepFrequencyIsError.value,
                        onClick = {
                            onComplete(viewModel.validateAndSaveStep())
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            }
            is EditProofStepViewModel.State.Error -> {
                Text(text = "Error: ${state.error.message}")
            }
        }
    }
}