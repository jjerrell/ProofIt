package app.jjerrell.proofed.ui.sequence.edit

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import dev.jjerrell.proofed.feature.domain.glue.ProofSequenceUseCases
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class EditProofSequencePageViewModel(
    private var sequenceId: Uuid?,
    private val useCases: ProofSequenceUseCases
) : ViewModel() {

    var state: State by mutableStateOf(State.Loading(null))
        private set

    var action: Action by mutableStateOf(Action.None)
        private set

    private var isNewSequence: Boolean = true

    val sequenceName by derivedStateOf { state.sequence?.name ?: "" }

    // region Setup
    fun initializeState() {
        state = State.Loading(state.sequence)
        with(sequenceId) {
            if (this == null) {
                createNewSequence()
            } else {
                loadSequence(this)
            }
        }
    }

    private fun loadSequence(id: Uuid) {
        viewModelScope.launch {
            runCatching {
                useCases.getSequence(sequenceId = id)
            }.onSuccess {
                state = if (it == null) {
                    State.Error(
                        sequence = state.sequence,
                        error = Throwable("Sequence not found")
                    )
                } else {
                    isNewSequence = false
                    State.Success(it)
                }
            }.onFailure {
                state = State.Error(
                    sequence = state.sequence,
                    error = it
                )
            }
        }
    }

    private fun createNewSequence() {
        val newSequence = ProofSequence.emptySequence
        isNewSequence = true
        sequenceId = newSequence.id
        state = State.Success(newSequence)
    }
    // endregion

    // region Updating
    fun validateAndSaveSequence() {
        if (isValidSequenceName()) {
            viewModelScope.launch {
                val updatedSequence = state.sequence?.let {
                    ProofSequence(
                        id = it.id,
                        name = it.name,
                        steps = it.steps
                    )
                } ?: return@launch
                runCatching {
                    if (isNewSequence) {
                        useCases.addSequence(updatedSequence)
                    } else {
                        useCases.updateSequence(updatedSequence)
                    }
                }.onSuccess {
                    sequenceId = updatedSequence.id
                    isNewSequence = false
                }.onFailure {
                    state = State.Error(
                        sequence = state.sequence,
                        error = it
                    )
                }
            }
        }
    }

    fun validateAndSetSequenceName(name: String): Boolean {
        val currentSequence = state.sequence ?: return false
        state = (state as? State.Success)?.copy(sequence = currentSequence.copy(name = name)) ?: state
        return isValidSequenceName()
    }

    fun isValidSequenceName(): Boolean {
        return sequenceName.isNotBlank()
    }
    // endregion

    // region Step Actions
    fun addNewStepAction() {
        action = Action.AddStep
    }

    fun editStepAction(step: ProofStep) {
        action = Action.EditStep(step)
    }

    fun addStep(step: ProofStep?) {
        if (step == null) return
        val currentSequence = state.sequence ?: return
        val existingStepIndex = currentSequence.steps
            .indexOfFirst { it.id == step.id }
            .takeUnless { it == -1 }

        state = (state as? State.Success)?.let {
            if (existingStepIndex != null) {
                val updatedSteps = currentSequence.steps.toMutableList()
                updatedSteps[existingStepIndex] = step
                it.copy(sequence = currentSequence.copy(steps = updatedSteps))
            } else {
                it.copy(
                    sequence = currentSequence
                        .copy(
                            steps = currentSequence.steps + step
                        )
                )
            }
        } ?: state
        action = Action.None
    }

    fun cancelAction() {
        action = Action.None
    }
    // endregion


    sealed interface State {
        val sequence: ProofSequence?
            get() = null

        data class Loading(override val sequence: ProofSequence?) : State
        data class Error(
            override val sequence: ProofSequence? = null,
            val error: Throwable
        ) : State
        data class Success(override val sequence: ProofSequence) : State
    }

    sealed interface Action {
        data object None : Action
        data object AddStep : Action
        data class EditStep(val step: ProofStep) : Action
    }

}
