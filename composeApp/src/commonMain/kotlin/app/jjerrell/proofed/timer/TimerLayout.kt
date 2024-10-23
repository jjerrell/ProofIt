package app.jjerrell.proofed.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.jjerrell.proofed.feature.timer.TimerManager
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.Uuid

@Composable
@Preview
fun TimerLayout(modifier: Modifier = Modifier) {
    val timer = remember {
        TimerManager(
            id = Uuid.random(),
            duration = 10.minutes
        )
    }
    val remainingTime = timer.remainingTimeFlow.collectAsState()
    val timerState = timer.timerStateFlow.collectAsState()

    Column(modifier = modifier) {
        Text(text = "Timer Layout")
        Text(text = "Timer: ${remainingTime.value}")
        Text(text = "Timer State: ${timerState.value}")
        Button(onClick = { timer.start() }) {
            Text(text = "Start")
        }
        Button(onClick = { timer.pause() }) {
            Text(text = "Pause")
        }
        Button(onClick = { timer.stop() }) {
            Text(text = "Stop")
        }
    }
}