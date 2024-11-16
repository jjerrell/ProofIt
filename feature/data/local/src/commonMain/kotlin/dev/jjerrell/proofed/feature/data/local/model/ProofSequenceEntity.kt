package dev.jjerrell.proofed.feature.data.local.model

import kotlin.uuid.Uuid

/**
 * { "name": "Sourdough Loaf", "steps":
 * [ { "name": "Autolyse", "duration": 3600, "frequency": "ONCE" }, { "name": "Stretch and Fold", "duration": 1800, "frequency": "UNTIL_CANCEL" }, { "name": "Bulk Ferment", "duration": 43200, "frequency": "ONCE" }, { "name": "Bake - Covered", "duration": 2400, "frequency": "ONCE" }, { "name": "Bake - Uncovered", "duration": 1200, "frequency": "ONCE" } ]
 * }
 */
data class ProofSequenceEntity(
    val id: Uuid,
    val name: String,
    val imageResourceUrl: String? = null
) {
    companion object {
        val loafSequence =
            ProofSequenceEntity(id = Uuid.random(), name = "Sourdough Loaf")
        val feedingSequence =
            ProofSequenceEntity(
                id = Uuid.random(),
                name = "Sourdough - Feeding",
            )
        val testSequence =
            ProofSequenceEntity(id = Uuid.random(), name = "Test Sequence")

        val allSequences = listOf(loafSequence, feedingSequence, testSequence)
    }
}