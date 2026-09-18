package com.pexwall.app.ui.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pexwall.app.util.Constants
import com.pexwall.app.util.WallpaperMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var showFrequencyDialog by remember { mutableStateOf(false) }
    var showModeDialog by remember { mutableStateOf(false) }
    var showOrientationDialog by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setNotificationsEnabled(isGranted)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // General Section
            SettingsSectionHeader("General")
            
            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = { viewModel.setApiKey(it) },
                label = { Text("Pexels API Key") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            SettingsToggleItem(
                title = "Auto-Change Wallpapers",
                subtitle = "Periodically update background",
                checked = uiState.autoChangeEnabled,
                onCheckedChange = { viewModel.setAutoChangeEnabled(it) }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Frequency
            val currentFrequency = Constants.FREQUENCY_OPTIONS.find {
                it.intervalMinutes == uiState.frequencyMinutes
            }?.displayName ?: "Unknown"

            SettingsClickableItem(
                title = "Change Frequency",
                subtitle = currentFrequency,
                onClick = { showFrequencyDialog = true }
            )

            SettingsToggleItem(
                title = "WiFi Only",
                subtitle = "Only download when on WiFi",
                checked = uiState.wifiOnly,
                onCheckedChange = { viewModel.setWifiOnly(it) }
            )

            SettingsToggleItem(
                title = "Wallpaper Notifications",
                subtitle = "Notify when wallpaper changes",
                checked = uiState.notificationsEnabled,
                onCheckedChange = { enabled ->
                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.setNotificationsEnabled(enabled)
                    }
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

            // Appearance Section
            SettingsSectionHeader("Appearance")

            SettingsClickableItem(
                title = "Target Screen",
                subtitle = uiState.wallpaperMode.displayName,
                onClick = { showModeDialog = true }
            )
            
            SettingsClickableItem(
                title = "Orientation",
                subtitle = uiState.orientation.replaceFirstChar { it.uppercase() },
                onClick = { showOrientationDialog = true }
            )

            if (uiState.wallpaperMode == WallpaperMode.HOME_ONLY || uiState.wallpaperMode == WallpaperMode.BOTH_SAME) {
                SettingsSliderItem(
                    title = "Home Screen Blur",
                    value = uiState.homeBlurPercent,
                    onValueChange = { viewModel.setHomeBlurPercent(it) }
                )
            }

            if (uiState.wallpaperMode == WallpaperMode.LOCK_ONLY || uiState.wallpaperMode == WallpaperMode.BOTH_SAME) {
                SettingsSliderItem(
                    title = "Lock Screen Blur",
                    value = uiState.lockBlurPercent,
                    onValueChange = { viewModel.setLockBlurPercent(it) }
                )
            }

            Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            
            // About
            SettingsSectionHeader("About")
            ListItem(
                headlineContent = { Text("PexWall version 1.0") },
                supportingContent = { Text("Powered by Pexels API") },
                leadingContent = {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            )
            Spacer(modifier = Modifier.height(100.dp))
        }

        // Dialogs
        if (showFrequencyDialog) {
            AlertDialog(
                onDismissRequest = { showFrequencyDialog = false },
                title = { Text("Select Frequency") },
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

        if (showOrientationDialog) {
            val options = listOf("portrait", "landscape", "any")
            AlertDialog(
                onDismissRequest = { showOrientationDialog = false },
                title = { Text("Wallpaper Orientation") },
                text = {
                    Column {
                        options.forEach { option ->
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
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp, end = 16.dp)
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
fun SettingsClickableItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun SettingsSliderItem(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title)
            Text("$value%")
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..100f
        )
    }
}
