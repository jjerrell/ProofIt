package dev.jjerrell.proofed.feature.domain.local

import dev.jjerrell.proofed.feature.domain.api.model.Frequency
import dev.jjerrell.proofed.feature.domain.api.model.ProofSequence
import dev.jjerrell.proofed.feature.domain.api.model.ProofStep
import dev.jjerrell.proofed.feature.domain.api.service.IProofSequenceService
import dev.jjerrell.proofed.feature.domain.api.service.IProofStepService
import dev.jjerrell.proofed.feature.domain.local.db.ProofingDatabase
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceEntity
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceWithSteps
import dev.jjerrell.proofed.feature.domain.local.model.ProofStepEntity
import kotlin.uuid.Uuid

class LocalRepository internal constructor(
    private val dbService: ProofingDatabase
) : IProofSequenceService, IProofStepService {
    // region IProofSequenceService
    override suspend fun getAllSequences(): List<ProofSequence> {
        return dbService.getProofingDao()
            .getProofSequencesWithSteps()
//            .ifEmpty { ProofSequenceWithSteps.allSequencesWithSteps }
            .map { it.convertSequenceWithSteps() }
    }

    override suspend fun getSequence(sequenceId: Uuid): ProofSequence? {
        return dbService.getProofingDao()
            .getProofSequenceWithSteps(sequenceId)
            ?.convertSequenceWithSteps()
    }

    override suspend fun addSequence(sequence: ProofSequence) {
        dbService.getProofingDao()
            .insertProofSequenceWithSteps(
                proofSequence = ProofSequenceEntity(
                    id = sequence.id,
                    name = sequence.name,
                    imageResourceUrl = sequence.imageResourceUrl
                ),
                steps = sequence.steps.map {
                    ProofStepEntity(
                        id = it.id,
                        sequenceId = sequence.id,
                        name = it.name,
                        duration = it.duration,
                        frequency = it.frequency.name,
                        isAlarmOnly = it.isAlarmOnly
                    )
                }
            )
    }

    override suspend fun removeSequence(sequenceId: Uuid) {
        dbService.getProofingDao().deleteProofSequence(sequenceId)
    }

    override suspend fun updateSequence(sequence: ProofSequence) {
        dbService.getProofingDao().updateProofSequenceWithSteps(
            proofSequence = ProofSequenceEntity(
                id = sequence.id,
                name = sequence.name,
                imageResourceUrl = sequence.imageResourceUrl
            ),
            steps = sequence.steps.map {
                ProofStepEntity(
                    id = it.id,
                    sequenceId = sequence.id,
                    name = it.name,
                    duration = it.duration,
                    frequency = it.frequency.name,
                    isAlarmOnly = it.isAlarmOnly
                )
            }
        )
    }
    // endregion
    // region IProofStepService
    override suspend fun getAllSequenceSteps(sequenceId: Uuid): List<ProofStep> {
        return dbService.getProofingDao().getProofSteps(sequenceId).map {
            it.convertStepEntity()
        }
    }

    override suspend fun addSequenceStep(sequenceId: Uuid, step: ProofStep) {
        dbService.getProofingDao().insertProofStep(
            ProofStepEntity(
                id = step.id,
                sequenceId = sequenceId,
                name = step.name,
                duration = step.duration,
                frequency = step.frequency.name,
                isAlarmOnly = step.isAlarmOnly
            )
        )
    }

    override suspend fun removeSequenceStep(sequenceId: Uuid, stepId: Uuid) {
        dbService.getProofingDao().deleteProofStep(sequenceId, stepId)
    }

    override suspend fun updateSequenceStep(sequenceId: Uuid, step: ProofStep) {
        dbService.getProofingDao().updateProofStep(
            ProofStepEntity(
                id = step.id,
                sequenceId = sequenceId,
                name = step.name,
                duration = step.duration,
                frequency = step.frequency.name,
                isAlarmOnly = step.isAlarmOnly
            )
        )
    }
    // endregion
}

// region Converters
private fun ProofSequenceWithSteps.convertSequenceWithSteps(): ProofSequence {
    return ProofSequence(
        id = proofSequence.id,
        name = proofSequence.name,
        steps = proofSteps.map { it.convertStepEntity() },
        imageResourceUrl = proofSequence.imageResourceUrl
    )
}

private fun ProofStepEntity.convertStepEntity(): ProofStep {
    return ProofStep(
        id = id,
        name = name,
        duration = duration,
        frequency = Frequency.valueOf(frequency),
        isAlarmOnly = isAlarmOnly
    )
}
//endregion
