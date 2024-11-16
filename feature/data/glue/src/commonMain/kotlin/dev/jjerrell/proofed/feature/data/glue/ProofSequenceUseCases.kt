package dev.jjerrell.proofed.feature.data.glue

import dev.jjerrell.proofed.feature.data.local.LocalRepository
import kotlin.uuid.Uuid

class ProofSequenceUseCases(
    val localRepository: LocalRepository
) {
    fun getAllSequences() = localRepository.getAllSequences()
    fun getSequence(sequenceId: Uuid) = localRepository.getSequence(sequenceId)
}