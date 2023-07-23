package com.ibrahimcanerdogan.selfiesegmentationapp.fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageProxy
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.GroundInstance
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.ImageUtils
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.ImageUtils.mergeBitmaps
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.SegmenterOptions
import java.nio.ByteBuffer

class ImageAnalyzer {

    private lateinit var mask : ByteBuffer
    private var maskWidth : Int = 0
    private var maskHeight : Int = 0

    fun analyze(bitmap: Bitmap?) {
        bitmap?.let { it ->
            val image = InputImage.fromBitmap(bitmap, 0)
            SegmenterOptions.imageSegmenter().process(image)
                .addOnSuccessListener {sMask ->
                    mask = sMask.buffer
                    maskWidth = sMask.width
                    maskHeight = sMask.height

                    GroundInstance.getInstance()?.foreground = generateMaskImage(it)
                }
                .addOnFailureListener {
                    Log.e("ImageAnalyzer", it.message.toString())
                }
        }
    }

    fun analyzeWithBG(bitmap: Bitmap?, background : Bitmap) {
        bitmap?.let { it ->
            val image = InputImage.fromBitmap(bitmap, 0)
            SegmenterOptions.imageSegmenter().process(image)
                .addOnSuccessListener {sMask ->
                    mask = sMask.buffer
                    maskWidth = sMask.width
                    maskHeight = sMask.height

                    GroundInstance.getInstance()?.background = generateMaskBgImage(it, background)
                }
                .addOnFailureListener {
                    Log.e("ImageAnalyzer", it.message.toString())
                }
        }
    }

    private fun generateMaskImage(image: Bitmap): Bitmap {
        val maskBitmap = Bitmap.createBitmap(image.width, image.height, image.config)

        for (y in 0 until maskHeight) {
            for (x in 0 until maskWidth) {
                val bgConfidence = ((1.0 - mask.float) * 255).toInt()
                maskBitmap.setPixel(x, y, Color.argb(bgConfidence, 255, 255, 255))
            }
        }
        mask.rewind()
        return mergeBitmaps(image, maskBitmap)
    }

    /**
     * Uses the detected segmentation mask to overlay the background image
     */
    fun generateMaskBgImage(image: Bitmap, bg: Bitmap): Bitmap {
        val bgBitmap = Bitmap.createBitmap(image.width, image.height, image.config)

        for (y in 0 until maskHeight) {
            for (x in 0 until maskWidth) {
                val bgConfidence = ((1.0 - mask.float) * 255).toInt()
                var bgPixel = bg.getPixel(x, y)
                bgPixel = ColorUtils.setAlphaComponent(bgPixel, bgConfidence)
                bgBitmap.setPixel(x, y, bgPixel)
            }
        }
        mask.rewind()
        return mergeBitmaps(image, bgBitmap)
    }
}