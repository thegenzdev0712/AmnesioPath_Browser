package com.thegenzdev.amnesiopathbrowser.screens

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit = {}){


    var showAboutDialog by remember { mutableStateOf(false) }

    var showOriginDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back button")
                    }
                }
            )
        }
    ) {  padding->

        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
        ) {
            ListItem(
                headlineContent = { Text("About AmnesioPath") },
                modifier = Modifier.clickable{ showAboutDialog = true }
            )
            HorizontalDivider()

            ListItem(
                headlineContent = { Text("Why use AmnesioPath?") },
                modifier = Modifier.clickable{ showOriginDialog = true }
            )
        }

        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("About AmnesioPath") },
                text = {
                    Text("App Version: 1.0\nDeveloped By ~ TheGenZDev")
                }
            )
        }

        if (showOriginDialog) {
            AlertDialog(
                onDismissRequest = { showOriginDialog = false },
                confirmButton = {
                    TextButton(onClick = { showOriginDialog = false }) {
                        Text("DAMN")
                    }
                },
                title = { Text("The Philosophy Behind AmnesioPath") },
                text = {
                    Text(
                        "The name AmnesioPath emerged from a condition of deliberate forgetting.\n" +
                                "\n" +
                                "'Amnesio' evokes amnesia. The browser forgets everything — no tabs, no cookies, no memory.\n" +
                                "\n" +
                                "'Path' refers to pathology. Not a journey, but a disorder. A ritualized refusal to retain.\n" +
                                "\n" +
                                "Together, AmnesioPath is a mythic browser afflicted by design. It does not remember. It dissociates.\n" +
                                "\n" +
                                "Built for those who browse like ghosts, not archivists."
                    )
                }
            )
        }

    }
}