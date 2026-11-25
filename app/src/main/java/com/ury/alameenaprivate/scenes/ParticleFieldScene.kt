package com.ury.alameenaprivate.scenes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

/** Scene 2: 320 particles flowing in a smooth field like smoke; touch attracts them. */
@Composable
fun ParticleFieldScene(modifier: Modifier = Modifier) {
    val n = 320
    val x = remember { FloatArray(n) { Random.nextFloat() } }
    val y = remember { FloatArray(n) { Random.nextFloat() } }
    val vx = remember { FloatArray(n) }
    val vy = remember { FloatArray(n) }
    var touch by remember { mutableStateOf<Offset?>(null) }
    var box by remember { mutableStateOf(Offset(1f, 1f)) }
    var clock by remember { mutableFloatStateOf(0f) }
    val time = rememberFrameTime { dt ->
        clock += dt
        val tp = touch
        for (i in 0 until n) {
            val a = (sin(x[i] * 6f + clock * 0.7f) * cos(y[i] * 6f - clock * 0.5f)) * 6.28f
            vx[i] += cos(a) * 0.25f * dt; vy[i] += sin(a) * 0.25f * dt
            if (tp != null) {
                val dx = tp.x / box.x - x[i]; val dy = tp.y / box.y - y[i]
                val d = hypot(dx, dy) + 0.02f
                vx[i] += dx / d * 0.6f * dt; vy[i] += dy / d * 0.6f * dt
            }
            vx[i] *= 0.98f; vy[i] *= 0.98f
            x[i] = (x[i] + vx[i] * dt * 3f + 1f) % 1f; y[i] = (y[i] + vy[i] * dt * 3f + 1f) % 1f
        }
    }
    Canvas(
        modifier.fillMaxSize().background(Color(0xFF070A1C)).pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val p = awaitPointerEvent().changes.firstOrNull()
                    touch = if (p != null && p.pressed) p.position else null
                }
            }
        }
    ) {
        time.value
        box = Offset(size.width, size.height)
        for (i in 0 until n) {
            val px = x[i] * size.width; val py = y[i] * size.height
            val sp = hypot(vx[i], vy[i]).coerceIn(0f, 1f)
            val c = Color(0.55f + 0.45f * sp, 0.5f + 0.2f * sp, 1f - 0.5f * sp)
            drawLine(c.copy(alpha = 0.35f), Offset(px - vx[i] * 400f, py - vy[i] * 400f), Offset(px, py), 2f)
            drawCircle(c.copy(alpha = 0.9f), 2.2f, Offset(px, py))
        }
    }
}
