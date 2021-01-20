package net.gini.android.capture.component.review.compat;

import android.app.Activity;
import android.content.Intent;

import net.gini.android.capture.Document;
import net.gini.android.capture.component.R;
import net.gini.android.capture.component.analysis.compat.AnalysisExampleAppCompatActivity;
import net.gini.android.capture.component.noresults.compat.NoResultsExampleAppCompatActivity;
import net.gini.android.capture.component.review.BaseReviewScreenHandler;
import net.gini.android.capture.review.ReviewFragmentCompat;
import net.gini.android.capture.review.ReviewFragmentInterface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by Alpar Szotyori on 04.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

/**
 * Creates compatibility library fragments and activities for the Review Screen.
 */
public class ReviewScreenHandlerAppCompat extends BaseReviewScreenHandler {

    private final AppCompatActivity mAppCompatActivity;
    private ReviewFragmentCompat mReviewFragment;

    ReviewScreenHandlerAppCompat(final Activity activity) {
        super(activity);
        mAppCompatActivity = (AppCompatActivity) activity;
    }

    @Override
    protected Intent getAnalysisActivityIntent(final Document document, final String errorMessage) {
        return AnalysisExampleAppCompatActivity.newInstance(document, errorMessage,
                mAppCompatActivity);
    }

    @Override
    protected Intent getNoResultsActivityIntent(final Document document) {
        return NoResultsExampleAppCompatActivity.newInstance(document, mAppCompatActivity);
    }

    @Override
    protected ReviewFragmentInterface createReviewFragment() {
        mReviewFragment = ReviewFragmentCompat.createInstance(getDocument());
        return mReviewFragment;
    }

    @Override
    protected void showReviewFragment() {
        mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.review_screen_container, mReviewFragment)
                .commit();
    }

    @Override
    protected ReviewFragmentInterface retrieveReviewFragment() {
        mReviewFragment =
                (ReviewFragmentCompat) mAppCompatActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.review_screen_container);
        return mReviewFragment;
    }

    @Override
    protected void setTitles() {
        final ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(R.string.review_screen_title);
        actionBar.setSubtitle(mAppCompatActivity.getString(R.string.review_screen_subtitle));
    }

    @Override
    protected void setUpActionBar() {
        mAppCompatActivity.setSupportActionBar(
                (Toolbar) mAppCompatActivity.findViewById(R.id.toolbar));
    }
}
