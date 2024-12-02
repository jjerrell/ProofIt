package app.jjerrell.proofed.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.jjerrell.proofed.ui.sequence.ProofSequencePage
import app.jjerrell.proofed.ui.sequence.edit.EditProofSequencePage
import app.jjerrell.proofed.ui.sequence.menu.ProofSequenceMenuPage
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.Uuid

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ProofNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startPage: ProofScreen = ProofScreen.Start
) {
    NavHost(
        navController = navController,
        startDestination = startPage.name,
        modifier = modifier
    ) {
        composable(route = ProofScreen.Start.route) {
            ProofSequenceMenuPage(
                viewModel = koinViewModel(),
                onCreateNewSequence = {
                    navController.navigate(ProofScreen.CreateSequence.name)
                },
                onSequenceClick = {
                    navController.navigate(
                        ProofScreen.Sequence.name + "/${it.id.toHexString()}"
                    )
                }
            )
        }
        composable(route = ProofScreen.Sequence.route) { backStackEntry ->
            val sequenceId = backStackEntry.arguments?.getString("sequenceId")
                ?.let { Uuid.parseHex(it) }
                ?: Uuid.random()
            ProofSequencePage(
                sequenceId = sequenceId,
                viewModel = koinViewModel()
            )
        }
        composable(route = ProofScreen.CreateSequence.route) {
            EditProofSequencePage(
                viewModel = koinNavViewModel(),
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}