package com.example.praktikum.ui.screens

import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun Home(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        LoadTextFileFromDownloads()
    }
}

@Composable
fun LoadTextFileFromDownloads() {
    var textContent by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var fileName by remember { mutableStateOf(TextFieldValue()) }
    var dialogOpen by remember { mutableStateOf(false) }

    Button(onClick = {
        dialogOpen = true
    }) {
        Text("Daten laden")
    }

    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text("Dateiname eingeben") },
            text = {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Dateiname") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadDir, fileName.text)
                    if (file.exists()) {
                        textContent = file.readText().replace("\n", "\n\n")
                        dialogOpen = false
                    } else {
                        textContent = "Datei nicht gefunden."
                    }
                }) {
                    Text("Daten laden")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = textContent, modifier = Modifier.padding(16.dp))
    }
}
