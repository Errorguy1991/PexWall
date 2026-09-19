package com.pexwall.app.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.currentWallpaper != null) {
            val wallpaper = uiState.currentWallpaper!!

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(wallpaper.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Current Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Crop fits it to mobile properly
            )

            // Top Gradient overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                        )
                    )
            )

            // Top Left Source and Photographer Info
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp)
            ) {
                val sourceName = when {
                    wallpaper.photographerUrl.contains("unsplash.com", ignoreCase = true) -> "Source: Unsplash"
                    wallpaper.photographerUrl.contains("pexels.com", ignoreCase = true) -> "Source: Pexels"
                    else -> "Source: Bing"
                }

                Text(
                    text = sourceName,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                val infoText = if (sourceName == "Source: Bing") {
                    wallpaper.photographer // Bing has the full copyright string here
                } else {
                    "Photo by " + wallpaper.photographer
                }

                Text(
                    text = infoText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // Error Snackbar
        AnimatedVisibility(
            visible = uiState.error != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = uiState.error ?: "",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
