package net.gini.android.vision.internal.util;

import net.gini.android.vision.DocumentImportEnabledFileTypes;
import net.gini.android.vision.GiniCapture;
import net.gini.android.vision.GiniCaptureFeatureConfiguration;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 05.03.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */

/**
 * Internal use only.
 *
 * @suppress
 */
public final class FeatureConfiguration {

    public static DocumentImportEnabledFileTypes getDocumentImportEnabledFileTypes(
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        return GiniCapture.hasInstance()
                ? GiniCapture.getInstance().getDocumentImportEnabledFileTypes()
                : giniCaptureFeatureConfiguration.getDocumentImportEnabledFileTypes();
    }

    public static boolean isFileImportEnabled(
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        return GiniCapture.hasInstance()
                ? GiniCapture.getInstance().isFileImportEnabled()
                : giniCaptureFeatureConfiguration.isFileImportEnabled();
    }

    public static boolean isQRCodeScanningEnabled(
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        return GiniCapture.hasInstance()
                ? GiniCapture.getInstance().isQRCodeScanningEnabled()
                : giniCaptureFeatureConfiguration.isQRCodeScanningEnabled();
    }

    public static boolean shouldShowOnboardingAtFirstRun(
            final boolean showOnboardingAtFirstRun) {
        return GiniCapture.hasInstance()
                ? GiniCapture.getInstance().shouldShowOnboardingAtFirstRun()
                : showOnboardingAtFirstRun;
    }

    public static boolean shouldShowOnboarding(
            final boolean showOnboarding) {
        return GiniCapture.hasInstance()
                ? GiniCapture.getInstance().shouldShowOnboarding()
                : showOnboarding;
    }

    public static boolean isMultiPageEnabled() {
        return GiniCapture.hasInstance() && GiniCapture.getInstance().isMultiPageEnabled();
    }

    private FeatureConfiguration() {
    }
}
