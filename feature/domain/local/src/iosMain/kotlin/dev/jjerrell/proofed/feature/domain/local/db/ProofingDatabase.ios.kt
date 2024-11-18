package dev.jjerrell.proofed.feature.domain.local.db

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

val iosDbModule = module {
    single { ProofingDatabaseFactory() }
}

actual class ProofingDatabaseFactory {
    actual fun newBuilder(): RoomDatabase.Builder<ProofingDatabase> {
        val dbFilePath = "${documentDirectory()}/$DATABASE_NAME"
        return Room.databaseBuilder<ProofingDatabase>(
            name = dbFilePath,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
