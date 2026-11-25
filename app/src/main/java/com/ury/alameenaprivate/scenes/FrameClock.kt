package com.ury.alameenaprivate.scenes

import androidx.compose.runtime.*

/** Frame loop: calls step(dt seconds) each frame while composed; stops when the scene leaves the screen. */
@Composable
fun rememberFrameTime(step: (Float) -> Unit): State<Float> {
    val time = remember { mutableFloatStateOf(0f) }
    val cb by rememberUpdatedState(step)
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) {
                    val dt = ((now - last) / 1e9f).coerceAtMost(0.05f)
                    cb(dt)
                    time.floatValue += dt
                }
                last = now
            }
        }
    }
    return time
}
