package ru.madfinal.launguageapp.presentation.exercise.animals

class DigitClassifier(context: Context) {

    private val interpreter = Interpreter(
        FileUtil.loadMappedFile(context, "mnist.tflite"),
        Interpreter.Options()
    )
    private val inputWidth = 28
    private val inputHeight = 28

    fun classify(bitmap: Bitmap): String {
        val input = convertBitmapToByteBuffer(
            Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true)
        )
        val output = Array(1) { FloatArray(10) }
        interpreter.run(input, output)
        val result = output[0]
        val maxIndex = result.indices.maxBy { result[it] } ?: -1
        return "Prediction: $maxIndex\nConfidence: ${result[maxIndex]}"
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * inputWidth * inputHeight)
        buffer.order(ByteOrder.nativeOrder())
        val pixels = IntArray(inputWidth * inputHeight)
        bitmap.getPixels(pixels, 0, inputWidth, 0, 0, inputWidth, inputHeight)
        for (p in pixels) {
            val r = (p shr 16) and 0xFF
            val g = (p shr 8) and 0xFF
            val b = p and 0xFF
            buffer.putFloat((r + g + b) / 3f / 255f)
        }
        return buffer
    }

    fun close() {
        interpreter.close()
    }
}