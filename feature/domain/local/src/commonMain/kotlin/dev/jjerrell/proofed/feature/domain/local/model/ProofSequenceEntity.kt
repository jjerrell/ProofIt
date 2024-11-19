package dev.jjerrell.proofed.feature.domain.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlin.uuid.Uuid

@Entity(tableName = "proof_sequence")
data class ProofSequenceEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val imageResourceUrl: String?
) {
    companion object {
        val loafSequence =
            ProofSequenceEntity(
                id = Uuid.random(),
                name = "Sourdough Loaf",
                imageResourceUrl = null
            )
        val feedingSequence =
            ProofSequenceEntity(
                id = Uuid.random(),
                name = "Sourdough - Feeding",
                imageResourceUrl = null
            )
        val testSequence =
            ProofSequenceEntity(id = Uuid.random(), name = "Test Sequence", imageResourceUrl = null)

        val allSequences = listOf(loafSequence, feedingSequence, testSequence)
    }
}

data class ProofSequenceWithSteps(
    @Embedded val proofSequence: ProofSequenceEntity,
    @Relation(parentColumn = "id", entityColumn = "sequenceId")
    val proofSteps: List<ProofStepEntity>
) {
    companion object {
        val allSequencesWithSteps =
            listOf(
                ProofSequenceWithSteps(
                    proofSequence = ProofSequenceEntity.loafSequence,
                    proofSteps = ProofStepEntity.loafSteps
                ),
                ProofSequenceWithSteps(
                    proofSequence = ProofSequenceEntity.feedingSequence,
                    proofSteps = ProofStepEntity.feedingSteps
                ),
                ProofSequenceWithSteps(
                    proofSequence = ProofSequenceEntity.testSequence,
                    proofSteps = ProofStepEntity.testSteps
                )
            )
    }
}
