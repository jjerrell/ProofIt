package dev.jjerrell.proofed.feature.data.glue

import dev.jjerrell.proofed.feature.data.local.LocalRepository
import kotlin.uuid.Uuid

class ProofSequenceUseCases(val localRepository: LocalRepository) {
    suspend fun getAllSequences() = localRepository.getAllSequences()

    suspend fun getSequence(sequenceId: Uuid) = localRepository.getSequence(sequenceId)
}
