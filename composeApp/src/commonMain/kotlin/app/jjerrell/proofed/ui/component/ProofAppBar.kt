package app.jjerrell.proofed.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.AppBarState
import app.jjerrell.proofed.ui.navigation.ProofScreen
import org.jetbrains.compose.resources.stringResource
import proofit.composeapp.generated.resources.Res
import proofit.composeapp.generated.resources.back_button

/** Composable that displays the topBar and displays back button if back navigation is possible. */
@Composable
fun ProofAppBar(
    currentScreen: ProofScreen,
    appBarState: AppBarState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(appBarState.title ?: stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            AnimatedVisibility(visible = canNavigateBack, enter = fadeIn(), exit = fadeOut()) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        },
        actions = {
            appBarState.actionItems.forEach { actionItem ->
                IconButton(
                    onClick = actionItem.onClick,
                    enabled = actionItem.isEnabled
                ) {
                    Icon(imageVector = actionItem.icon, contentDescription = actionItem.description)
                }
            }
        }
    )
}

