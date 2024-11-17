package dev.jjerrell.proofed.feature.data.api.service

import dev.jjerrell.proofed.feature.data.api.model.ProofStep
import kotlin.uuid.Uuid

interface IProofStepService {
    suspend fun getAllSequenceSteps(
        sequenceId: Uuid
    ): List<ProofStep>

    suspend fun addSequenceStep(
        sequenceId: Uuid,
        step: ProofStep
    )

    suspend fun removeSequenceStep(
        sequenceId: Uuid,
        stepId: Uuid
    )

    suspend fun updateSequenceStep(
        sequenceId: Uuid,
        step: ProofStep
    )
}