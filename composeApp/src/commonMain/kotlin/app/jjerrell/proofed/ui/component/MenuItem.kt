package app.jjerrell.proofed.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(modifier = modifier, onClick = onClick, enabled = enabled) {
        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            icon()
            content()
        }
    }
}
