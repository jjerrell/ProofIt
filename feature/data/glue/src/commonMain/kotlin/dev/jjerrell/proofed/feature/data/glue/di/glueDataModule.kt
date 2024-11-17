package dev.jjerrell.proofed.feature.data.glue.di

import dev.jjerrell.proofed.feature.data.glue.ProofSequenceUseCases
import dev.jjerrell.proofed.feature.data.local.di.localDataModule
import org.koin.dsl.module

fun glueDataModule() = module {
    includes(localDataModule())
    factory { ProofSequenceUseCases(localRepository = get()) }
}
