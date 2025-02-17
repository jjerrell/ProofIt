package dev.jjerrell.proofed.feature.domain.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceEntity
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceWithSteps
import dev.jjerrell.proofed.feature.domain.local.model.ProofStepEntity
import kotlin.uuid.Uuid

@Dao
interface ProofingDao {
    @Insert suspend fun insertProofSequence(proofSequence: ProofSequenceEntity): Long

    @Update suspend fun updateProofSequence(proofSequence: ProofSequenceEntity): Int

    @Query("SELECT * FROM proof_step WHERE sequenceId = :sequenceId")
    suspend fun getProofSteps(sequenceId: Uuid): List<ProofStepEntity>

    @Query("SELECT * FROM proof_step WHERE id = :id")
    suspend fun getProofStep(id: Uuid): ProofStepEntity?

    @Insert suspend fun insertProofStep(proofStep: ProofStepEntity): Long

    @Update suspend fun updateProofStep(proofStep: ProofStepEntity): Int

    @Transaction
    suspend fun insertProofSequenceWithSteps(
        proofSequence: ProofSequenceEntity,
        steps: List<ProofStepEntity>
    ): Long {
        return insertProofSequence(proofSequence).also {
            steps.forEach { step -> insertProofStep(step) }
        }
    }

    @Transaction
    suspend fun updateProofSequenceWithSteps(
        proofSequence: ProofSequenceEntity,
        steps: List<ProofStepEntity>
    ): Int {
        val sequenceResult = updateProofSequence(proofSequence) * 10
        val stepsResult = steps.sumOf { updateProofStep(it) }
        return sequenceResult + stepsResult
    }

    @Query("SELECT * FROM proof_sequence WHERE id = :sequenceId")
    suspend fun getProofSequence(sequenceId: Uuid): ProofSequenceEntity?

    @Transaction
    @Query("SELECT * FROM proof_sequence WHERE id = :sequenceId")
    suspend fun getProofSequenceWithSteps(sequenceId: Uuid): ProofSequenceWithSteps?

    @Transaction
    @Query("SELECT * FROM proof_sequence")
    suspend fun getProofSequencesWithSteps(): List<ProofSequenceWithSteps>

    @Query("DELETE FROM proof_sequence WHERE id = :sequenceId")
    suspend fun deleteProofSequence(sequenceId: Uuid)

    @Query("DELETE FROM proof_step WHERE sequenceId = :sequenceId AND id = :stepId")
    suspend fun deleteProofStep(sequenceId: Uuid, stepId: Uuid)
}
