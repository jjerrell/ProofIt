package dev.jjerrell.proofed.feature.data.api.service

import dev.jjerrell.proofed.feature.data.api.model.ProofSequence
import kotlin.uuid.Uuid

interface IProofSequenceService {
    suspend fun getAllSequences(): List<ProofSequence>

    suspend fun getSequence(sequenceId: Uuid): ProofSequence?

    suspend fun addSequence(sequence: ProofSequence)

    suspend fun removeSequence(sequenceId: Uuid)

    suspend fun updateSequence(sequence: ProofSequence)
}