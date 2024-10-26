package app.jjerrell.proofed.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jjerrell.proofed.feature.timer.TimerManager
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun TimerLayout(modifier: Modifier = Modifier) {
    val timer = koinInject<TimerManager> { parametersOf(Uuid.random(), 20.seconds, false) }

    val remainingTime = timer.remainingTimeFlow.collectAsState()
    val timerState = timer.timerStateFlow.collectAsState()

    val alarmTimer =
        koinInject<TimerManager> {
            parametersOf(
                Uuid.random(),
                20.seconds,
                true // is alarm
            )
        }
    val remainingAlarmTime = alarmTimer.remainingTimeFlow.collectAsState()
    val timerAlarmState = alarmTimer.timerStateFlow.collectAsState()

    Column(modifier = modifier) {
        Text(text = "Timer Layout")
        Text(text = "Timer: ${remainingTime.value}")
        Text(text = "Timer State: ${timerState.value}")
        Button(onClick = { timer.start() }) { Text(text = "Start") }
        Button(onClick = { timer.pause() }) { Text(text = "Pause") }
        Button(onClick = { timer.stop() }) { Text(text = "Stop") }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text(text = "Alarm Timer Layout")
        Text(text = "Timer: ${remainingAlarmTime.value}")
        Text(text = "Timer State: ${timerAlarmState.value}")
        Button(onClick = { alarmTimer.start() }) { Text(text = "Start") }
        Button(onClick = { alarmTimer.pause() }) { Text(text = "Pause") }
        Button(onClick = { alarmTimer.stop() }) { Text(text = "Stop") }
    }
}
