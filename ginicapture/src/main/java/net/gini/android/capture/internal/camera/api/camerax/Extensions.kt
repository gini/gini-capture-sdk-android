package net.gini.android.capture.internal.camera.api.camerax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.util.Rational
import android.util.Size
import android.view.Surface
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.ImageOutputConfig
import net.gini.android.capture.internal.camera.api.CameraException
import java.io.ByteArrayOutputStream
import java.io.IOException

fun ImageProxy.toByteArray(): ByteArray {
    val buffer = planes[0].buffer
    buffer.rewind()
    val byteArray = ByteArray(buffer.remaining())
    buffer.get(byteArray)
    return byteArray
}

fun ImageProxy.shouldCrop(): Boolean {
    val sourceSize = Size(width, height)
    val targetSize = Size(cropRect.width(), cropRect.height())
    return targetSize != sourceSize
}

fun ImageProxy.toCroppedByteArray(): ByteArray {
    val byteArray = toByteArray()
    if (shouldCrop()) {
        return cropByteArray(byteArray, cropRect)
    }
    return byteArray
}

@Throws(CameraException::class)
private fun cropByteArray(data: ByteArray, cropRect: Rect?): ByteArray {
    if (cropRect == null) {
        return data
    }
    try {
        val decoder = BitmapRegionDecoder.newInstance(data, 0, data.size, false)
        val bitmap = decoder.decodeRegion(cropRect, BitmapFactory.Options())
        decoder.recycle()

        val out = ByteArrayOutputStream()
        val success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

        if (!success) {
            throw CameraException(
                "Encode bitmap failed.",
                CameraException.Type.SHOT_FAILED
            )
        }

        bitmap.recycle()
        return out.toByteArray()
    } catch (e: IllegalArgumentException) {
        throw CameraException(e, CameraException.Type.SHOT_FAILED)
    } catch (e: IOException) {
        throw CameraException(e, CameraException.Type.SHOT_FAILED)
    }
}

/**
 * Swaps width and height for portrait.
 *
 * @param rotation a [Surface].`ROTATION_X` constant: [Surface.ROTATION_0]
 */
fun Size.forRotation(@ImageOutputConfig.RotationValue rotation: Int): Size =
    when (rotation) {
        Surface.ROTATION_0, Surface.ROTATION_180 -> Size(height, width)
        Surface.ROTATION_90, Surface.ROTATION_270 -> Size(width, height)
        else -> this
    }

/**
 * Swaps numerator and denominator for portrait.
 *
 * @param rotation a [Surface].`ROTATION_X` constant: [Surface.ROTATION_0]
 */
fun Rational.forRotation(@ImageOutputConfig.RotationValue rotation: Int): Rational =
    when (rotation) {
        Surface.ROTATION_0, Surface.ROTATION_180 -> Rational(denominator, numerator)
        Surface.ROTATION_90, Surface.ROTATION_270 -> Rational(numerator, denominator)
        else -> this
    }