package com.example.sensebridge

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.sensebridge.models.Model

class ImageAnalyserAlgo : ImageAnalysis.Analyzer  {
    lateinit var model : Model
    lateinit var onResult: (Array<FloatArray>) -> Unit

    fun ImageProxy.toBitmapCustom() : Bitmap {
        val planeProxy = planes[0]
        val buffer = planeProxy.buffer
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    override fun analyze(image: ImageProxy) {
        // ..
        try {
            var bitmapImage : Bitmap = image.toBitmapCustom()
            var input = model.preProcess(bitmapImage)
            var output = model.inference(input, intArrayOf(1,512))
            onResult(output)
        }
        catch (e : Exception) {
            e.printStackTrace()
        }
        image.close()
    }
}