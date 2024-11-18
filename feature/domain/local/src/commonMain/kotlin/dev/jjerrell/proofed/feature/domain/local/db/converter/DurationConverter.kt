package dev.jjerrell.proofed.feature.domain.local.db.converter

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DurationConverter {
    @TypeConverter
    fun fromDuration(duration: Duration): Long {
        return duration.inWholeMilliseconds
    }

    @TypeConverter
    fun toDuration(millis: Long): Duration {
        return millis.milliseconds
    }
}
