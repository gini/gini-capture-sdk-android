package net.gini.android.vision.accounting.network;

import net.gini.android.DocumentTaskManager;
import net.gini.android.vision.GiniCapture;
import net.gini.android.vision.accounting.network.model.SpecificExtractionMapper;
import net.gini.android.vision.internal.camera.api.UIExecutor;
import net.gini.android.vision.network.Error;
import net.gini.android.vision.network.GiniCaptureNetworkApi;
import net.gini.android.vision.network.GiniCaptureNetworkCallback;
import net.gini.android.vision.network.model.GiniCaptureSpecificExtraction;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import androidx.annotation.NonNull;
import bolts.Continuation;
import bolts.Task;

/**
 * Created by Alpar Szotyori on 22.02.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */

/**
 * Default implementation of network calls which can be performed manually from outside the Gini
 * Vision Library (e.g. for sending feedback).
 *
 * <p> To create an instance use the {@link GiniCaptureAccountingNetworkApi.Builder} returned by the
 * {@link #builder()} method.
 *
 * <p> In order to easily access this implementation pass an instance of it to {@link
 * GiniCapture.Builder#setGiniCaptureNetworkApi(GiniCaptureNetworkApi)} when creating a {@link
 * GiniCapture} instance. You can then get the instance in your app with {@link
 * GiniCapture#getGiniCaptureNetworkApi()}.
 */
public class GiniCaptureAccountingNetworkApi implements GiniCaptureNetworkApi {

    private static final Logger LOG = LoggerFactory.getLogger(GiniCaptureAccountingNetworkApi.class);

    private final GiniCaptureAccountingNetworkService mAccountingNetworkService;
    private final UIExecutor mUIExecutor = new UIExecutor();

    /**
     * Creates a new {@link GiniCaptureAccountingNetworkApi.Builder} to configure and create a new
     * instance.
     *
     * @return a new {@link GiniCaptureAccountingNetworkApi.Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    GiniCaptureAccountingNetworkApi(
            @NonNull final GiniCaptureAccountingNetworkService accountingNetworkService) {
        mAccountingNetworkService = accountingNetworkService;
    }

    @Override
    public void sendFeedback(@NonNull final Map<String, GiniCaptureSpecificExtraction> extractions,
            @NonNull final GiniCaptureNetworkCallback<Void, Error> callback) {
        final DocumentTaskManager documentTaskManager = mAccountingNetworkService.getGiniApi()
                .getDocumentTaskManager();
        final net.gini.android.models.Document document =
                mAccountingNetworkService.getAnalyzedGiniApiDocument();
        // We require the Gini API SDK's net.gini.android.models.Document for sending the feedback
        if (document != null) {
            LOG.debug("Send feedback for api document {} using extractions {}", document.getId(),
                    extractions);
            try {
                documentTaskManager.sendFeedbackForExtractions(document,
                        SpecificExtractionMapper.mapToApiSdk(extractions))
                        .continueWith(new Continuation<net.gini.android.models.Document, Object>() {
                            @Override
                            public Object then(
                                    @NonNull final Task<net.gini.android.models.Document> task) {
                                mUIExecutor.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (task.isFaulted()) {
                                            LOG.error(
                                                    "Send feedback failed for api document {}: {}",
                                                    document.getId(), task.getError());
                                            String message = "unknown";
                                            if (task.getError() != null) {
                                                message = task.getError().getMessage();
                                            }
                                            callback.failure(new Error(message));
                                        } else {
                                            LOG.debug("Send feedback success for api document {}",
                                                    document.getId());
                                            callback.success(null);
                                        }
                                    }
                                });
                                return null;
                            }
                        });
            } catch (final JSONException e) {
                LOG.error("Send feedback failed for api document {}: {}", document.getId(), e);
                callback.failure(new Error(e.getMessage()));
            }
        } else {
            LOG.error("Send feedback failed: no Gini Api Document available");
            callback.failure(new Error("Feedback not set: no Gini Api Document available"));
        }
    }

    @Override
    public void deleteGiniUserCredentials() {
        mAccountingNetworkService.getGiniApi().getCredentialsStore().deleteUserCredentials();
    }

    /**
     * Builder for configuring a new instance of the {@link GiniCaptureAccountingNetworkApi}.
     */
    public static class Builder {

        private GiniCaptureAccountingNetworkService mAccountingNetworkService;

        Builder() {
        }

        /**
         * Set the same {@link GiniCaptureAccountingNetworkService} instance you use for {@link
         * GiniCapture}.
         *
         * @param networkService {@link GiniCaptureAccountingNetworkService} instance
         *
         * @return the {@link Builder} instance
         */
        public Builder withGiniCaptureAccountingNetworkService(
                @NonNull final GiniCaptureAccountingNetworkService networkService) {
            mAccountingNetworkService = networkService;
            return this;
        }

        /**
         * Create a new instance of the {@link GiniCaptureAccountingNetworkApi}.
         *
         * @return new {@link GiniCaptureAccountingNetworkApi} instance
         */
        public GiniCaptureAccountingNetworkApi build() {
            if (mAccountingNetworkService == null) {
                throw new IllegalStateException(
                        "GiniCaptureAccountingNetworkApi requires a GiniCaptureAccountingNetworkService instance.");
            }
            return new GiniCaptureAccountingNetworkApi(mAccountingNetworkService);
        }
    }
}
