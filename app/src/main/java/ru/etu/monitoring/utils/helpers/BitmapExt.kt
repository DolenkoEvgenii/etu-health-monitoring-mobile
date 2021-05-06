package ru.etu.monitoring.utils.helpers

import android.graphics.*
import android.media.ExifInterface
import android.util.Base64
import ru.etu.monitoring.utils.BitmapService
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun Bitmap.toBase64(): String {
    var result = ""
    val blob = ByteArrayOutputStream()
    try {
        compress(Bitmap.CompressFormat.JPEG, 100, blob)
        val bitmapBytes = blob.toByteArray()
        result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            blob.flush()
            blob.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return result.removeSpaces().removeLineBreaks()
}

fun Bitmap.resize(w: Number, h: Number): Bitmap {
    val width = width
    val height = height
    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    if (width > 0 && height > 0) {
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    return this
}

fun Bitmap.resize(reqSizePx: Int): Bitmap {
    val resizedBitmap: Bitmap
    val originalWidth = width
    val originalHeight = height
    var newWidth = -1
    var newHeight = -1
    val multiFactor: Float

    if (originalWidth < reqSizePx && originalHeight < reqSizePx) {
        return this
    }

    when {
        originalHeight > originalWidth -> {
            newHeight = reqSizePx
            multiFactor = originalWidth.toFloat() / originalHeight.toFloat()
            newWidth = (newHeight * multiFactor).toInt()
        }
        originalWidth > originalHeight -> {
            newWidth = reqSizePx
            multiFactor = originalHeight.toFloat() / originalWidth.toFloat()
            newHeight = (newWidth * multiFactor).toInt()
        }
        originalHeight == originalWidth -> {
            newHeight = reqSizePx
            newWidth = reqSizePx
        }
    }

    resizedBitmap = Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
    return resizedBitmap
}

fun Bitmap.fixOrientation(filePath: String): Bitmap {
    try {
        val ei = ExifInterface(filePath)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(90f)

            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(180f)

            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(270f)

            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(true, false)

            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(false, true)

            else -> this
        }
    } catch (exc: Exception) {
        return this
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.flip(horizontal: Boolean, vertical: Boolean): Bitmap {
    val matrix = Matrix()
    matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.saveToFile(path: String) {
    val f = File(path)
    if (!f.exists()) {
        f.createNewFile()
    }
    val stream = FileOutputStream(f)
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.flush()
    stream.close()
}

fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(compressFormat, 100, stream)
    return stream.toByteArray()
}

fun File.prepareBitmap(): Bitmap {
    val picturePath = absolutePath

    val tmpOptions = BitmapFactory.Options()
    val options = BitmapFactory.Options()

    tmpOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(picturePath, tmpOptions)
    options.inSampleSize = BitmapService.calculateInSampleSize(tmpOptions, 1080)
    options.inJustDecodeBounds = false

    var bitmap = BitmapFactory.decodeFile(picturePath, options)
    try {
        bitmap = bitmap.fixOrientation(picturePath)
        bitmap = bitmap.resize(1080)
    } catch (exc: Exception) {
        exc.printStackTrace()
    }

    return bitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}

fun Bitmap.withRoundedCorners(radiusX: Float, radiusY: Float): Bitmap {
    try {
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        val rectF = RectF(rect)
        val roundXPx = radiusX.dp
        val roundYPx = radiusY.dp

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundXPx, roundYPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)

        return output
    } catch (exc: Exception) {
        exc.printStackTrace()
        return this
    }
}


fun Bitmap.withRoundedCorners(topLeftCorner: Float, topRightCorner: Float, bottomRightCorner: Float, bottomLeftCorner: Float): Bitmap {
    try {
        val output = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = Color.WHITE
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        val rectF = RectF(rect)
        val path = Path()
        val radii = floatArrayOf(topLeftCorner, bottomLeftCorner, topRightCorner, topRightCorner, bottomRightCorner, bottomRightCorner, bottomLeftCorner, bottomLeftCorner)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        path.addRoundRect(rectF, radii, Path.Direction.CW)
        canvas.drawPath(path, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)
        return output
    } catch (exc: Exception) {
        exc.printStackTrace()
        return this
    }
}

fun Bitmap.cropBitmapTransparency(): Bitmap? {
    var minX = this.width
    var minY = this.height
    var maxX = -1
    var maxY = -1
    for (y in 0 until this.height) {
        for (x in 0 until this.width) {
            val alpha = this.getPixel(x, y) shr 24 and 255
            if (alpha > 0)
            // pixel is not 100% transparent
            {
                if (x < minX)
                    minX = x
                if (x > maxX)
                    maxX = x
                if (y < minY)
                    minY = y
                if (y > maxY)
                    maxY = y
            }
        }
    }
    return if (maxX < minX || maxY < minY) null else Bitmap.createBitmap(this, minX, minY, maxX - minX + 1, maxY - minY + 1) // Bitmap is entirely transparent

}
