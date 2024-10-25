package app.jjerrell.proofed.feature.timer.di

import app.jjerrell.proofed.feature.timer.TimerService
import org.koin.dsl.module

val androidTimerModule = timerModule + module {
    single { TimerService(get()) }
}