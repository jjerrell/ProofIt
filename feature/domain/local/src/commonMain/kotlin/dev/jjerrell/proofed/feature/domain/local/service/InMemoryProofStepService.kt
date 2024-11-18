package dev.jjerrell.proofed.feature.domain.local.service

import dev.jjerrell.proofed.feature.domain.local.model.ProofStepEntity
import kotlin.uuid.Uuid

internal class InMemoryProofStepService {
    fun getAllSequenceSteps(sequenceId: Uuid): List<ProofStepEntity> {
        return ProofStepEntity.allSteps.filter { it.sequenceId == sequenceId }
    }

    fun addSequenceStep(sequenceId: Uuid, step: ProofStepEntity) {}

    fun removeSequenceStep(sequenceId: Uuid, stepId: Uuid) {}

    fun updateSequenceStep(sequenceId: Uuid, step: ProofStepEntity) {}
}
