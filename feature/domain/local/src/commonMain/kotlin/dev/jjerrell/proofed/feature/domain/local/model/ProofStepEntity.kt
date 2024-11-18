package dev.jjerrell.proofed.feature.domain.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@Entity(
    tableName = "proof_step",
    indices = [Index("sequenceId")],
    foreignKeys = [
        ForeignKey(
            entity = ProofSequenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["sequenceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProofStepEntity(
    @PrimaryKey val id: Uuid,
    val sequenceId: Uuid,
    val name: String,
    val duration: Duration,
    val frequency: String,
    val isAlarmOnly: Boolean
) {
    companion object {
        val loafSteps =
            listOf(
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.loafSequence.id,
                    name = "Autolyse",
                    duration = 1.hours,
                    frequency = "ONCE",
                    isAlarmOnly = false
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.loafSequence.id,
                    name = "Stretch and Fold",
                    duration = 30.minutes,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = false
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.loafSequence.id,
                    name = "Bulk Ferment",
                    duration = 12.hours,
                    frequency = "ONCE",
                    isAlarmOnly = false
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.loafSequence.id,
                    name = "Bake - Covered",
                    duration = 40.minutes,
                    frequency = "ONCE",
                    isAlarmOnly = false
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.loafSequence.id,
                    name = "Bake - Uncovered",
                    duration = 20.minutes,
                    frequency = "ONCE",
                    isAlarmOnly = false
                )
            )

        val feedingSteps =
            listOf(
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.feedingSequence.id,
                    name = "Feed Starter",
                    duration = 12.hours,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = true
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.feedingSequence.id,
                    name = "Feed Starter - Dormant",
                    duration = 7.days,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = true
                )
            )

        val testSteps =
            listOf(
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.testSequence.id,
                    name = "Test Alarm",
                    duration = 10.seconds,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = true
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.testSequence.id,
                    name = "Test Long Alarm",
                    duration = 1.minutes,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = true
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.testSequence.id,
                    name = "Test Timer",
                    duration = 20.seconds,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = false
                ),
                ProofStepEntity(
                    id = Uuid.random(),
                    sequenceId = ProofSequenceEntity.testSequence.id,
                    name = "Test Long Timer",
                    duration = 1.minutes,
                    frequency = "UNTIL_CANCEL",
                    isAlarmOnly = false
                ),
            )

        val allSteps = loafSteps + feedingSteps + testSteps
    }
}
