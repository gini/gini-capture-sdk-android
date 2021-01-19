package net.gini.android.vision.camera;

import net.gini.android.vision.GiniCaptureFeatureConfiguration;
import net.gini.android.vision.document.QRCodeDocument;
import net.gini.android.vision.internal.camera.api.CameraControllerFake;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 15.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class CameraActivityFake extends CameraActivity {

    private CameraFragmentCompatFake mCameraFragmentCompatFake;
    private QRCodeDocument mQRCodeDocument;

    @Override
    protected CameraFragmentCompat createCameraFragmentCompat() {
        return mCameraFragmentCompatFake = CameraFragmentCompatFake.createInstance();
    }

    @Override
    protected CameraFragmentCompat createCameraFragmentCompat(
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        return mCameraFragmentCompatFake = CameraFragmentCompatFake.createInstance(
                giniCaptureFeatureConfiguration);
    }

    @Override
    public void onQRCodeAvailable(@NonNull final QRCodeDocument qrCodeDocument) {
        mQRCodeDocument = qrCodeDocument;
    }

    public CameraControllerFake getCameraControllerFake() {
        return mCameraFragmentCompatFake.getCameraControllerFake();
    }

    public CameraFragmentImplFake getCameraFragmentImplFake() {
        return mCameraFragmentCompatFake.getCameraFragmentImplFake();
    }

    public QRCodeDocument getQRCodeDocument() {
        return mQRCodeDocument;
    }
}
