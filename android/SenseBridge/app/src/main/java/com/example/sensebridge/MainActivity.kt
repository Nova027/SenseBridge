package com.example.sensebridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sensebridge.ui.theme.SenseBridgeTheme
@OptIn(ExperimentalMaterial3Api::class)

class MainActivity : ComponentActivity()
{
    private external fun stringFromJNI(): String
    private external fun stringFromJNI2(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("sensebridge_cpp_v1")
        System.loadLibrary("sensebridge_cpp_v2")
        enableEdgeToEdge()
        setContent {
            HomeScreenUI()
        }
    }

    @Composable
    fun HomeScreenUI()
    {
        var showAddSessionDialog = remember { mutableStateOf(false) }

        Scaffold(
            topBar = { TopBar("Home") },
            floatingActionButton = { AddSessionButton(showAddSessionDialog) })
        { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            {
                Text(text = "$innerPadding ${stringFromJNI()} ${stringFromJNI2()}!", modifier = Modifier.padding(16.dp))
            }
        }

        if (showAddSessionDialog.value)
        {
            AddSessionDialog(showAddSessionDialog)
        }
    }

    @Composable
    fun TopBar(text: String = "Top Bar")
    {
        TopAppBar(colors = topAppBarColors(containerColor = Color(0xFF44A6CC)),
            title = {Text(text, color = Color.White)})
    }

    @Composable
    fun AddSessionButton(showAddSessionDialog: MutableState<Boolean>)
    {
        FloatingActionButton(onClick = { showAddSessionDialog.value = true }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }

    @Composable
    fun AddSessionDialog(showAddSessionDialog: MutableState<Boolean>)
    {
        AlertDialog(
            onDismissRequest = { showAddSessionDialog.value = false },
            title = { Text("Dialog Title") },
            text = { Text("This is the dialog content.") },
            confirmButton = {
                TextButton(onClick = { showAddSessionDialog.value = false }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSessionDialog.value = false }) {
                    Text("Dismiss")
                }
            }
        )
    }
}