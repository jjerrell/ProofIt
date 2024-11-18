package dev.jjerrell.proofed.feature.domain.local.service

import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceEntity
import kotlin.uuid.Uuid

internal class InMemoryProofSequenceService {
    fun getAllSequences(): List<ProofSequenceEntity> {
        return ProofSequenceEntity.allSequences
    }

    fun getSequence(sequenceId: Uuid): ProofSequenceEntity? {
        return ProofSequenceEntity.allSequences.find { it.id == sequenceId }
    }

    fun addSequence(sequence: ProofSequenceEntity) {}

    fun removeSequence(sequenceId: Uuid) {}

    fun updateSequence(sequence: ProofSequenceEntity) {}
}
