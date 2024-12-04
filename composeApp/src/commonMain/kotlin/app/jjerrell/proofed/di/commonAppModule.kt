package app.jjerrell.proofed.di

import app.jjerrell.proofed.ui.sequence.ProofSequencePageViewModel
import app.jjerrell.proofed.ui.sequence.edit.EditProofSequencePageViewModel
import app.jjerrell.proofed.ui.sequence.edit.EditProofStepViewModel
import app.jjerrell.proofed.ui.sequence.menu.ProofSequenceMenuPageViewModel
import app.jjerrell.proofed.ui.step.ProofStepViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonAppModule = module {
    viewModel { ProofSequenceMenuPageViewModel(useCases = get()) }
    viewModel { ProofSequencePageViewModel(useCases = get()) }
    viewModel { parameters ->
        ProofStepViewModel(sequence = parameters.get(), proofStep = parameters.get())
    }
    viewModel { parameters ->
        EditProofSequencePageViewModel(useCases = get(), sequenceId = parameters.getOrNull())
    }
    viewModel { parameters -> EditProofStepViewModel(selectedStep = parameters.getOrNull()) }
}
