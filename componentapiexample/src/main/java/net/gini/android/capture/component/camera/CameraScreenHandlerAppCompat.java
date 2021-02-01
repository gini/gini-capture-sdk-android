package net.gini.android.capture.component.camera;

import android.app.Activity;
import android.content.Intent;

import net.gini.android.capture.Document;
import net.gini.android.capture.camera.CameraFragmentCompat;
import net.gini.android.capture.camera.CameraFragmentInterface;
import net.gini.android.capture.component.R;
import net.gini.android.capture.component.analysis.AnalysisExampleAppCompatActivity;
import net.gini.android.capture.component.review.ReviewExampleAppCompatActivity;
import net.gini.android.capture.onboarding.OnboardingFragmentCompat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * Created by Alpar Szotyori on 04.12.2017.
 *
 * Copyright (c) 2017 Gini GmbH.
 */

/**
 * Creates compatibility library fragments and activities for the Camera Screen.
 */
public class CameraScreenHandlerAppCompat extends BaseCameraScreenHandler {

    private final AppCompatActivity mAppCompatActivity;
    private CameraFragmentCompat mCameraFragment;

    CameraScreenHandlerAppCompat(final Activity activity) {
        super(activity);
        mAppCompatActivity = (AppCompatActivity) activity;
    }

    @Override
    protected void removeOnboardingFragment() {
        final Fragment fragment = mAppCompatActivity.getSupportFragmentManager().findFragmentById(
                R.id.onboarding_container);
        if (fragment != null) {
            mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void setTitlesForCamera() {
        final ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(R.string.camera_screen_title);
        actionBar.setSubtitle(mAppCompatActivity.getString(R.string.camera_screen_subtitle));
    }

    @Override
    protected Intent getReviewActivityIntent(final Document document) {
        return ReviewExampleAppCompatActivity.newInstance(document, mAppCompatActivity);
    }

    @Override
    protected Intent getAnalysisActivityIntent(final Document document) {
        return AnalysisExampleAppCompatActivity.newInstance(document, null, mAppCompatActivity);
    }

    @Override
    protected boolean isOnboardingVisible() {
        return mAppCompatActivity.getSupportFragmentManager().findFragmentById(
                R.id.onboarding_container) != null;
    }

    @Override
    protected void setUpActionBar() {
        mAppCompatActivity.setSupportActionBar(
                (Toolbar) mAppCompatActivity.findViewById(R.id.toolbar));
    }

    @Override
    protected CameraFragmentInterface createCameraFragment() {
        mCameraFragment = CameraFragmentCompat.createInstance();
        return mCameraFragment;
    }

    @Override
    protected void showCameraFragment() {
        mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.camera_container, mCameraFragment)
                .commit();
    }

    @Override
    protected CameraFragmentInterface retrieveCameraFragment() {
        mCameraFragment =
                (CameraFragmentCompat) mAppCompatActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.camera_container);
        return mCameraFragment;
    }

    @Override
    protected void showOnboardingFragment() {
        mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.onboarding_container, new OnboardingFragmentCompat())
                .commit();
    }

    @Override
    protected void setTitlesForOnboarding() {
        final ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle("");
        actionBar.setSubtitle("");
    }
}
