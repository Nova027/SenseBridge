package com.example.sensebridge

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService

class CameraControl(val camAnalyser : ImageAnalyserAlgo) {
    lateinit var camController : LifecycleCameraController
    lateinit var previewView: PreviewView
    var setupComplete : Boolean = false

    fun setupCamera(context: Context, executor: ExecutorService, lifecycleOwner : LifecycleOwner) {
        camController = LifecycleCameraController(context.applicationContext).apply {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                setEnabledUseCases(LifecycleCameraController.IMAGE_ANALYSIS)
                setImageAnalysisAnalyzer(executor, camAnalyser)
                setImageAnalysisBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                setImageAnalysisOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            }
        camController.bindToLifecycle(lifecycleOwner)

        previewView = PreviewView(context)
        previewView.controller = camController

        setupComplete = true
    }

    fun switchCamera() {
        if (!setupComplete)
            return
        camController.cameraSelector =
            if (camController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
    }

    @Composable
    fun ImagePreview() {
        if (!setupComplete)
            return
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
    }

    fun shutdownCamera() {
        if (!setupComplete)
            return
        camController.clearImageAnalysisAnalyzer()
        camController.unbind()
        previewView.controller = null
        setupComplete = false
    }
}