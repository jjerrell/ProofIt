package app.jjerrell.proofed.feature.timer.di

import app.jjerrell.proofed.feature.timer.service.TimerAlarmService
import app.jjerrell.proofed.feature.timer.service.TimerService
import org.koin.dsl.module

val androidTimerModule =
    timerModule +
        module {
            single { TimerService() }
            single { TimerAlarmService() }
        }
