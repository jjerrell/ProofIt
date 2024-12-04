package app.jjerrell.proofed

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.jjerrell.proofed.ui.navigation.ProofNavHost
import app.jjerrell.proofed.ui.navigation.ProofScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ProofScreen.fromRoute(backStackEntry?.destination?.route)

    KoinContext { MaterialTheme { ProofNavHost(navController = navController) } }
}

data class AppBarState(val title: String? = null, val actionItems: List<ActionItem> = emptyList()) {
    data class ActionItem(
        val isEnabled: Boolean,
        val description: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    )
}
