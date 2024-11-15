package com.example.sensebridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sensebridge.ui.theme.SenseBridgeTheme

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
            // Text("${stringFromJNI()} ${stringFromJNI2()}!", modifier = Modifier.padding(50.dp))

            HomeScreenUI()
        }
    }

    @Composable
    fun HomeScreenUI()
    {
        Scaffold(topBar = { TopBar("Home") }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            {
                Text(text = "$innerPadding ${stringFromJNI()} ${stringFromJNI2()}!", modifier = Modifier.padding(16.dp))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(text: String = "Top Bar")
    {
        TopAppBar(colors = topAppBarColors(containerColor = Color.Blue),
            title = {Text(text, color = Color.White)})
    }
}