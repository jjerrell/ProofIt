package dev.jjerrell.proofed.feature.domain.local.db.converter

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

class UuidConverter {
    @TypeConverter
    fun fromUuid(uuid: Uuid): String {
        return uuid.toHexString()
    }

    @TypeConverter
    fun toUuid(hexString: String): Uuid {
        return Uuid.parseHex(hexString)
    }
}
