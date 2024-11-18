package dev.jjerrell.proofed.feature.domain.glue

import dev.jjerrell.proofed.feature.domain.local.LocalRepository
import kotlin.uuid.Uuid

class ProofSequenceUseCases(val localRepository: LocalRepository) {
    suspend fun getAllSequences() = localRepository.getAllSequences()

    suspend fun getSequence(sequenceId: Uuid) = localRepository.getSequence(sequenceId)
}
