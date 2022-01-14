package io.intelligible.intelligentocr.utils


import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import io.intelligible.intelligentocr.ui.CameraTextAnalyzerListener


class TextAnalyser(
    private val textListener: CameraTextAnalyzerListener,
    private var context: Context,
    private val fromFile: Uri,
) {

    private val recognizer by lazy {
      //  TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
    }

    fun analyseImage() {

        try {


            val image = InputImage.fromFilePath(context, fromFile)


            recognizer.process(image)
                .addOnSuccessListener { visionText ->

                    Log.e("text", visionText.text)

                        textListener(visionText.text)

                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Log.e("text", e.localizedMessage)
                }


        } catch (e: Exception) {

            Log.e("text ", e.localizedMessage)

        }
    }


}










