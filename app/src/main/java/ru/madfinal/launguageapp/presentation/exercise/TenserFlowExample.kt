package ru.madfinal.launguageapp.presentation.exercise

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class TenserFlowExample(private val context: Context) {

    private var objectDetector: ObjectDetector? = null

    init {
        try {
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(1)
                .setScoreThreshold(0.5f)
                .build()
            objectDetector = ObjectDetector.createFromFileAndOptions(
                context,
                "model.tfile",
                options
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun recognizeImage(bitmap: Bitmap, callback: (String) -> Unit) {
        try {
            val image = TensorImage.fromBitmap(bitmap)
            val results = objectDetector?.detect(image)

            if (results != null && results.isNotEmpty()) {
                val detectedObject = results[0].categories[0].label
                callback(detectedObject)
            } else {
                callback("unknown")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback("error")
        }
    }
}