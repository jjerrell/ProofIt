package dev.jjerrell.proofed.feature.domain.api.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

data class ProofStep(
    val id: Uuid,
    val name: String,
    val duration: Duration,
    val frequency: Frequency,
    val isAlarmOnly: Boolean = false
) {
    companion object {
        val loafSteps =
            listOf(
                ProofStep(
                    id = Uuid.random(),
                    name = "Autolyse",
                    duration = 1.hours,
                    frequency = Frequency.ONCE
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Stretch and Fold",
                    duration = 30.minutes,
                    frequency = Frequency.UNTIL_CANCEL
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Bulk Ferment",
                    duration = 12.hours,
                    frequency = Frequency.ONCE
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Bake - Covered",
                    duration = 40.minutes,
                    frequency = Frequency.ONCE
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Bake - Uncovered",
                    duration = 20.minutes,
                    frequency = Frequency.ONCE
                )
            )

        val feedingSteps =
            listOf(
                ProofStep(
                    id = Uuid.random(),
                    name = "Feed Starter",
                    duration = 12.hours,
                    frequency = Frequency.UNTIL_CANCEL,
                    isAlarmOnly = true
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Feed Starter - Dormant",
                    duration = 7.days,
                    frequency = Frequency.UNTIL_CANCEL,
                    isAlarmOnly = true
                )
            )

        val testSteps =
            listOf(
                ProofStep(
                    id = Uuid.random(),
                    name = "Test Alarm",
                    duration = 10.seconds,
                    frequency = Frequency.UNTIL_CANCEL,
                    isAlarmOnly = true
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Test Long Alarm",
                    duration = 1.minutes,
                    frequency = Frequency.UNTIL_CANCEL,
                    isAlarmOnly = true
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Test Timer",
                    duration = 20.seconds,
                    frequency = Frequency.UNTIL_CANCEL,
                    isAlarmOnly = false
                ),
                ProofStep(
                    id = Uuid.random(),
                    name = "Test Long Timer",
                    duration = 1.minutes,
                    frequency = Frequency.UNTIL_CANCEL,
                    isAlarmOnly = false
                ),
            )
    }
}

enum class Frequency {
    ONCE,
    UNTIL_CANCEL
}
