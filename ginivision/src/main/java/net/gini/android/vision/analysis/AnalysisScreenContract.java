package net.gini.android.vision.analysis;

import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.gini.android.vision.GiniVisionBasePresenter;
import net.gini.android.vision.GiniVisionBaseView;
import net.gini.android.vision.internal.util.Size;

import java.util.List;

import jersey.repackaged.jsr166e.CompletableFuture;

/**
 * Created by Alpar Szotyori on 08.05.2019.
 *
 * Copyright (c) 2019 Gini GmbH.
 */
interface AnalysisScreenContract {

    abstract class View extends GiniVisionBaseView<Presenter> implements
            AnalysisFragmentInterface {

        abstract void showScanAnimation();

        abstract void hideScanAnimation();

        abstract CompletableFuture<Void> waitForViewLayout();

        abstract void showPdfInfoPanel();

        abstract void showPdfTitle(@NonNull final String title);

        abstract void showPdfPageCount(@NonNull final String pageCount);

        abstract void hidePdfPageCount();

        abstract Size getPdfPreviewSize();

        abstract void showBitmap(@Nullable final Bitmap bitmap, final int rotationForDisplay);

        abstract void showAlertDialog(@NonNull final String message,
                @NonNull final String positiveButtonTitle,
                @NonNull final DialogInterface.OnClickListener positiveButtonClickListener,
                @Nullable final String negativeButtonTitle,
                @Nullable final DialogInterface.OnClickListener negativeButtonClickListener,
                @Nullable final DialogInterface.OnCancelListener cancelListener);

        abstract void showErrorSnackbar(@NonNull final String message, final int duration,
                @Nullable final String buttonTitle,
                @Nullable final android.view.View.OnClickListener onClickListener);

        abstract void hideErrorSnackbar();

        abstract void showHints(List<AnalysisHint> hints);
    }

    abstract class Presenter extends GiniVisionBasePresenter<View> implements
            AnalysisFragmentInterface {

        Presenter(@NonNull final Application app,
                @NonNull final View view) {
            super(app, view);
        }

        abstract void finish();
    }
}
