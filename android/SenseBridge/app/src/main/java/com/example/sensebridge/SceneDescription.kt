package com.example.sensebridge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import com.example.sensebridge.models.Model
import java.util.concurrent.ExecutorService
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)

class SceneDescription {
    @Composable
    fun SceneDescriptionUI(mainContext: MainActivity, executor : ExecutorService)
    {
        val imageAnalyser = remember { ImageAnalyserAlgo() }
        val camControl = remember { CameraControl(imageAnalyser) }
        val model = remember { Model() }
        var display = remember { mutableStateOf("") }
        val lifeCycleOwner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            model.setupModel(mainContext, "openai_clip-clipimageencoder.tflite")
            // model2.setupModel(mainContext, "openai_clip-cliptextencoder.tflite")
            imageAnalyser.model = model
            imageAnalyser.onResult = {
                display.value = it[0][0].toString() + " " + it[0][1].toString() + " " + it[0][2].toString() + " " + it[0][3].toString() + " " + it[0][4].toString() +
                        " " + it[0][5].toString() + " " + it[0][6].toString() + " " + it[0][7].toString() + " -- " + it[0].size.toString()
            }
            camControl.setupCamera(mainContext, executor, lifeCycleOwner)
            onDispose {
                camControl.shutdownCamera()
                model.closeModel()
            }
        }

        // Screen to display camera preview
        camControl.ImagePreview()

        // Debug display
        Column(modifier = Modifier.size(400.dp).padding(16.dp).background(Color.White)) {
            Text(text = "Output", modifier = Modifier.padding(16.dp), color = Color.Black)
            Text(text = "What? ${display.value}" , modifier = Modifier.padding(16.dp), color = Color.Black)
        }

        // Overlaid buttons to flip camera, save snapshot (with transcription)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 32.dp))
        {
            IconButton(
                onClick = { camControl.switchCamera() },
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD0BCFF).copy(alpha = 0.3f))
            ) {
                Icon(painter = painterResource(R.drawable.flip_camera_android_24px),
                    tint = Color.White, contentDescription = "Flip Camera")
            }
            Spacer(modifier = Modifier.width(64.dp))
            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(Color.White.copy(alpha = 0.85f)),
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .clip(CircleShape)
            ) { }
        }
    }
}