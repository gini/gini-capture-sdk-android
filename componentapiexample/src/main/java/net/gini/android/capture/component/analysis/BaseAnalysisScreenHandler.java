package net.gini.android.capture.component.analysis;

import static android.app.Activity.RESULT_OK;

import static net.gini.android.capture.component.analysis.compat.AnalysisExampleAppCompatActivity.EXTRA_IN_DOCUMENT;
import static net.gini.android.capture.component.analysis.compat.AnalysisExampleAppCompatActivity.EXTRA_IN_ERROR_MESSAGE;
import static net.gini.android.capture.example.shared.ExampleUtil.getExtractionsBundle;
import static net.gini.android.capture.example.shared.ExampleUtil.getLegacyExtractionsBundle;
import static net.gini.android.capture.example.shared.ExampleUtil.hasNoPay5Extractions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.gini.android.models.SpecificExtraction;
import net.gini.android.capture.Document;
import net.gini.android.capture.GiniCaptureCoordinator;
import net.gini.android.capture.GiniCaptureDebug;
import net.gini.android.capture.GiniCaptureError;
import net.gini.android.capture.analysis.AnalysisFragmentInterface;
import net.gini.android.capture.analysis.AnalysisFragmentListener;
import net.gini.android.capture.component.ExtractionsActivity;
import net.gini.android.capture.component.R;
import net.gini.android.capture.example.shared.BaseExampleApp;
import net.gini.android.capture.example.shared.DocumentAnalyzer;
import net.gini.android.capture.example.shared.SingleDocumentAnalyzer;
import net.gini.android.capture.network.model.GiniCaptureSpecificExtraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 04.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

/**
 * Contains the logic for the Analysis Screen.
 * <p>
 * Code that differs between the standard and the compatibility library is abstracted away and is
 * implemented in the {@code standard} and {@code compat} packages.
 */
public abstract class BaseAnalysisScreenHandler implements AnalysisFragmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAnalysisScreenHandler.class);

    private final Activity mActivity;
    private AnalysisFragmentInterface mAnalysisFragmentInterface;
    private Document mDocument;
    private String mErrorMessageFromReviewScreen;
    private SingleDocumentAnalyzer mSingleDocumentAnalyzer;

    protected BaseAnalysisScreenHandler(final Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onAnalyzeDocument(@NonNull final Document document) {
        LOG.debug("Analyze document {}", document);
        GiniCaptureDebug.writeDocumentToFile(mActivity, document, "_for_analysis");

        mAnalysisFragmentInterface.startScanAnimation();
        // We can start analyzing the document by sending it to the Gini API
        getSingleDocumentAnalyzer().analyzeDocument(document,
                new DocumentAnalyzer.Listener() {
                    @Override
                    public void onException(final Exception exception) {
                        mAnalysisFragmentInterface.stopScanAnimation();
                        String message = mActivity.getString(R.string.unknown_error);
                        if (exception.getMessage() != null) {
                            message = exception.getMessage();
                        }

                        // Show the error in the Snackbar with a retry button
                        final DocumentAnalyzer.Listener listener = this;
                        mAnalysisFragmentInterface.showError(
                                mActivity.getString(R.string.analysis_failed, message),
                                mActivity.getString(R.string.retry_analysis),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        mAnalysisFragmentInterface.startScanAnimation();
                                        getSingleDocumentAnalyzer().cancelAnalysis();
                                        getSingleDocumentAnalyzer().analyzeDocument(document,
                                                listener);
                                    }
                                });
                        LOG.error("Analysis failed in the Analysis Screen", exception);
                    }

                    @Override
                    public void onExtractionsReceived(
                            final Map<String, SpecificExtraction> extractions) {
                        LOG.debug("Document analyzed in the Analysis Screen");
                        // Calling onDocumentAnalyzed() is important to notify the Analysis
                        // Fragment that the
                        // analysis has completed successfully
                        mAnalysisFragmentInterface.onDocumentAnalyzed();
                        mAnalysisFragmentInterface.stopScanAnimation();
                        // If we have no Pay 5 extractions we query the Gini Capture SDK
                        // whether we should show the Gini Capture No Results Screen
                        if (hasNoPay5Extractions(extractions.keySet())
                                && GiniCaptureCoordinator.shouldShowGiniCaptureNoResultsScreen(
                                        document)) {
                            // Show a special screen, if no Pay5 extractions were found to give the user some
                            // hints and tips
                            // for using the Gini Capture SDK
                            showNoResultsScreen(document);
                        } else {
                            showExtractions(getSingleDocumentAnalyzer().getGiniApiDocument(),
                                    getLegacyExtractionsBundle(extractions));

                        }
                    }
                });
    }

    private SingleDocumentAnalyzer getSingleDocumentAnalyzer() {
        if (mSingleDocumentAnalyzer == null) {
            mSingleDocumentAnalyzer =
                    ((BaseExampleApp) mActivity.getApplication()).getSingleDocumentAnalyzer();
        }
        return mSingleDocumentAnalyzer;
    }

    private void showExtractions(final net.gini.android.models.Document giniApiDocument,
            final Bundle extractionsBundle) {
        LOG.debug("Show extractions");
        final Intent intent = new Intent(mActivity, ExtractionsActivity.class);
        intent.putExtra(ExtractionsActivity.EXTRA_IN_EXTRACTIONS, extractionsBundle);
        mActivity.startActivity(intent);
        mActivity.setResult(RESULT_OK);
        mActivity.finish();
    }

    private void showNoResultsScreen(final Document document) {
        final Intent intent = getNoResultsActivityIntent(document);
        mActivity.startActivity(intent);
        mActivity.setResult(RESULT_OK);
        mActivity.finish();
    }

    protected abstract Intent getNoResultsActivityIntent(final Document document);

    @Override
    public void onError(@NonNull final GiniCaptureError error) {
        mAnalysisFragmentInterface.showError(mActivity.getString(R.string.gini_capture_error,
                error.getErrorCode(), error.getMessage()), Toast.LENGTH_LONG);
    }

    public Activity getActivity() {
        return mActivity;
    }

    public Document getDocument() {
        return mDocument;
    }

    protected String getErrorMessageFromReviewScreen() {
        return mErrorMessageFromReviewScreen;
    }

    public void onCreate(final Bundle savedInstanceState) {
        setUpActionBar();
        setTitles();
        readExtras();

        if (savedInstanceState == null) {
            mAnalysisFragmentInterface = createAnalysisFragment();
            showAnalysisFragment();
        } else {
            mAnalysisFragmentInterface = retrieveAnalysisFragment();
        }
    }

    protected abstract AnalysisFragmentInterface retrieveAnalysisFragment();

    protected abstract void showAnalysisFragment();

    protected abstract AnalysisFragmentInterface createAnalysisFragment();

    private void readExtras() {
        mDocument = mActivity.getIntent().getParcelableExtra(EXTRA_IN_DOCUMENT);
        mErrorMessageFromReviewScreen = mActivity.getIntent().getStringExtra(
                EXTRA_IN_ERROR_MESSAGE);
    }

    protected abstract void setTitles();

    protected abstract void setUpActionBar();

    @Override
    public void onExtractionsAvailable(@NonNull final Map<String, GiniCaptureSpecificExtraction> extractions) {
        showExtractions(null, getExtractionsBundle(extractions));
    }

    @Override
    public void onProceedToNoExtractionsScreen(@NonNull final Document document) {
        showNoResultsScreen(document);
    }

    @Override
    public void onDefaultPDFAppAlertDialogCancelled() {
        mActivity.finish();
    }

}
