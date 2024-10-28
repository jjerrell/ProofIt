package app.jjerrell.proofed.di

import app.jjerrell.proofed.ui.step.ProofStepViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonAppModule = module {
    viewModel { parameters ->
        ProofStepViewModel(sequence = parameters.get(), proofStep = parameters.get())
    }
}
