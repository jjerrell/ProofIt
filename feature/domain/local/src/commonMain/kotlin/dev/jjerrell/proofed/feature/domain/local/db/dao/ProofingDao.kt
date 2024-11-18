package dev.jjerrell.proofed.feature.domain.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceEntity
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceWithSteps
import dev.jjerrell.proofed.feature.domain.local.model.ProofStepEntity
import kotlin.uuid.Uuid

@Dao
interface ProofingDao {
    @Insert
    suspend fun insertProofSequence(proofSequence: ProofSequenceEntity)

    @Insert
    suspend fun insertProofStep(proofStep: ProofStepEntity)

    @Transaction
    suspend fun insertProofSequenceWithSteps(proofSequence: ProofSequenceEntity, steps: List<ProofStepEntity>) {
        insertProofSequence(proofSequence)
        steps.forEach { step ->
            insertProofStep(step)
        }
    }

    @Query("SELECT * FROM proof_sequence WHERE id = :sequenceId")
    suspend fun getProofSequence(sequenceId: Uuid): ProofSequenceEntity?

    @Transaction
    @Query("SELECT * FROM proof_sequence WHERE id = :sequenceId")
    suspend fun getProofSequenceWithSteps(sequenceId: Uuid): ProofSequenceWithSteps?
}