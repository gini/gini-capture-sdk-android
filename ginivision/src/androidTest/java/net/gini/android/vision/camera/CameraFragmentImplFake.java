package net.gini.android.vision.camera;

import android.app.Activity;
import android.support.annotation.NonNull;

import net.gini.android.vision.GiniVisionFeatureConfiguration;
import net.gini.android.vision.document.QRCodeDocument;
import net.gini.android.vision.internal.camera.api.CameraControllerFake;
import net.gini.android.vision.internal.camera.api.CameraInterface;
import net.gini.android.vision.internal.ui.FragmentImplCallback;

/**
 * Created by Alpar Szotyori on 15.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

public class CameraFragmentImplFake extends CameraFragmentImpl {

    private CameraControllerFake mCameraControllerFake;
    private int mHidePaymentDataDetectedPopupDelayMs = 10000;
    private QRCodeDocument mQRCodeDocument;

    CameraFragmentImplFake(
            @NonNull final FragmentImplCallback fragment) {
        super(fragment);
    }

    CameraFragmentImplFake(
            @NonNull final FragmentImplCallback fragment,
            @NonNull final GiniVisionFeatureConfiguration giniVisionFeatureConfiguration) {
        super(fragment, giniVisionFeatureConfiguration);
    }

    @Override
    long getHideQRCodeDetectedPopupDelayMs() {
        return CameraFragmentImpl.DEFAULT_ANIMATION_DURATION + mHidePaymentDataDetectedPopupDelayMs;
    }

    @Override
    long getDifferentQRCodeDetectedPopupDelayMs() {
        return 100;
    }

    @Override
    void analyzeQRCode(final QRCodeDocument qrCodeDocument) {
        mQRCodeDocument = qrCodeDocument;
        super.analyzeQRCode(qrCodeDocument);
    }

    @NonNull
    @Override
    protected CameraInterface createCameraController(final Activity activity) {
        return mCameraControllerFake = new CameraControllerFake();
    }

    public QRCodeDocument getQRCodeDocument() {
        return mQRCodeDocument;
    }

    void setHidePaymentDataDetectedPopupDelayMs(
            final int hidePaymentDataDetectedPopupDelayMs) {
        mHidePaymentDataDetectedPopupDelayMs = hidePaymentDataDetectedPopupDelayMs;
    }

    CameraControllerFake getCameraControllerFake() {
        return mCameraControllerFake;
    }
}
