package com.dicoding.myahi.data.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.dicoding.myahi.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILE_NAME_FORMAT = "yyyyMMdd_HHmmss"
private val currentTimestamp: String = SimpleDateFormat(FILE_NAME_FORMAT, Locale.US).format(Date())
private const val MAX_FILE_SIZE = 1000000

fun generateImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$currentTimestamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: createUriForLegacyDevices(context)
}

private fun createUriForLegacyDevices(context: Context): Uri {
    val pictureDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(pictureDir, "/MyCamera/$currentTimestamp.jpg").apply {
        parentFile?.takeIf { !it.exists() }?.mkdir()
    }
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

fun createTemporaryImageFile(context: Context): File {
    val cacheDir = context.externalCacheDir
    return File.createTempFile(currentTimestamp, ".jpg", cacheDir)
}

fun convertUriToFile(imageUri: Uri, context: Context): File {
    val tempFile = createTemporaryImageFile(context)
    context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }
    return tempFile
}

fun File.compressImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path).adjustOrientation(this)
    var compressionQuality = 100
    var outputSize: Int
    do {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressionQuality, byteArrayOutputStream)
        outputSize = byteArrayOutputStream.size()
        compressionQuality -= 5
    } while (outputSize > MAX_FILE_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressionQuality, FileOutputStream(this))
    return this
}

fun Bitmap.adjustOrientation(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(this, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(this, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(this, 270f)
        else -> this
    }
}

fun rotateBitmap(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix().apply { postRotate(angle) }
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}
