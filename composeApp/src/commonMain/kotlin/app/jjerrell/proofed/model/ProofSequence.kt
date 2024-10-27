package app.jjerrell.proofed.model

import kotlin.uuid.Uuid

/**
 * { "name": "Sourdough Loaf", "steps":
 * [ { "name": "Autolyse", "duration": 3600, "frequency": "ONCE" }, { "name": "Stretch and Fold", "duration": 1800, "frequency": "UNTIL_CANCEL" }, { "name": "Bulk Ferment", "duration": 43200, "frequency": "ONCE" }, { "name": "Bake - Covered", "duration": 2400, "frequency": "ONCE" }, { "name": "Bake - Uncovered", "duration": 1200, "frequency": "ONCE" } ]
 * }
 */
data class ProofSequence(
    val id: Uuid,
    val name: String,
    val steps: List<ProofStep>,
    val imageResourceUrl: String? = null
) {
    companion object {
        val loafSequence =
            ProofSequence(id = Uuid.random(), name = "Sourdough Loaf", steps = ProofStep.loafSteps)
        val feedingSequence =
            ProofSequence(
                id = Uuid.random(),
                name = "Sourdough - Feeding",
                steps = ProofStep.feedingSteps
            )
        val testSequence =
            ProofSequence(id = Uuid.random(), name = "Test Sequence", steps = ProofStep.testSteps)

        val allSequences = listOf(loafSequence, feedingSequence, testSequence)
    }
}
