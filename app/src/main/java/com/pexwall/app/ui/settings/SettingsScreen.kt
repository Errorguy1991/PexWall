package com.pexwall.app.ui.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pexwall.app.util.Constants
import com.pexwall.app.util.WallpaperMode
import com.pexwall.app.util.WallpaperSource
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.HazeStyle
import com.pexwall.app.ui.navigation.LocalHazeState
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var showFrequencyDialog by remember { mutableStateOf(false) }
    var showModeDialog by remember { mutableStateOf(false) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var showOrientationDialog by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setNotificationsEnabled(isGranted)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent.copy(alpha = 0.8f)
                )
            )
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            
            // General Section
            SettingsSectionHeader("GENERAL")
            SettingsGroup {
                SettingsClickableItem(
                    title = "Wallpaper Source",
                    subtitle = uiState.wallpaperSource.displayName,
                    onClick = { showSourceDialog = true }
                )
                SettingsDivider()
                SettingsToggleItem(
                    title = "Auto-Change Wallpapers",
                    subtitle = "Periodically update background",
                    checked = uiState.autoChangeEnabled,
                    onCheckedChange = { viewModel.setAutoChangeEnabled(it) }
                )
            }

            // Frequency
            SettingsSectionHeader("UPDATE RULES")
            SettingsGroup {
                val currentFrequency = Constants.FREQUENCY_OPTIONS.find {
                    it.intervalMinutes == uiState.frequencyMinutes
                }?.displayName ?: "Unknown"

                SettingsClickableItem(
                    title = "Change Frequency",
                    subtitle = currentFrequency,
                    onClick = { showFrequencyDialog = true }
                )
                SettingsDivider()
                SettingsToggleItem(
                    title = "WiFi Only",
                    subtitle = "Only download when on WiFi",
                    checked = uiState.wifiOnly,
                    onCheckedChange = { viewModel.setWifiOnly(it) }
                )
            }
            
            // Appearance Section
            SettingsSectionHeader("APPEARANCE")
            SettingsGroup {
                SettingsClickableItem(
                    title = "Target Screen",
                    subtitle = uiState.wallpaperMode.displayName,
                    onClick = { showModeDialog = true }
                )
                SettingsDivider()
                SettingsClickableItem(
                    title = "Orientation",
                    subtitle = uiState.orientation.replaceFirstChar { it.uppercase() },
                    onClick = { showOrientationDialog = true }
                )

                if (uiState.wallpaperMode != WallpaperMode.LOCK_ONLY) {
                    SettingsDivider()
                    SettingsSliderItem(
                        title = "Home Screen Blur",
                        value = uiState.homeBlurPercent,
                        onValueChange = { viewModel.setHomeBlurPercent(it) }
                    )
                }

                if (uiState.wallpaperMode != WallpaperMode.HOME_ONLY) {
                    SettingsDivider()
                    SettingsSliderItem(
                        title = "Lock Screen Blur",
                        value = uiState.lockBlurPercent,
                        onValueChange = { viewModel.setLockBlurPercent(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // padding for bottom bar
        }
    }

    // Dialogs
    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Wallpaper Source") },
            text = {
                Column {
                    WallpaperSource.values().forEach { source ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setWallpaperSource(source)
                                    showSourceDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = source == uiState.wallpaperSource, onClick = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(source.displayName)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showSourceDialog = false }) { Text("Cancel") } }
        )
    }

    if (showModeDialog) {
        AlertDialog(
            onDismissRequest = { showModeDialog = false },
            title = { Text("Target Screen") },
            text = {
                Column {
                    WallpaperMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setWallpaperMode(mode)
                                    showModeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = mode == uiState.wallpaperMode,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(mode.displayName)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showModeDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showFrequencyDialog) {
        AlertDialog(
            onDismissRequest = { showFrequencyDialog = false },
            title = { Text("Change Frequency") },
            text = {
                Column {
                    Constants.FREQUENCY_OPTIONS.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setFrequency(option.intervalMinutes)
                                    showFrequencyDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = option.intervalMinutes == uiState.frequencyMinutes,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(option.displayName)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFrequencyDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showOrientationDialog) {
        AlertDialog(
            onDismissRequest = { showOrientationDialog = false },
            title = { Text("Orientation") },
            text = {
                Column {
                    listOf("portrait", "landscape", "square", "any").forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setOrientation(option)
                                    showOrientationDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = option == uiState.orientation,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(option.replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showOrientationDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha=0.1f), RoundedCornerShape(12.dp))
            
            .background(Color.White.copy(alpha=0.05f))
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 16.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 32.dp, top = 24.dp, bottom = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsClickableItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SettingsSliderItem(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text("%", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..100f
        )
    }
}
