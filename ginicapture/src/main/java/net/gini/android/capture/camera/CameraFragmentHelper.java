package net.gini.android.capture.camera;

import android.content.Context;
import android.os.Bundle;

import net.gini.android.capture.GiniCaptureFeatureConfiguration;
import net.gini.android.capture.internal.ui.FragmentImplCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class CameraFragmentHelper {

    private static final String ARGS_GINI_CAPTURE_FEATURES = "GC_ARGS_GINI_CAPTURE_FEATURES";

    public static Bundle createArguments(
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARGS_GINI_CAPTURE_FEATURES, giniCaptureFeatureConfiguration);
        return arguments;
    }

    @NonNull
    CameraFragmentImpl createFragmentImpl(@NonNull final FragmentImplCallback fragment,
            @Nullable final Bundle arguments) {
        if (arguments != null) {
            final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration =
                    arguments.getParcelable(ARGS_GINI_CAPTURE_FEATURES);
            if (giniCaptureFeatureConfiguration != null) {
                return createCameraFragment(fragment, giniCaptureFeatureConfiguration);
            }
        }
        return createCameraFragment(fragment);
    }

    @NonNull
    protected CameraFragmentImpl createCameraFragment(
            @NonNull final FragmentImplCallback fragment,
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        return new CameraFragmentImpl(fragment, giniCaptureFeatureConfiguration);
    }

    @NonNull
    protected CameraFragmentImpl createCameraFragment(
            @NonNull final FragmentImplCallback fragment) {
        return new CameraFragmentImpl(fragment);
    }

    public static void setListener(@NonNull final CameraFragmentImpl fragmentImpl,
            @NonNull final Context context, @Nullable final CameraFragmentListener listener) {
        if (context instanceof CameraFragmentListener) {
            fragmentImpl.setListener((CameraFragmentListener) context);
        } else if (listener != null) {
            fragmentImpl.setListener(listener);
        } else {
            throw new IllegalStateException(
                    "CameraFragmentListener not set. "
                            + "You can set it with CameraFragmentCompat#setListener() or "
                            + "by making the host activity implement the CameraFragmentListener.");
        }
    }
}
