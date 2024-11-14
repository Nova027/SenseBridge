package com.example.sensebridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sensebridge.ui.theme.SenseBridgeTheme

class MainActivity : ComponentActivity()
{
    companion object {
        init {
            System.loadLibrary("sensebridge_cpp_v1")
            System.loadLibrary("sensebridge_cpp_v2")
        }
    }

    private external fun stringFromJNI(): String
    private external fun stringFromJNI2(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SenseBridgeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "${stringFromJNI()} ${stringFromJNI2()} Hello $name!",
            modifier = modifier
        )
    }
}