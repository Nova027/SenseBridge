package com.example.sensebridge.models
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

open class Model() {
    private var modelInterpreter: Interpreter? = null
    private var gpuDelegate: GpuDelegate? = null

    fun setupModel(context: Context, modelName: String) {
        // Initialize GPU delegate if available
        // gpuDelegate = try {
        //    GpuDelegate()
        // } catch (e: Exception) {
        //     e.printStackTrace()
        //     null
        // }
        // Initialize model here
        modelInterpreter = try {
            loadModelFile(modelName, context)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    open fun preProcess(input : Bitmap) : Array<Array<FloatArray>> {
        val resizedBitmap = Bitmap.createScaledBitmap(input, 224, 224, true) // bilinear
        return toNormalizedRGBArray(resizedBitmap,
            Triple(0.48145466f, 0.4578275f, 0.40821073f),
            Triple(0.26862954f, 0.26130258f, 0.27577711f))
    }

    private fun loadModelFile(modelName: String, context: Context): Interpreter {
        val assetFileDescriptor = context.assets.openFd(modelName)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val mappedByteBuffer: MappedByteBuffer = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )
        // val fileChannel = assetFileDescriptor.createInputStream()
        // val fileContent = fileChannel.readBytes()
        // val buffer = ByteBuffer.allocateDirect(fileContent.size) // Create a ByteBuffer
        // buffer.put(fileContent) // Copy the ByteArray to the ByteBuffer
        // buffer.rewind() // Rewind the buffer to the beginning
        val interpreterOptions = Interpreter.Options()
        if (gpuDelegate != null)
            interpreterOptions.addDelegate(gpuDelegate)
        return Interpreter(mappedByteBuffer, interpreterOptions) // Use the ByteBuffer to create the Interpreter
    }

    fun inference(input : Array<Array<FloatArray>>, outputShape : IntArray) : Array<FloatArray> {
        val output = Array(outputShape[0]) { FloatArray(outputShape[1]) }
        val inputBatch = Array(1) { input }
        modelInterpreter?.run(inputBatch, output)
        return output
    }

    fun postProcess(output : FloatArray) : String {
        return ""
    }

    open fun closeModel() {
        modelInterpreter?.close()
        modelInterpreter = null
        gpuDelegate?.close()
        gpuDelegate = null
    }


    // SHIFT
    fun toNormalizedRGBArray(bitmap: Bitmap,
                             mean: Triple<Float, Float, Float>,
                             std: Triple<Float, Float, Float>): Array<Array<FloatArray>> {
        val width = bitmap.width
        val height = bitmap.height
        val rgbArray = Array(height) { Array(width) {FloatArray(3)} }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)

                rgbArray[y][x][0] = Color.blue(pixel) / 255f            // This is R
                rgbArray[y][x][1] = Color.green(pixel) / 255f
                rgbArray[y][x][2] = Color.red(pixel) / 255f             // This is B

                rgbArray[y][x][0] = (rgbArray[y][x][0] - mean.first) / std.first
                rgbArray[y][x][1] = (rgbArray[y][x][1] - mean.second) / std.second
                rgbArray[y][x][2] = (rgbArray[y][x][2] - mean.third) / std.third
            }
        }

        return rgbArray
    }
}