package net.gini.android.capture.component.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.gini.android.capture.Document;
import net.gini.android.capture.GiniCaptureError;
import net.gini.android.capture.camera.CameraFragmentCompat;
import net.gini.android.capture.camera.CameraFragmentListener;
import net.gini.android.capture.component.R;
import net.gini.android.capture.component.analysis.AnalysisExampleAppCompatActivity;
import net.gini.android.capture.component.review.ReviewExampleAppCompatActivity;
import net.gini.android.capture.document.GiniCaptureMultiPageDocument;
import net.gini.android.capture.help.HelpActivity;
import net.gini.android.capture.network.model.GiniCaptureSpecificExtraction;
import net.gini.android.capture.onboarding.OnboardingFragmentCompat;
import net.gini.android.capture.onboarding.OnboardingFragmentListener;

import java.util.Map;

/**
 * Created by Alpar Szotyori on 04.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

/**
 * AppCompatActivity using the {@link CameraScreenHandler} to host the
 * {@link CameraFragmentCompat} and the {@link OnboardingFragmentCompat} and to start the
 * {@link ReviewExampleAppCompatActivity}, the {@link AnalysisExampleAppCompatActivity} or the {@link HelpActivity}.
 */
public class CameraExampleAppCompatActivity extends AppCompatActivity implements
        CameraFragmentListener,
        OnboardingFragmentListener {

    private CameraScreenHandler mCameraScreenHandler;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraScreenHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mCameraScreenHandler.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        mCameraScreenHandler.onNewIntent(intent);
    }

    @Override
    public void onCloseOnboarding() {
        mCameraScreenHandler.onCloseOnboarding();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_compat);
        mCameraScreenHandler = new CameraScreenHandler(this);
        mCameraScreenHandler.onCreate(savedInstanceState);
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
    public void onDocumentAvailable(@NonNull final Document document) {
        mCameraScreenHandler.onDocumentAvailable(document);
    }

    @Override
    public void onProceedToMultiPageReviewScreen(
            @NonNull final GiniCaptureMultiPageDocument multiPageDocument) {
        mCameraScreenHandler.onProceedToMultiPageReviewScreen(multiPageDocument);
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
