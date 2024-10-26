package app.jjerrell.proofed.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.ui.navigation.ProofScreen
import org.jetbrains.compose.resources.stringResource
import proofed.composeapp.generated.resources.Res
import proofed.composeapp.generated.resources.back_button

/** Composable that displays the topBar and displays back button if back navigation is possible. */
@Composable
fun ProofAppBar(
    currentScreen: ProofScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        }
    )
}
