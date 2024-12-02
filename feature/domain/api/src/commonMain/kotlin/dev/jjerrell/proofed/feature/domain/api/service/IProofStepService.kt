package dev.jjerrell.proofed.feature.domain.api.service

import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import kotlin.uuid.Uuid

interface IProofStepService {
    suspend fun getStep(stepId: Uuid): ProofStep?

    suspend fun getAllSequenceSteps(sequenceId: Uuid): List<ProofStep>

    suspend fun addSequenceStep(sequenceId: Uuid, step: ProofStep): Boolean

    suspend fun removeSequenceStep(sequenceId: Uuid, stepId: Uuid)

    suspend fun updateSequenceStep(sequenceId: Uuid, step: ProofStep): Boolean
}
