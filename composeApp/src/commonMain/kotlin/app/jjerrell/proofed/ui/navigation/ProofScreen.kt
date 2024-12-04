package app.jjerrell.proofed.ui.navigation

import org.jetbrains.compose.resources.StringResource
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.app_name

/** enum values that represent the screens in the app */
enum class ProofScreen(val title: StringResource = Res.string.app_name) {
    Start,
    Sequence {
        override val route: String = super.route + "/{sequenceId}"
    },
    CreateSequence;

    open val route: String
        get() = name

    companion object {
        fun fromRoute(route: String?): ProofScreen =
            when (route?.substringBefore("/")) {
                Sequence.name -> Sequence
                else -> Start
            }
    }
}
