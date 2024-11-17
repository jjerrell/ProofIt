package dev.jjerrell.proofed.feature.data.local.di

import dev.jjerrell.proofed.feature.data.api.service.IProofSequenceService
import dev.jjerrell.proofed.feature.data.api.service.IProofStepService
import dev.jjerrell.proofed.feature.data.local.LocalRepository
import dev.jjerrell.proofed.feature.data.local.service.InMemoryProofSequenceService
import dev.jjerrell.proofed.feature.data.local.service.InMemoryProofStepService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun localDataModule() = module {
    singleOf(::InMemoryProofSequenceService)
    singleOf(::InMemoryProofStepService)
    factory { LocalRepository(localProofService = get(), localProofStepService = get()) }

    singleOf(::LocalRepository) { bind<IProofSequenceService>() }
    singleOf(::LocalRepository) { bind<IProofStepService>() }
}
