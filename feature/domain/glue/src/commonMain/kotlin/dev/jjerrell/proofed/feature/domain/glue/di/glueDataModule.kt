package dev.jjerrell.proofed.feature.domain.glue.di

import dev.jjerrell.proofed.feature.domain.glue.ProofSequenceUseCases
import dev.jjerrell.proofed.feature.domain.local.di.localDataModule
import org.koin.dsl.module

fun glueDataModule() = module {
    includes(localDataModule())
    factory { ProofSequenceUseCases(localRepository = get()) }
}
