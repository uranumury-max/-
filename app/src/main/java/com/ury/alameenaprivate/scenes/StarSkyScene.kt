package com.ury.alameenaprivate.scenes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

/** Scene 1: twinkling stars, touch draws glowing constellation lines, shooting stars. */
@Composable
fun StarSkyScene(modifier: Modifier = Modifier) {
    val n = 130
    val sx = remember { FloatArray(n) { Random.nextFloat() } }
    val sy = remember { FloatArray(n) { Random.nextFloat() } }
    val ph = remember { FloatArray(n) { Random.nextFloat() * 6.28f } }
    val sz = remember { FloatArray(n) { 1f + Random.nextFloat() * 2.5f } }
    var touch by remember { mutableStateOf<Offset?>(null) }
    val shoot = remember { floatArrayOf(0f, 0f, 1f) }
    var wait by remember { mutableFloatStateOf(3f) }
    val time = rememberFrameTime { dt ->
        if (shoot[2] < 1f) shoot[2] += dt * 1.2f
        else { wait -= dt; if (wait <= 0f) { shoot[0] = Random.nextFloat() * 0.6f; shoot[1] = Random.nextFloat() * 0.4f; shoot[2] = 0f; wait = 3f + Random.nextFloat() * 5f } }
    }
    Canvas(
        modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF050716), Color(0xFF1A1245), Color(0xFF2A1B5E))))
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val p = awaitPointerEvent().changes.firstOrNull()
                        touch = if (p != null && p.pressed) p.position else null
                    }
                }
            }
    ) {
        val t = time.value
        val w = size.width; val h = size.height
        val tp = touch
        for (i in 0 until n) {
            val x = sx[i] * w; val y = sy[i] * h
            var a = 0.45f + 0.45f * sin(t * 1.5f + ph[i])
            var near = false
            if (tp != null) {
                val d = hypot(x - tp.x, y - tp.y)
                if (d < 260f) {
                    near = true; a = 1f
                    drawLine(Color(0xFFE6C068).copy(alpha = (1f - d / 260f) * 0.7f), tp, Offset(x, y), 2f)
                }
            }
            val c = if (near) Color(0xFFFFE9A8) else Color.White
            drawCircle(c.copy(alpha = a * 0.18f), sz[i] * 4f, Offset(x, y))
            drawCircle(c.copy(alpha = a), sz[i], Offset(x, y))
        }
        if (shoot[2] < 1f) {
            val p = shoot[2]
            val hx = (shoot[0] + p * 0.4f) * w; val hy = (shoot[1] + p * 0.3f) * h
            drawLine(Color.White.copy(alpha = 0.8f), Offset(hx, hy), Offset(hx - 160f, hy - 120f), 3f)
            drawCircle(Color.White, 3.5f, Offset(hx, hy))
        }
    }
}
