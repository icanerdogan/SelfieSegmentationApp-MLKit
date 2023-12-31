package com.ibrahimcanerdogan.selfiesegmentationapp.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.*
import java.sql.Timestamp
import java.util.*


object ImageUtils {

    @Throws(FileNotFoundException::class)
    fun download(
        bitmap: Bitmap,
        context: Context
    ) {

        if (Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/selfiesegmentation")
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            if (uri != null) {
                saveImageToStream(context, bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }

        } else {

            val dir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                ""
            )

            if (!dir.exists()) dir.mkdirs()

            val date = Date()

            val fullFileName = "myFileName.jpeg"
            val fileName = fullFileName.substring(0, fullFileName.lastIndexOf("."))

            val imageFile = File(
                dir.absolutePath.toString()
                        + File.separator
                        + fileName + "_" + Timestamp(date.time).toString()
                        + ".jpg"
            )

            saveImageToStream(context, bitmap, FileOutputStream(imageFile))

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)

            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        }

    }

    private fun saveImageToStream(context: Context, bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                Toast.makeText(context, "Image downloaded successfully.", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun mergeBitmaps(bmp1: Bitmap, bmp2: Bitmap): Bitmap {
        val merged = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
        val canvas = Canvas(merged)
        canvas.drawBitmap(bmp1, Matrix(), null)
        canvas.drawBitmap(bmp2, Matrix(), null)
        return merged
    }

    fun resizeBitmap(bmp: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bmp, width, height, false)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }
}