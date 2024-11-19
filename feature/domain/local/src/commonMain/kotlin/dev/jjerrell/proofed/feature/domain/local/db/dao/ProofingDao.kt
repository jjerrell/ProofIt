package dev.jjerrell.proofed.feature.domain.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
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
    @Insert suspend fun insertProofSequence(proofSequence: ProofSequenceEntity)
    @Update suspend fun updateProofSequence(proofSequence: ProofSequenceEntity)

    @Query("SELECT * FROM proof_step WHERE sequenceId = :sequenceId")
    suspend fun getProofSteps(sequenceId: Uuid): List<ProofStepEntity>
    @Insert suspend fun insertProofStep(proofStep: ProofStepEntity)
    @Update suspend fun updateProofStep(proofStep: ProofStepEntity)

    @Transaction
    suspend fun insertProofSequenceWithSteps(
        proofSequence: ProofSequenceEntity,
        steps: List<ProofStepEntity>
    ) {
        insertProofSequence(proofSequence)
        steps.forEach { step -> insertProofStep(step) }
    }

    @Transaction
    suspend fun updateProofSequenceWithSteps(
        proofSequence: ProofSequenceEntity,
        steps: List<ProofStepEntity>
    ) {
        updateProofSequence(proofSequence)
        steps.forEach { step -> updateProofStep(step) }
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
