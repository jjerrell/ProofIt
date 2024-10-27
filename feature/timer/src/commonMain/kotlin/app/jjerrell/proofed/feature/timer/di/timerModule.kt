package app.jjerrell.proofed.feature.timer.di

import app.jjerrell.proofed.feature.timer.TimerManager
import kotlin.time.Duration
import kotlin.uuid.Uuid
import org.koin.dsl.module

val timerModule = module {
    factory { (id: Uuid, duration: Duration, isAlarm: Boolean, description: String?) ->
        TimerManager(id, duration, isAlarm, description, get(), get())
    }
}
