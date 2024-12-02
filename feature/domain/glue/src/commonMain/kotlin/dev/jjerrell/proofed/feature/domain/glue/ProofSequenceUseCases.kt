package dev.jjerrell.proofed.feature.domain.glue

import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import dev.jjerrell.proofed.feature.domain.local.LocalRepository
import kotlin.uuid.Uuid

class ProofSequenceUseCases(val localRepository: LocalRepository) {
    suspend fun getAllSequences() = localRepository.getAllSequences()

    suspend fun getSequence(sequenceId: Uuid): ProofSequence? {
        return localRepository.getSequence(sequenceId)
    }

    suspend fun addSequence(sequence: ProofSequence): Boolean =
        localRepository.addSequence(sequence)

    suspend fun updateSequence(sequence: ProofSequence): Boolean =
        localRepository.updateSequence(sequence)

    suspend fun getStep(stepId: Uuid) = localRepository.getStep(stepId)

    suspend fun addStep(sequenceId: Uuid, step: ProofStep): Boolean {
        return localRepository.addSequenceStep(sequenceId, step)
    }

    suspend fun updateStep(sequenceId: Uuid, step: ProofStep): Boolean {
        return localRepository.updateSequenceStep(sequenceId, step)
    }
}
