package dev.jjerrell.proofed.feature.domain.glue.di

import dev.jjerrell.proofed.feature.domain.local.db.androidDbModule
import org.koin.dsl.module

val androidDataGlueModule = module {
    includes(dataGlueModule)
    includes(androidDbModule)
}