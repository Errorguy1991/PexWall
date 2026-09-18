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
import com.pexwall.app.ui.navigation.NavGraph
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    val apiKey by preferencesManager.apiKey.collectAsState(initial = "")
                    var showApiKeyDialog by remember { mutableStateOf(false) }
                    
                    // Show dialog if API key is empty or is the default provided key
                    LaunchedEffect(apiKey) {
                        if (apiKey.isBlank() || apiKey == Constants.DEFAULT_API_KEY) {
                            // Only show once per session if they dismiss it
                            showApiKeyDialog = true
                        }
                    }

                    if (showApiKeyDialog) {
                        ApiKeyDialog(
                            onDismiss = { showApiKeyDialog = false },
                            onSave = { newKey ->
                                kotlinx.coroutines.MainScope().launch {
                                    ApiKeyHolder.apiKey = newKey
                                    preferencesManager.setApiKey(newKey)
                                }
                                showApiKeyDialog = false
                            }
                        )
                    }
                    
                    NavGraph()
                }
            }
        }
    }
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
