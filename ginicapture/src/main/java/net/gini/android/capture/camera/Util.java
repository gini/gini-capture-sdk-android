package net.gini.android.capture.camera;

import net.gini.android.capture.GiniCaptureError;

import androidx.annotation.NonNull;

/**
 * Internal use only.
 *
 * @suppress
 */
final class Util {

    private static final String CAMERA_EXCEPTION_MESSAGE_NO_ACCESS =
            "Fail to connect to camera service";

    @NonNull
    static GiniCaptureError cameraExceptionToGiniCaptureError(@NonNull final Exception exception) {
        // String comparison is the only way to determine the cause of the camera exception with the old Camera API
        // Here are the possible error messages:
        // https://android.googlesource.com/platform/frameworks/base/+/marshmallow-release/core/java/android/hardware/Camera.java#415
        final String message = exception.getMessage();
        if (message.equals(CAMERA_EXCEPTION_MESSAGE_NO_ACCESS)) {
            return new GiniCaptureError(GiniCaptureError.ErrorCode.CAMERA_NO_ACCESS, message);
        } else {
            return new GiniCaptureError(GiniCaptureError.ErrorCode.CAMERA_UNKNOWN, message);
        }
    }

    private Util() {
    }
}
