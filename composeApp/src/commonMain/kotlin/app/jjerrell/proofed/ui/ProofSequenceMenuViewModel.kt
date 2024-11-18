package app.jjerrell.proofed.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.glue.ProofSequenceUseCases
import kotlinx.coroutines.launch

class ProofSequenceMenuViewModel(val useCases: ProofSequenceUseCases) : ViewModel() {
    var state: State? by mutableStateOf(null)
        private set

    fun getAllSequences() {
        viewModelScope.launch {
            runCatching {
                    state = State.Loading
                    useCases.getAllSequences()
                }
                .onSuccess {
                    state =
                        if (it.isEmpty()) {
                            State.Error(Exception("No sequences found"))
                        } else {
                            State.Success(sequences = it)
                        }
                }
                .onFailure { state = State.Error(error = it) }
        }
    }

    sealed interface State {
        data object Loading : State

        data class Success(val sequences: List<ProofSequence>) : State

        data class Error(val error: Throwable) : State
    }
}
