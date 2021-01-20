package net.gini.android.capture.component.camera.standard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.gini.android.capture.Document;
import net.gini.android.capture.GiniCaptureError;
import net.gini.android.capture.camera.CameraFragmentListener;
import net.gini.android.capture.camera.CameraFragmentStandard;
import net.gini.android.capture.component.R;
import net.gini.android.capture.component.analysis.standard.AnalysisExampleActivity;
import net.gini.android.capture.component.review.standard.ReviewExampleActivity;
import net.gini.android.capture.document.GiniCaptureMultiPageDocument;
import net.gini.android.capture.document.QRCodeDocument;
import net.gini.android.capture.help.HelpActivity;
import net.gini.android.capture.network.model.GiniCaptureSpecificExtraction;
import net.gini.android.capture.onboarding.OnboardingFragmentListener;
import net.gini.android.capture.onboarding.OnboardingFragmentStandard;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 04.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

/**
 * Standard Activity using the {@link CameraScreenHandler} to host the
 * {@link CameraFragmentStandard} and the {@link OnboardingFragmentStandard} and to start the
 * {@link ReviewExampleActivity}, the {@link AnalysisExampleActivity} or the {@link HelpActivity}.
 */
public class CameraExampleActivity extends Activity implements CameraFragmentListener,
        OnboardingFragmentListener {

    private CameraScreenHandler mCameraScreenHandler;

    @Override
    public void onCloseOnboarding() {
        mCameraScreenHandler.onCloseOnboarding();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraScreenHandler = new CameraScreenHandler(this);
        mCameraScreenHandler.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        mCameraScreenHandler.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (mCameraScreenHandler.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return mCameraScreenHandler.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mCameraScreenHandler.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraScreenHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDocumentAvailable(@NonNull final Document document) {
        mCameraScreenHandler.onDocumentAvailable(document);
    }

    @Override
    public void onProceedToMultiPageReviewScreen(
            @NonNull final GiniCaptureMultiPageDocument multiPageDocument) {
        mCameraScreenHandler.onProceedToMultiPageReviewScreen(multiPageDocument);
    }

    @Override
    public void onQRCodeAvailable(@NonNull final QRCodeDocument qrCodeDocument) {
        mCameraScreenHandler.onQRCodeAvailable(qrCodeDocument);
    }

    @Override
    public void onCheckImportedDocument(@NonNull final Document document,
            @NonNull final DocumentCheckResultCallback callback) {
        mCameraScreenHandler.onCheckImportedDocument(document, callback);
    }

    @Override
    public void onError(@NonNull final GiniCaptureError error) {
        mCameraScreenHandler.onError(error);
    }

    @Override
    public void onExtractionsAvailable(
            @NonNull final Map<String, GiniCaptureSpecificExtraction> extractions) {
        mCameraScreenHandler.onExtractionsAvailable(extractions);
    }
}
