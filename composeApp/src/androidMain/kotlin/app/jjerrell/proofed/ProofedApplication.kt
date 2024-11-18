package app.jjerrell.proofed

import android.app.Application
import app.jjerrell.proofed.di.commonAppModule
import app.jjerrell.proofed.feature.notification.di.notificationModule
import app.jjerrell.proofed.feature.timer.di.androidTimerModule
import dev.jjerrell.proofed.feature.domain.glue.di.androidDataGlueModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ProofedApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ProofedApplication)
            androidLogger()
            modules(
                listOf(
                    commonAppModule,
                    notificationModule,
                    androidTimerModule,
                    androidDataGlueModule
                )
            )
        }
    }
}
