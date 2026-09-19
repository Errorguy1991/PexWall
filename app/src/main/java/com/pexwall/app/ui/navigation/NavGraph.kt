package com.pexwall.app.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pexwall.app.ui.categories.CategoriesScreen
import com.pexwall.app.ui.history.HistoryScreen
import com.pexwall.app.ui.home.HomeScreen
import com.pexwall.app.ui.settings.SettingsScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.HazeStyle

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

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Categories,
    Screen.History,
    Screen.Settings
)

@Composable
fun PexWallNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val hazeState = remember { HazeState() }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize().haze(state = hazeState)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Categories.route) { CategoriesScreen() }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }

        // iOS Style Floating Liquid Glass Bottom Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp)
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .hazeChild(
                    state = hazeState,
                    style = HazeStyle(blurRadius = 16.dp)
                )
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
        ) {
            val selectedIndex = bottomNavItems.indexOfFirst { screen ->
                currentDestination?.hierarchy?.any { it.route == screen.route } == true
            }.takeIf { it >= 0 } ?: 0
            
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val itemWidth = maxWidth / bottomNavItems.size
                val indicatorOffset by animateDpAsState(
                    targetValue = itemWidth * selectedIndex,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioLowBouncy)
                )
                
                // Animated Pill Indicator
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(itemWidth)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomNavItems.forEachIndexed { index, screen ->
                        val selected = selectedIndex == index
                        val interactionSource = remember { MutableInteractionSource() }
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null // Disable standard ripple for custom pill
                                ) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
