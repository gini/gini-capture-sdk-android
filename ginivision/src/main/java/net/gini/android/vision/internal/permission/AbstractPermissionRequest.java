package net.gini.android.vision.internal.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * @exclude
 */
abstract class AbstractPermissionRequest<T> implements PermissionRequest<T> {

    private final String mPermission;
    private final int mReqCode;
    private final PermissionRequestListener mListener;

    AbstractPermissionRequest(String permission, int reqCode, final PermissionRequestListener listener) {
        mPermission = permission;
        mReqCode = reqCode;
        mListener = listener;
    }

    @NonNull
    protected String getPermission() {
        return mPermission;
    }

    protected int getReqCode() {
        return mReqCode;
    }

    @NonNull
    protected PermissionRequestListener getListener() {
        return mListener;
    }

    public void requestPermission(@NonNull final T context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mListener.permissionGranted();
        }
        if (checkSelfPermission(context)) {
            mListener.permissionGranted();
            return;
        }

        if (shouldShowRequestRationale(context)) {
            mListener.shouldShowRequestPermissionRationale(
                    new PermissionRequestListener.RationaleResponse() {
                        @Override
                        public void requestPermission() {
                            doRequestPermission(context);
                        }
                    });
        } else {
            doRequestPermission(context);
        }
    }

    protected abstract Boolean checkSelfPermission(@NonNull final T context);

    protected abstract boolean shouldShowRequestRationale(@NonNull final T context);

    protected abstract void doRequestPermission(@NonNull final T context);

    public void onRequestPermissionsResult(@NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mListener.permissionGranted();
        } else {
            mListener.permissionDenied();
        }
    }
}