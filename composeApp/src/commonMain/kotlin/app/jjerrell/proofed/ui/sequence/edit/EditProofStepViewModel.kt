package app.jjerrell.proofed.ui.sequence.edit

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.jjerrell.proofed.feature.domain.api.model.Frequency
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

class EditProofStepViewModel(
    val selectedStep: ProofStep?,
) : ViewModel() {
    var state: State by mutableStateOf(State.Loading(null))
        private set

    private var isNewStep: Boolean = true

    val stepName by derivedStateOf {
        state.step?.name ?: ""
    }

    val stepDuration by derivedStateOf {
        state.step?.duration ?: ""
    }

    val stepFrequency by derivedStateOf {
        (state.step?.frequency ?: Frequency.ONCE).toString()
    }

    val isAlarmOnly by derivedStateOf {
        state.step?.isAlarmOnly ?: false
    }

    // region Setup
    fun initializeState() {
        state = State.Loading(state.step)
        if (selectedStep == null) {
            createNewStep()
        } else {
            loadStep()
        }
    }

    private fun loadStep() {
        state = if (selectedStep == null) {
            State.Error(Throwable("Step not found"))
        } else {
            State.Success(step =
                MutableStep(
                    id = selectedStep.id,
                    name = selectedStep.name,
                    duration = selectedStep.duration.inWholeSeconds.toString(),
                    frequency = selectedStep.frequency.toString(),
                    isAlarmOnly = selectedStep.isAlarmOnly
                )
            ).also {
                isNewStep = false
            }
        }
    }

    private fun createNewStep() {
        state = State.Success(step = MutableStep())
        isNewStep = true
    }
    // endregion

    // region Updating
    fun validateAndSaveStep(): ProofStep? {
        return if (isValidName() && isValidDuration() && isValidFrequency()) {
            state.step?.let {
                ProofStep(
                    id = it.id,
                    name = it.name,
                    duration = it.duration.toLong().seconds,
                    frequency = Frequency.valueOf(it.frequency),
                )
            }
        } else {
            null
        }
    }

    fun validateAndSetName(name: String): Boolean {
        val currentStep = state.step ?: return false
        state = (state as? State.Success)?.copy(step = currentStep.copy(name = name)) ?: state
        return isValidName()
    }

    fun validateAndSetDuration(duration: String): Boolean {
        val currentStep = state.step ?: return false
        state = (state as? State.Success)?.copy(step = currentStep.copy(duration = duration)) ?: state
        return isValidDuration()
    }

    fun validateAndSetFrequency(frequency: String): Boolean {
        val currentStep = state.step ?: return false
        state = (state as? State.Success)?.copy(step = currentStep.copy(frequency = frequency)) ?: state
        return isValidFrequency()
    }

    fun isValidName(): Boolean {
        return stepName.isNotBlank()
    }

    fun isValidDuration(): Boolean {
        return when (val stepDurationLong = stepDuration.toLongOrNull()) {
            null -> false
            else -> stepDurationLong >= 0L
        }
    }

    fun isValidFrequency(): Boolean {
        return try {
            Frequency.valueOf(stepFrequency)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    // endregion

    sealed interface State {
        val step: MutableStep?
            get() = null

        data class Loading(override val step: MutableStep? = null) : State

        data class Success(override val step: MutableStep) : State

        data class Error(val error: Throwable) : State
    }

    data class MutableStep(
        val id: Uuid = Uuid.random(),
        val name: String = "",
        val duration: String = "",
        val frequency: String = "",
        val isAlarmOnly: Boolean = false
    )
}