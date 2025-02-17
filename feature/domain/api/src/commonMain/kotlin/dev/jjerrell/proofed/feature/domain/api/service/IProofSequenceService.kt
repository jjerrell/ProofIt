package dev.jjerrell.proofed.feature.domain.api.service

import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import kotlin.uuid.Uuid

interface IProofSequenceService {
    suspend fun getAllSequences(): List<ProofSequence>

    suspend fun getSequence(sequenceId: Uuid): ProofSequence?

    suspend fun addSequence(sequence: ProofSequence): Boolean

    suspend fun removeSequence(sequenceId: Uuid)

    suspend fun updateSequence(sequence: ProofSequence): Boolean
}
