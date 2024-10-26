package app.jjerrell.proofed.feature.timer.di

import app.jjerrell.proofed.feature.timer.TimerManager
import org.koin.dsl.module
import kotlin.time.Duration
import kotlin.uuid.Uuid

val timerModule = module {
    factory { (id: Uuid, duration: Duration, isAlarm: Boolean) ->
        TimerManager(id, duration, isAlarm, get(), get())
    }
}
