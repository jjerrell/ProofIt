package dev.jjerrell.proofed.feature.domain.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.jjerrell.proofed.feature.domain.local.db.converter.DurationConverter
import dev.jjerrell.proofed.feature.domain.local.db.converter.UuidConverter
import dev.jjerrell.proofed.feature.domain.local.db.dao.ProofingDao
import dev.jjerrell.proofed.feature.domain.local.model.ProofSequenceEntity
import dev.jjerrell.proofed.feature.domain.local.model.ProofStepEntity

internal const val DATABASE_NAME = "proofing_room.db"

@Database(
    entities = [ProofSequenceEntity::class, ProofStepEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(UuidConverter::class, DurationConverter::class)
@ConstructedBy(ProofingDatabaseConstructor::class)
abstract class ProofingDatabase : RoomDatabase() {
    abstract fun getProofingDao(): ProofingDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect object ProofingDatabaseConstructor : RoomDatabaseConstructor<ProofingDatabase> {
    override fun initialize(): ProofingDatabase
}

expect class ProofingDatabaseFactory {
    fun newBuilder(): RoomDatabase.Builder<ProofingDatabase>
}