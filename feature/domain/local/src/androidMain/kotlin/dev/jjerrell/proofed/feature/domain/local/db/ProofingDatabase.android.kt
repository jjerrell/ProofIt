package dev.jjerrell.proofed.feature.domain.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module

val androidDbModule = module { single { ProofingDatabaseFactory(context = get()) } }

actual class ProofingDatabaseFactory(private val context: Context) {
    actual fun newBuilder(): RoomDatabase.Builder<ProofingDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(DATABASE_NAME)
        return Room.databaseBuilder<ProofingDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
