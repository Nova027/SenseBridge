package com.example.sensebridge

import android.content.Context
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.sensebridge.ui.theme.Purple80
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.sensebridge.models.Model
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)

class SceneDescription {
    @Composable
    fun SceneDescriptionUI(mainContext: MainActivity, executor : ExecutorService)
    {
        val imageAnalyser = remember { ImageAnalyserAlgo() }
        val camControl = remember { CameraControl(imageAnalyser) }
        val model = remember { Model() }
        // val model2 = remember { Model() }
        var display = remember { mutableStateOf("") }
        DisposableEffect(Unit) {
            model.setupModel(mainContext, "openai_clip-clipimageencoder.tflite")
            // model2.setupModel(mainContext, "openai_clip-cliptextencoder.tflite")
            imageAnalyser.model = model
            imageAnalyser.onResult = {
                display.value = it[0][0].toString() + " " + it[0][1].toString() + " " + it[0][2].toString() + " " + it[0][3].toString() + " " + it[0][4].toString() +
                        " " + it[0][5].toString() + " " + it[0][6].toString() + " " + it[0][7].toString() + " -- " + it[0].size.toString()
            }
            camControl.setupCamera(mainContext, executor)
            onDispose {
                model.closeModel()
                // model2.closeModel()
            }
        }

        // Screen to display camera preview
        val lifeCycleOwner = LocalLifecycleOwner.current
        camControl.ImagePreview(lifeCycleOwner)

        // Overlaid buttons to flip camera, save snapshot (with transcription)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White)) {
            Text(text = "Output", modifier = Modifier.padding(16.dp), color = Color.Black)
            Text(text = "What? ${display.value}" , modifier = Modifier.padding(16.dp), color = Color.Black)
        }
    }
}