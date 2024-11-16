package dev.jjerrell.proofed.feature.data.api.service

import dev.jjerrell.proofed.feature.data.api.model.ProofStep
import kotlin.uuid.Uuid

interface IProofStepService {
    fun getAllSequenceSteps(
        sequenceId: Uuid
    ): List<ProofStep>

    fun addSequenceStep(
        sequenceId: Uuid,
        step: ProofStep
    )

    fun removeSequenceStep(
        sequenceId: Uuid,
        stepId: Uuid
    )

    fun updateSequenceStep(
        sequenceId: Uuid,
        step: ProofStep
    )
}