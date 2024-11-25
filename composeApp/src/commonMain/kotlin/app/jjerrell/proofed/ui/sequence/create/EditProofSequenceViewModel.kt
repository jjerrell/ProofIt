package app.jjerrell.proofed.ui.sequence.create

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jjerrell.proofed.feature.domain.api.model.Frequency
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import dev.jjerrell.proofed.feature.domain.glue.ProofSequenceUseCases
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch

/**
 * A [ViewModel] for editing a [ProofSequence].
 *
 * @param useCases The [ProofSequenceUseCases] to use.
 * @param sequenceId The ID of an existing [ProofSequence] to edit. If null, a new [ProofSequence]
 *   will be created.
 */
class EditProofSequenceViewModel(
    private val useCases: ProofSequenceUseCases,
    private val sequenceId: Uuid?
) : ViewModel() {
    var state by mutableStateOf(State())
        private set

    var currentStep by mutableStateOf<ProofStep?>(null)
        private set

    val currentStepIsNew by derivedStateOf { state.steps.none { it.id == currentStep?.id } }

    val isSequenceDirty: Boolean by derivedStateOf {
        state.sequenceName != state.sequence?.name || state.steps != state.sequence?.steps
    }

    // region Lifecycle
    fun initialize() {
        if (sequenceId != null) {
            loadSequence(sequenceId)
        } else {
            beginNewSequence()
        }
    }

    private fun loadSequence(id: Uuid) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            runCatching { useCases.getSequence(sequenceId = id) }
                .onSuccess { state = State(isLoading = false, sequence = it) }
                .onFailure { state = state.copy(isLoading = false, error = it) }
        }
    }

    private fun beginNewSequence() {
        state = State(sequence = ProofSequence(id = Uuid.random(), name = "", steps = emptyList()))
    }
    // endregion

    // region Sequence Operations
    fun updateSequenceName(name: String) {
        val validNameOrNull = checkValidName(name)
        if (validNameOrNull != null) {
            state = state.copy(sequenceName = validNameOrNull)
        }
    }

    fun saveSequence(onSuccessfulSave: (Boolean) -> Unit, ) {
        viewModelScope.launch {
            runCatching {
                useCases.addSequence(state.sequence!!)
            }
                .onSuccess {
                    onSuccessfulSave(it)
                }
                .onFailure {
                    state = state.copy(error = it)
                }
        }
    }
    // endregion

    // region Sequence Step Operations
    fun beginEditingStep(id: Uuid?) {
        currentStep =
            if (id == null) {
                // Create new step
                ProofStep(
                    id = Uuid.random(),
                    name = "",
                    duration = 0.seconds,
                    frequency = Frequency.ONCE,
                )
            } else {
                state.steps.firstOrNull { it.id == id }
            }
    }

    fun updateCurrentStep(data: UpdatedStepData): UpdatedStepError? {
        return if (data.isDirty) {
            val validNameOrNull = checkValidName(data.name)
            val validDurationOrNull = checkValidStepDuration(data.duration)
            val validFrequencyOrNull = checkValidStepFrequency(data.frequency)
            if (
                validNameOrNull == null ||
                    validDurationOrNull == null ||
                    validFrequencyOrNull == null
            ) {
                UpdatedStepError(
                    nameIsError = validNameOrNull == null,
                    durationIsError = validDurationOrNull == null,
                    frequencyIsError = validFrequencyOrNull == null
                )
            } else {
                val existingIndex =
                    state.steps.indexOfFirst { it.id == currentStep?.id }.takeIf { it > -1 }
                val updatedStep =
                    (currentStep ?: ProofStep.EMPTY).copy(
                        name = validNameOrNull,
                        duration = validDurationOrNull,
                        frequency = validFrequencyOrNull
                    )
                if (currentStepIsNew) {
                    addNewStep(updatedStep)
                } else if (existingIndex != null) {
                    updateExistingStep(existingIndex, updatedStep)
                }
                cancelEditingStep()
                return null
            }
        } else {
            cancelEditingStep()
            return null
        }
    }

    fun cancelEditingStep() {
        currentStep = null
    }

    fun saveSteps() {
        // TODO: Check if steps are valid
        state =
            state.copy(
                sequence = state.sequence?.copy(steps = state.steps.filter { it.name.isNotBlank() })
            )
    }

    private fun updateExistingStep(index: Int, step: ProofStep) {
        val steps = state.steps.toMutableList()
        steps[index] = step
        state = state.copy(steps = steps)
    }

    private fun addNewStep(step: ProofStep) {
        val steps = state.steps.toMutableList()
        steps.add(step)
        state = state.copy(steps = steps)
    }

    private fun checkValidName(name: String): String? = name
        .trim()
        .takeIf { it.isNotBlank() }

    private fun checkValidStepDuration(duration: String): Duration? =
        duration.toDoubleOrNull()?.seconds?.takeIf { it.isPositive() }

    private fun checkValidStepFrequency(frequency: String): Frequency? =
        try {
            Frequency.valueOf(frequency.uppercase())
        } catch (t: Throwable) {
            null
        }
    // endregion

    data class State(
        val isLoading: Boolean = false,
        val sequence: ProofSequence? = null,
        val sequenceName: String = sequence?.name ?: "",
        val steps: List<ProofStep> = sequence?.steps?.takeUnless { it.isEmpty() } ?: emptyList(),
        val error: Throwable? = null
    ) {
        /** False if an existing sequence is being edited. */
        val isNewSequence: Boolean
            get() = sequence == null

        val isEmptyState: Boolean
            get() = this == State()

        val hasError: Boolean
            get() = error != null
    }

    data class UpdatedStepData(
        val name: String,
        val duration: String,
        val frequency: String,
        val isDirty: Boolean
    )

    data class UpdatedStepError(
        val nameIsError: Boolean,
        val durationIsError: Boolean,
        val frequencyIsError: Boolean
    )
}
