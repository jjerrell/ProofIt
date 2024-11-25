package app.jjerrell.proofed.ui.sequence

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.glue.ProofSequenceUseCases
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch

class ProofSequencePageViewModel(val useCases: ProofSequenceUseCases) : ViewModel() {
    var state: State? by mutableStateOf(null)
        private set

    fun getSequence(sequenceId: Uuid) {
        viewModelScope.launch {
            runCatching {
                    state = State.Loading
                    useCases.getSequence(sequenceId)
                }
                .onSuccess {
                    state =
                        if (it == null) {
                            State.Error(Exception("Sequence not found"))
                        } else {
                            State.Success(sequence = it)
                        }
                }
                .onFailure { state = State.Error(error = it) }
        }
    }

    sealed interface State {
        data object Loading : State

        data class Success(val sequence: ProofSequence) : State

        data class Error(val error: Throwable) : State
    }
}
