package dev.jjerrell.proofed.feature.data.api.service

import dev.jjerrell.proofed.feature.data.api.model.ProofSequence
import kotlin.uuid.Uuid

interface IProofSequenceService {
    fun getAllSequences(): List<ProofSequence>

    fun getSequence(sequenceId: Uuid): ProofSequence?

    fun addSequence(sequence: ProofSequence)

    fun removeSequence(sequenceId: Uuid)

    fun updateSequence(sequence: ProofSequence)

}