package app.jjerrell.proofed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.jjerrell.proofed.ui.component.ProofAppBar
import app.jjerrell.proofed.ui.navigation.ProofScreen
import app.jjerrell.proofed.ui.sequence.ProofSequencePage
import app.jjerrell.proofed.ui.sequence.create.CreateProofSequencePage
import app.jjerrell.proofed.ui.sequence.menu.ProofSequenceMenu
import kotlin.uuid.Uuid
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ProofScreen.fromRoute(backStackEntry?.destination?.route)
    // Mechanism for controlling the Floating Action Button
    val fabState = remember { mutableStateOf(FabState()) }
    // Mechanism for controlling the App Bar
    val appBarState = remember { mutableStateOf(AppBarState()) }

    KoinContext {
        MaterialTheme {
            Scaffold(
                topBar = {
                    ProofAppBar(
                        currentScreen = currentScreen,
                        appBarState = appBarState.value,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButton = {
                    if (fabState.value.isVisible) {
                        FloatingActionButton(
                            onClick = fabState.value.onClick,
                            content = { Icon(fabState.value.icon, "") },
                        )
                    }
                },
                floatingActionButtonPosition = fabState.value.floatingActionButtonPosition,
                isFloatingActionButtonDocked = fabState.value.isFloatingActionButtonDocked,
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
                            viewModel = koinViewModel(),
                            setFabState = { fabState.value = it },
                            onCreateNewSequence = {
                                navController.navigate(ProofScreen.CreateSequence.route)
                            },
                            onSequenceClick = {
                                navController.navigate(
                                    ProofScreen.Sequence.name + "/${it.id.toHexString()}"
                                )
                            }
                        )
                    }
                    composable(route = ProofScreen.Sequence.route) {
                        val sequenceId = it.arguments?.getString("sequenceId")
                        ProofSequencePage(
                            sequenceId = sequenceId?.let { it1 -> Uuid.parseHex(it1) } ?: Uuid.NIL,
                            viewModel = koinViewModel()
                        )
                    }
                    composable(route = ProofScreen.CreateSequence.route) {
                        CreateProofSequencePage(
                            setAppBarState = { appBarState.value = it },
                            onNavigateUp = navController::popBackStack,
                            viewModel = koinViewModel { parametersOf(null) }
                        )
                    }
                }
            }
        }
    }
}

data class FabState(
    val isVisible: Boolean = false,
    val icon: ImageVector = Icons.Default.Add,
    val floatingActionButtonPosition: FabPosition = FabPosition.End,
    val isFloatingActionButtonDocked: Boolean = false,
    val onClick: () -> Unit = {}
)

data class AppBarState(
    val title: String? = null,
    val actionItems: List<ActionItem> = emptyList()
) {
    data class ActionItem(
        val isEnabled: Boolean,
        val description: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    )
}