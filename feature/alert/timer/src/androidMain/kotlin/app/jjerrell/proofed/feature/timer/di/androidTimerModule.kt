package app.jjerrell.proofed.feature.timer.di

import app.jjerrell.proofed.feature.timer.service.TimerAlarmService
import app.jjerrell.proofed.feature.timer.service.TimerService
import org.koin.dsl.module

val androidTimerModule = module {
    includes(timerModule)
    single { TimerService() }
    single { TimerAlarmService() }
}
