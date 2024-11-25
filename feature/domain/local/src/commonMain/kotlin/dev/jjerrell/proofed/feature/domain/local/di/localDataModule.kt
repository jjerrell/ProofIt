package dev.jjerrell.proofed.feature.domain.local.di

import dev.jjerrell.proofed.feature.domain.api.service.IProofSequenceService
import dev.jjerrell.proofed.feature.domain.api.service.IProofStepService
import dev.jjerrell.proofed.feature.domain.local.LocalRepository
import dev.jjerrell.proofed.feature.domain.local.db.ProofingDatabase
import dev.jjerrell.proofed.feature.domain.local.db.ProofingDatabaseFactory
import org.koin.dsl.binds
import org.koin.dsl.module

val localDataModule = module {
    single<ProofingDatabase> { get<ProofingDatabaseFactory>().newBuilder().build() }
    single<LocalRepository> { LocalRepository(dbService = get()) } binds
        arrayOf(IProofSequenceService::class, IProofStepService::class)
}
