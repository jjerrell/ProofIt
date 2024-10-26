package app.jjerrell.proofed.ui.navigation

import org.jetbrains.compose.resources.StringResource
import proofed.composeapp.generated.resources.Res
import proofed.composeapp.generated.resources.app_name
import proofed.composeapp.generated.resources.timer
import proofed.composeapp.generated.resources.view_sequence

/** enum values that represent the screens in the app */
enum class ProofScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Sequence(title = Res.string.view_sequence),
    TimerDemo(title = Res.string.timer);

    companion object {
        fun fromRoute(route: String?): ProofScreen =
            when (route?.substringBefore("/")) {
                Sequence.name -> Sequence
                TimerDemo.name -> TimerDemo
                else -> Start
            }
    }
}
