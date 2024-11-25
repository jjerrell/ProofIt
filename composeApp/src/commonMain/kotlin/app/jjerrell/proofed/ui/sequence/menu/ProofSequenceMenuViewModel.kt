package app.jjerrell.proofed.ui.sequence.menu

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
                    state = State.Loading(state?.sequences)
                    useCases.getAllSequences()
                }
                .onSuccess { state = State.Success(sequences = it) }
                .onFailure { state = State.Error(error = it) }
        }
    }

    sealed interface State {
        val sequences: List<ProofSequence>?
            get() = null

        data class Loading(override val sequences: List<ProofSequence>? = null) : State

        data class Success(override val sequences: List<ProofSequence>) : State

        data class Error(val error: Throwable) : State
    }
}
