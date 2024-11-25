package app.jjerrell.proofed.di

import app.jjerrell.proofed.ui.sequence.ProofSequencePageViewModel
import app.jjerrell.proofed.ui.sequence.create.EditProofSequenceViewModel
import app.jjerrell.proofed.ui.sequence.menu.ProofSequenceMenuViewModel
import app.jjerrell.proofed.ui.step.ProofStepViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonAppModule = module {
    viewModel { ProofSequenceMenuViewModel(useCases = get()) }
    viewModel { ProofSequencePageViewModel(useCases = get()) }
    viewModel { parameters ->
        ProofStepViewModel(sequence = parameters.get(), proofStep = parameters.get())
    }
    viewModel { parameters ->
        EditProofSequenceViewModel(useCases = get(), sequenceId = parameters.getOrNull())
    }
}
