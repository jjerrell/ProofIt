package dev.jjerrell.proofed.feature.data.local

import dev.jjerrell.proofed.feature.data.api.model.Frequency
import dev.jjerrell.proofed.feature.data.api.model.ProofSequence
import dev.jjerrell.proofed.feature.data.api.model.ProofStep
import dev.jjerrell.proofed.feature.data.api.service.IProofSequenceService
import dev.jjerrell.proofed.feature.data.api.service.IProofStepService
import dev.jjerrell.proofed.feature.data.local.model.ProofSequenceEntity
import dev.jjerrell.proofed.feature.data.local.model.ProofStepEntity
import dev.jjerrell.proofed.feature.data.local.service.InMemoryProofSequenceService
import dev.jjerrell.proofed.feature.data.local.service.InMemoryProofStepService
import kotlin.uuid.Uuid

class LocalRepository internal constructor(
    private val localProofService: InMemoryProofSequenceService,
    private val localProofStepService: InMemoryProofStepService
) : IProofSequenceService, IProofStepService {
    //region IProofSequenceService
    override fun getAllSequences(): List<ProofSequence> {
        return localProofService.getAllSequences().map {
            ProofSequence(
                id = it.id,
                name = it.name,
                steps = getAllSequenceSteps(it.id),
                imageResourceUrl = it.imageResourceUrl
            )
        }
    }

    override fun getSequence(sequenceId: Uuid): ProofSequence? {
        return localProofService.getSequence(sequenceId)?.let {
            ProofSequence(
                id = it.id,
                name = it.name,
                steps = getAllSequenceSteps(it.id),
                imageResourceUrl = it.imageResourceUrl
            )
        }
    }

    override fun addSequence(sequence: ProofSequence) {
        localProofService.addSequence(
            sequence = sequence.let {
                ProofSequenceEntity(
                    id = it.id,
                    name = it.name,
                    imageResourceUrl = it.imageResourceUrl
                )
            }
        )
    }

    override fun removeSequence(sequenceId: Uuid) {
        localProofService.removeSequence(sequenceId)
    }

    override fun updateSequence(sequence: ProofSequence) {
        localProofService.updateSequence(
            sequence = sequence.let {
                ProofSequenceEntity(
                    id = it.id,
                    name = it.name,
                    imageResourceUrl = it.imageResourceUrl
                )
            }
        )
    }
    //endregion
    //region IProofStepService
    override fun getAllSequenceSteps(sequenceId: Uuid): List<ProofStep> {
        return localProofStepService
            .getAllSequenceSteps(sequenceId)
            .map {
                ProofStep(
                    id = it.id,
                    name = it.name,
                    duration = it.duration,
                    frequency = Frequency.valueOf(it.frequency),
                    isAlarmOnly = it.isAlarmOnly
                )
            }
    }

    override fun addSequenceStep(sequenceId: Uuid, step: ProofStep) {
        localProofStepService.addSequenceStep(
            sequenceId = sequenceId,
            step = step.let {
                ProofStepEntity(
                    id = it.id,
                    sequenceId = sequenceId,
                    name = it.name,
                    duration = it.duration,
                    frequency = it.frequency.name,
                    isAlarmOnly = it.isAlarmOnly
                )
            }
        )
    }

    override fun removeSequenceStep(sequenceId: Uuid, stepId: Uuid) {
        localProofStepService.removeSequenceStep(sequenceId, stepId)
    }

    override fun updateSequenceStep(sequenceId: Uuid, step: ProofStep) {
        localProofStepService.updateSequenceStep(
            sequenceId = sequenceId,
            step = step.let {
                ProofStepEntity(
                    id = it.id,
                    sequenceId = sequenceId,
                    name = it.name,
                    duration = it.duration,
                    frequency = it.frequency.name,
                    isAlarmOnly = it.isAlarmOnly
                )
            }
        )
    }
    //endregion
}