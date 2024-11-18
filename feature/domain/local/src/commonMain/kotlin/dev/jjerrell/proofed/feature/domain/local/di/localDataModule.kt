package dev.jjerrell.proofed.feature.domain.local.di

import dev.jjerrell.proofed.feature.domain.api.service.IProofSequenceService
import dev.jjerrell.proofed.feature.domain.api.service.IProofStepService
import dev.jjerrell.proofed.feature.domain.local.LocalRepository
import dev.jjerrell.proofed.feature.domain.local.service.InMemoryProofSequenceService
import dev.jjerrell.proofed.feature.domain.local.service.InMemoryProofStepService
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
