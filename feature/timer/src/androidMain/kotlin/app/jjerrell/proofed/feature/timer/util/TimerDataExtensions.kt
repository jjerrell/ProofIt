package app.jjerrell.proofed.feature.timer.util

import android.os.Bundle
import app.jjerrell.proofed.feature.timer.TimerData

fun TimerData.toBundle(): Bundle {
    val bundle = Bundle()
    bundle.putInt("id", id.hashCode())
    bundle.putString("remaining", remaining.toString())
    return bundle
}
