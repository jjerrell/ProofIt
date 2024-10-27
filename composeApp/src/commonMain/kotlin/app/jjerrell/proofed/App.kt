package app.jjerrell.proofed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.jjerrell.proofed.model.ProofSequence
import app.jjerrell.proofed.ui.ProofSequenceMenu
import app.jjerrell.proofed.ui.ProofSequencePage
import app.jjerrell.proofed.ui.component.ProofAppBar
import app.jjerrell.proofed.ui.navigation.ProofScreen
import app.jjerrell.proofed.ui.timer.demo.TimerLayout
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ProofScreen.fromRoute(backStackEntry?.destination?.route)

    KoinContext {
        MaterialTheme {
            Scaffold(
                topBar = {
                    ProofAppBar(
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = ProofScreen.Start.name,
                    modifier =
                        Modifier.fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    composable(route = ProofScreen.Start.route) {
                        ProofSequenceMenu(
                            onSequenceClick = {
                                navController.navigate(
                                    ProofScreen.Sequence.name + "/${it.id.toHexString()}"
                                )
                            }
                        )
                    }
                    composable(route = ProofScreen.Sequence.route) {
                        val sequenceId = it.arguments?.getString("sequenceId")
                        val sequence =
                            ProofSequence.allSequences.first { proofSequence ->
                                proofSequence.id.toHexString() == sequenceId
                            }
                        ProofSequencePage(proofSequence = sequence)
                    }
                    composable(route = ProofScreen.TimerDemo.route) { TimerLayout() }
                }
            }
        }
    }
}
