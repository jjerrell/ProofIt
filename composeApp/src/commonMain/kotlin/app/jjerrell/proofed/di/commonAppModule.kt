package app.jjerrell.proofed.di

import app.jjerrell.proofed.ui.ProofSequenceMenuViewModel
import app.jjerrell.proofed.ui.ProofSequencePageViewModel
import app.jjerrell.proofed.ui.step.ProofStepViewModel
import dev.jjerrell.proofed.feature.data.glue.di.glueDataModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonAppModule = module {
    includes(glueDataModule())
    viewModel { ProofSequenceMenuViewModel(useCases = get()) }
    viewModel { ProofSequencePageViewModel(useCases = get()) }
    viewModel { parameters ->
        ProofStepViewModel(sequence = parameters.get(), proofStep = parameters.get())
    }
}
