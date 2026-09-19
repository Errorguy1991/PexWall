package com.pexwall.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun MeshGradientBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D11))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0F3A8D).copy(alpha = 0.6f), Color.Transparent),
                    center = Offset(0f, 0f),
                    radius = width * 1.2f
                ),
                radius = width * 1.2f,
                center = Offset(0f, 0f)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8B2117).copy(alpha = 0.5f), Color.Transparent),
                    center = Offset(width, 0f),
                    radius = width * 1.1f
                ),
                radius = width * 1.1f,
                center = Offset(width, 0f)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0F7A46).copy(alpha = 0.7f), Color.Transparent),
                    center = Offset(width / 2f, height),
                    radius = width * 1.3f
                ),
                radius = width * 1.3f,
                center = Offset(width / 2f, height)
            )
        }
    }
}
