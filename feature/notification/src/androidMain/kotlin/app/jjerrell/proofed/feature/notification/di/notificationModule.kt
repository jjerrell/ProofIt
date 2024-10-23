package app.jjerrell.proofed.feature.notification.di

import app.jjerrell.proofed.feature.notification.NotificationHelper
import org.koin.dsl.module

val notificationModule = module {
    single { NotificationHelper(get()) }
}