package net.gini.android.capture.internal.camera.api

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.os.Build
import android.util.Rational
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.core.impl.ImageOutputConfig
import androidx.camera.lifecycle.ExperimentalUseCaseGroupLifecycle
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import jersey.repackaged.jsr166e.CompletableFuture
import net.gini.android.capture.Document
import net.gini.android.capture.internal.camera.api.camerax.forRotation
import net.gini.android.capture.internal.camera.api.camerax.toByteArray
import net.gini.android.capture.internal.camera.api.camerax.toCroppedByteArray
import net.gini.android.capture.internal.camera.photo.Photo
import net.gini.android.capture.internal.camera.photo.PhotoFactory
import net.gini.android.capture.internal.camera.view.CameraXPreviewContainer
import net.gini.android.capture.internal.util.DeviceHelper
import net.gini.android.capture.internal.util.Size
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.RejectedExecutionException
import kotlin.math.roundToInt

private val LOG: Logger = LoggerFactory.getLogger(CameraXController::class.java)

// Auto focus is 1/6 of the area.
private const val AF_SIZE = 1.0f / 6.0f
private const val AE_SIZE = AF_SIZE * 1.5f

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class CameraXController(val activity: Activity) : CameraInterface {

    private val cameraLifecycle: CameraLifecycle = CameraLifecycle()
    private var camera: androidx.camera.core.Camera? = null

    private var previewUseCase: Preview? = null
    private val previewContainer: CameraXPreviewContainer = CameraXPreviewContainer(activity)

    private var imageCaptureUseCase: ImageCapture? = null

    override fun open(): CompletableFuture<Void> {
        val openFuture = CompletableFuture<Void>()

        val cameraProviderFuture = try {
            ProcessCameraProvider.getInstance(activity)
        } catch (e: IllegalStateException) {
            LOG.error("Failed to get ProcessCameraProvider instance future", e)
            openFuture.completeExceptionally(CameraException(e, CameraException.Type.OPEN_FAILED))
            return openFuture
        }

        try {
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = try {
                    cameraProviderFuture.get()
                } catch (e: Exception) {
                    LOG.error("Failed to get ProcessCameraProvider instance", e)
                    openFuture.completeExceptionally(
                        CameraException(e, CameraException.Type.OPEN_FAILED)
                    )
                    return@addListener
                }

                val targetRotation = previewContainer.previewView.display.rotation
                val targetResolution = android.util.Size(3000, 4000)

                LOG.debug(
                    "Opening camera for target rotation {} and target resolution {}",
                    targetRotation, targetResolution
                )

                val preview = Preview.Builder()
                    .setTargetResolution(targetResolution)
                    .setTargetRotation(targetRotation)
                    .build()

                preview.setSurfaceProvider { request ->
                    LOG.debug("Using preview resolution {}", request.resolution)
                    previewContainer.setPreviewSize(
                        android.util.Size(
                            request.resolution.width,
                            request.resolution.height
                        ).forRotation(targetRotation)
                    )
                    previewContainer.previewView.surfaceProvider.onSurfaceRequested(request)
                }

                previewUseCase = preview

                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setTargetResolution(targetResolution)
                    .setTargetRotation(targetRotation)
                    .build()

                imageCaptureUseCase = imageCapture

                @OptIn(markerClass = [ExperimentalUseCaseGroup::class])
                val viewPort = ViewPort.Builder(
                    Rational(4, 3).forRotation(targetRotation),
                    targetRotation
                ).build()

                @OptIn(markerClass = [ExperimentalUseCaseGroup::class])
                val useCaseGroup = UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .addUseCase(imageCapture)
                    .setViewPort(viewPort)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    @OptIn(markerClass = [ExperimentalUseCaseGroupLifecycle::class])
                    camera = cameraProvider.bindToLifecycle(
                        cameraLifecycle,
                        cameraSelector,
                        useCaseGroup
                    )

                    // TODO: move to a method to get the supported resolutions
//                    @SuppressLint("RestrictedApi", "UnsafeOptInUsageError")
//                    val characteristics = Camera2CameraInfo.extractCameraCharacteristics(camera!!.cameraInfo)
//                    val streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
//                    val outputSizes = streamConfigurationMap?.getOutputSizes(ImageFormat.JPEG)

                    LOG.info("Camera is open")

                    openFuture.complete(null)
                } catch (e: Exception) {
                    LOG.error("Preview use case binding failed", e)
                    openFuture.completeExceptionally(
                        CameraException(
                            e,
                            CameraException.Type.NO_PREVIEW
                        )
                    )
                }
            }, ContextCompat.getMainExecutor(activity))
        } catch (e: RejectedExecutionException) {
            LOG.error("Failed to add ProcessCameraProvider listener", e)
            openFuture.completeExceptionally(CameraException(e, CameraException.Type.OPEN_FAILED))
            return openFuture
        }

        cameraLifecycle.start()

        return openFuture
    }

    override fun close() {
        cameraLifecycle.stop()
        previewUseCase = null
        camera = null
        imageCaptureUseCase = null
    }

    override fun startPreview(): CompletableFuture<Void> {
        // Not needed for CameraX
        return CompletableFuture.completedFuture(null)
    }

    override fun stopPreview() {
        // Not needed for CameraX
    }

    override fun isPreviewRunning(): Boolean {
        return camera != null
    }

    override fun enableTapToFocus(listener: CameraInterface.TapToFocusListener?) {
        LOG.info("Tap to focus enabled")
        previewContainer.previewView.setOnTouchListener { _, event ->
            val isSingleTouch = event.pointerCount == 1
            val isUpEvent = event.action == MotionEvent.ACTION_UP
            val notALongPress = (event.eventTime - event.downTime
                    < ViewConfiguration.getLongPressTimeout())
            if (isSingleTouch && isUpEvent && notALongPress) {
                focusAtPoint(event.x, event.y)
            }
            true
        }
    }

    private fun focusAtPoint(
        x: Float,
        y: Float,
        listener: CameraInterface.TapToFocusListener? = null
    ) {
        LOG.debug("Focusing at point ({}, {})", x, y)

        val meteringPointFactory = previewContainer.previewView.meteringPointFactory
        val afPoint = meteringPointFactory.createPoint(x, y, AF_SIZE)
        val aePoint = meteringPointFactory.createPoint(x, y, AE_SIZE)

        val focusMeteringAction =
            FocusMeteringAction.Builder(afPoint, FocusMeteringAction.FLAG_AF)
                .addPoint(aePoint, FocusMeteringAction.FLAG_AE)
                .build()

        listener?.onFocusing(
            Point(x.roundToInt(), y.roundToInt()),
            Size(previewContainer.previewView.width, previewContainer.previewView.height)
        )

        val focusFuture = camera?.cameraControl?.startFocusAndMetering(focusMeteringAction)

        focusFuture?.addListener({
            val result = focusFuture.get()
            LOG.debug("Focus result: {}", result.isFocusSuccessful)
            listener?.onFocused(result.isFocusSuccessful)
        }, ContextCompat.getMainExecutor(activity))
    }

    override fun disableTapToFocus() {
        LOG.info("Tap to focus disabled")
        previewContainer.previewView.setOnTouchListener(null)
    }

    override fun focus(): CompletableFuture<Boolean> {
        LOG.info("Focus at preview center")
        val focusFuture = CompletableFuture<Boolean>()

        focusAtPoint(
            previewContainer.previewView.width / 2f,
            previewContainer.previewView.height / 2f,
            object : CameraInterface.TapToFocusListener {
                override fun onFocusing(point: Point, previewViewSize: Size) {}

                override fun onFocused(success: Boolean) {
                    focusFuture.complete(success)
                }
            })

        return focusFuture
    }

    override fun takePicture(): CompletableFuture<Photo> {
        LOG.info("Take picture")

        val pictureFuture = CompletableFuture<Photo>()

        if (camera == null) {
            LOG.error("Cannot take picture: camera not open")
            pictureFuture.completeExceptionally(
                CameraException(
                    "Cannot take picture: camera not open",
                    CameraException.Type.SHOT_FAILED
                )
            )
            return pictureFuture
        }


        if (imageCaptureUseCase == null) {
            LOG.error("Cannot take picture: no image capture use case")
            pictureFuture.completeExceptionally(
                CameraException(
                    "Cannot take picture: no image capture use case",
                    CameraException.Type.SHOT_FAILED
                )
            )
            return pictureFuture
        }

        imageCaptureUseCase?.takePicture(ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val byteArray = try {
                         image.toCroppedByteArray()
                    } catch (e: CameraException) {
                        LOG.error("Failed to take picture", e)
                        pictureFuture.completeExceptionally(e)
                        return
                    }

                    val photo = PhotoFactory.newPhotoFromJpeg(
                        byteArray,
                        image.imageInfo.rotationDegrees,
                        DeviceHelper.getDeviceOrientation(activity),
                        DeviceHelper.getDeviceType(activity),
                        Document.Source.newCameraSource()
                    )

                    LOG.info("Picture taken")
                    pictureFuture.complete(photo)
                }

                override fun onError(exception: ImageCaptureException) {
                    LOG.error("Failed to take picture", exception)
                    pictureFuture.completeExceptionally(
                        CameraException(
                            exception,
                            CameraException.Type.SHOT_FAILED
                        )
                    )
                }
            })

        return pictureFuture
    }

    override fun getPreviewSize(): Size {
        // TODO: needed for QR code scanning
        return Size(300, 300)
    }

    override fun getPictureSize(): Size {
        // TODO
        return Size(300, 300)
    }

    override fun setPreviewCallback(previewCallback: Camera.PreviewCallback?) {
        // TODO: needed for QR code scanning
    }

    override fun getPreviewView(context: Context): View = previewContainer

    override fun getCameraRotation(): Int {
        // TODO: needed for QR code scanning
        return previewUseCase?.targetRotation ?: 0
    }

    override fun isFlashAvailable(): Boolean {
        return camera?.cameraInfo?.hasFlashUnit() ?: false
    }

    override fun isFlashEnabled(): Boolean {
        // TODO
        return imageCaptureUseCase?.flashMode == ImageCapture.FLASH_MODE_ON
                || imageCaptureUseCase?.flashMode == ImageCapture.FLASH_MODE_AUTO
    }

    override fun setFlashEnabled(enabled: Boolean) {
        imageCaptureUseCase?.flashMode =
            if (enabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
    }
}

internal class CameraLifecycle : LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    fun start() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun stop() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }
}