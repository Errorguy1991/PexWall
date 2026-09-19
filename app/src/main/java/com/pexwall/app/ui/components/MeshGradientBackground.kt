package com.pexwall.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun MeshGradientBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "MeshGradient")

    // Animate positions for the 3 circles
    val xOffset1 by infiniteTransition.animateFloat(
        initialValue = -0.2f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "xOffset1"
    )
    val yOffset1 by infiniteTransition.animateFloat(
        initialValue = -0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset1"
    )

    val xOffset2 by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "xOffset2"
    )
    val yOffset2 by infiniteTransition.animateFloat(
        initialValue = -0.2f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset2"
    )

    val xOffset3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "xOffset3"
    )
    val yOffset3 by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset3"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D11))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val c1 = Offset(width * xOffset1, height * yOffset1)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0F3A8D).copy(alpha = 0.6f), Color.Transparent),
                    center = c1,
                    radius = width * 1.2f
                ),
                radius = width * 1.2f,
                center = c1
            )

            val c2 = Offset(width * xOffset2, height * yOffset2)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8B2117).copy(alpha = 0.5f), Color.Transparent),
                    center = c2,
                    radius = width * 1.1f
                ),
                radius = width * 1.1f,
                center = c2
            )

            val c3 = Offset(width * xOffset3, height * yOffset3)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0F7A46).copy(alpha = 0.7f), Color.Transparent),
                    center = c3,
                    radius = width * 1.3f
                ),
                radius = width * 1.3f,
                center = c3
            )
        }
    }
}
