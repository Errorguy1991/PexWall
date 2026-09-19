package com.pexwall.app.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pexwall.app.ui.categories.CategoriesScreen
import com.pexwall.app.ui.history.HistoryScreen
import com.pexwall.app.ui.home.HomeScreen
import com.pexwall.app.ui.home.HomeViewModel
import com.pexwall.app.ui.settings.SettingsScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.HazeStyle
import kotlinx.coroutines.launch

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Categories : Screen("categories", "Categories", Icons.Default.Category)
    data object History : Screen("history", "History", Icons.Default.History)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val LocalHazeState = compositionLocalOf<HazeState> { error("No HazeState provided") }

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Categories,
    Screen.History,
    Screen.Settings
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PexWallNavGraph(homeViewModel: HomeViewModel = hiltViewModel()) {
    val hazeState = remember { HazeState() }
    val pagerState = rememberPagerState(pageCount = { bottomNavItems.size })
    val coroutineScope = rememberCoroutineScope()
    
    val currentDestinationIndex = pagerState.currentPage

    Box(modifier = Modifier.fillMaxSize()) {
        
        Box(modifier = Modifier.fillMaxSize().haze(state = hazeState)) {
            com.pexwall.app.ui.components.MeshGradientBackground()
            CompositionLocalProvider(LocalHazeState provides hazeState) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    beyondBoundsPageCount = 3
                ) { page ->
                    when (page) {
                        0 -> HomeScreen(viewModel = homeViewModel)
                        1 -> CategoriesScreen()
                        2 -> HistoryScreen()
                        3 -> SettingsScreen()
                    }
                }
            }
        }

        // Floating Navigation Island
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(32.dp))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(32.dp))
                .hazeChild(
                    state = hazeState,
                    style = HazeStyle(blurRadius = 16.dp)
                )
                .background(Color(0xFF222222).copy(alpha = 0.6f))
                .padding(vertical = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Change Button (Centered above tabs)
                if (currentDestinationIndex == 0 || currentDestinationIndex == 1) {
                    Button(
                        onClick = {
                            if (currentDestinationIndex == 0) {
                                homeViewModel.changeWallpaperNow()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.1f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Change",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change", style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Inner Pill for Tabs
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        bottomNavItems.forEachIndexed { index, screen ->
                            val isSelected = currentDestinationIndex == index
                            val animatedWeight by animateDpAsState(
                                targetValue = if (isSelected) 36.dp else 24.dp,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                label = "tab_size"
                            )
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .background(
                                            color = if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(animatedWeight),
                                        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                                    )
                                    if (!isSelected) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = screen.title,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
