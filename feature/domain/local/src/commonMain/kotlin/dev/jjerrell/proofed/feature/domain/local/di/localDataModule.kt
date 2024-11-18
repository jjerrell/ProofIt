package dev.jjerrell.proofed.feature.domain.local.di

import dev.jjerrell.proofed.feature.domain.api.service.IProofSequenceService
import dev.jjerrell.proofed.feature.domain.api.service.IProofStepService
import dev.jjerrell.proofed.feature.domain.local.LocalRepository
import dev.jjerrell.proofed.feature.domain.local.db.ProofingDatabase
import dev.jjerrell.proofed.feature.domain.local.db.ProofingDatabaseFactory
import dev.jjerrell.proofed.feature.domain.local.service.InMemoryProofSequenceService
import dev.jjerrell.proofed.feature.domain.local.service.InMemoryProofStepService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val localDataModule = module {
    single<ProofingDatabase> {
        get<ProofingDatabaseFactory>()
            .newBuilder()
            .build()
    }
    single {
        InMemoryProofSequenceService()
    }
    single {
        InMemoryProofStepService()
    }
    single<LocalRepository> {
        LocalRepository(localProofService = get(), localProofStepService = get())
    }  binds arrayOf(IProofSequenceService::class, IProofStepService::class)
}
