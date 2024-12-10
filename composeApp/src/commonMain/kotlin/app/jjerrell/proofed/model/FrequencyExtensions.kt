package app.jjerrell.proofed.model

import androidx.compose.runtime.Composable
import dev.jjerrell.proofed.feature.domain.api.model.Frequency
import org.jetbrains.compose.resources.stringArrayResource
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.frequency_options

val Frequency.titleResource: String
    @Composable get() = stringArrayResource(Res.array.frequency_options).getOrNull(ordinal) ?: name
