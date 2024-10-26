package app.jjerrell.proofed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.timer.TimerLayout
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        TimerLayout(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
