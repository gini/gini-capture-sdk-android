package net.gini.android.capture.component.review;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import net.gini.android.capture.Document;
import net.gini.android.capture.GiniCaptureError;
import net.gini.android.capture.component.R;
import net.gini.android.capture.component.analysis.AnalysisExampleAppCompatActivity;
import net.gini.android.capture.review.ReviewFragmentCompat;
import net.gini.android.capture.review.ReviewFragmentInterface;
import net.gini.android.capture.review.ReviewFragmentListener;

import static android.app.Activity.RESULT_OK;
import static net.gini.android.capture.component.review.ReviewExampleAppCompatActivity.EXTRA_IN_DOCUMENT;

/**
 * Created by Alpar Szotyori on 04.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

/**
 * Contains the logic for the Review Screen.
 */
public class ReviewScreenHandler implements ReviewFragmentListener {

    private static final int ANALYSIS_REQUEST = 1;
    private final AppCompatActivity mActivity;
    private Document mDocument;
    private ReviewFragmentInterface mReviewFragmentInterface;

    protected ReviewScreenHandler(final AppCompatActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onProceedToAnalysisScreen(@NonNull final Document document,
            @Nullable final String errorMessage) {
        final Intent intent = getAnalysisActivityIntent(document, errorMessage);
        mActivity.startActivityForResult(intent, ANALYSIS_REQUEST);
    }

    private Intent getAnalysisActivityIntent(final Document document, final String errorMessage) {
        return AnalysisExampleAppCompatActivity.newInstance(document, errorMessage, mActivity);
    }

    @Override
    public void onError(@NonNull final GiniCaptureError error) {
        Toast.makeText(mActivity, mActivity.getString(R.string.gini_capture_error,
                error.getErrorCode(), error.getMessage()), Toast.LENGTH_LONG).show();
    }

    public Document getDocument() {
        return mDocument;
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case ANALYSIS_REQUEST:
                if (resultCode == RESULT_OK) {
                    mActivity.setResult(RESULT_OK);
                    mActivity.finish();
                }
                break;
        }
    }

    public void onCreate(final Bundle savedInstanceState) {
        setUpActionBar();
        setTitles();
        readDocumentFromExtras();

        if (savedInstanceState == null) {
            createReviewFragment();
            showReviewFragment();
        } else {
            retrieveReviewFragment();
        }
    }

    private void readDocumentFromExtras() {
        mDocument = mActivity.getIntent().getParcelableExtra(EXTRA_IN_DOCUMENT);
    }

    private ReviewFragmentInterface createReviewFragment() {
        mReviewFragmentInterface = ReviewFragmentCompat.createInstance(getDocument());
        return mReviewFragmentInterface;
    }

    private void showReviewFragment() {
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.review_screen_container, (Fragment) mReviewFragmentInterface)
                .commit();
    }

    private ReviewFragmentInterface retrieveReviewFragment() {
        mReviewFragmentInterface =
                (ReviewFragmentCompat) mActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.review_screen_container);
        return mReviewFragmentInterface;
    }

    private void setTitles() {
        final ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(R.string.review_screen_title);
        actionBar.setSubtitle(mActivity.getString(R.string.review_screen_subtitle));
    }

    private void setUpActionBar() {
        mActivity.setSupportActionBar(
                (Toolbar) mActivity.findViewById(R.id.toolbar));
    }
}
