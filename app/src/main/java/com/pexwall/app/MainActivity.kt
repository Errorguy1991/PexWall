package com.pexwall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pexwall.app.data.preferences.PreferencesManager
import com.pexwall.app.ui.navigation.PexWallNavGraph
import com.pexwall.app.ui.theme.PexWallTheme
import com.pexwall.app.util.ApiKeyHolder
import com.pexwall.app.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PexWallTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.ui.graphics.Color.Transparent
                ) {
                    val hasSeenDialog by preferencesManager.hasSeenApiKeyDialog.collectAsState(initial = false)
                    var showWelcomeDialog by remember { mutableStateOf(false) }
                    
                    // Show welcome dialog only on first launch
                    LaunchedEffect(hasSeenDialog) {
                        if (!hasSeenDialog) {
                            showWelcomeDialog = true
                        }
                    }

                    if (showWelcomeDialog) {
                        WelcomeDialog(
                            onDismiss = {
                                kotlinx.coroutines.MainScope().launch {
                                    preferencesManager.setHasSeenApiKeyDialog(true)
                                }
                                showWelcomeDialog = false
                            }
                        )
                    }
                    
                    PexWallNavGraph()
                }
            }
        }
    }
}

@Composable
fun WelcomeDialog(onDismiss: () -> Unit) {
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Welcome to PexWall!") },
        text = {
            Column {
                Text("PexWall brings beautiful wallpapers directly to your home and lock screens.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("By default, the app uses Bing's daily wallpapers, which requires no setup!")
                Spacer(modifier = Modifier.height(8.dp))
                Text("If you want to unlock millions of high-quality photos from Pexels or Unsplash, you will need to provide your own free API keys to prevent rate-limiting.")
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.TextButton(onClick = { uriHandler.openUri("https://www.pexels.com/api/") }) {
                    Text("Get free Pexels Key")
                }
                androidx.compose.material3.TextButton(onClick = { uriHandler.openUri("https://unsplash.com/developers") }) {
                    Text("Get free Unsplash Key")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Once generated, you can paste them in the Settings tab under 'API KEYS'.")
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(onClick = onDismiss) {
                Text("Got it!")
            }
        }
    )
}

@Composable
fun ApiKeyDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pexels API Key Required") },
        text = {
            Column {
                Text("Please enter your own Pexels API key to ensure unlimited wallpaper downloads.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("API Key") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotBlank()) onSave(text) },
                enabled = text.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Use Default")
            }
        }
    )
}
