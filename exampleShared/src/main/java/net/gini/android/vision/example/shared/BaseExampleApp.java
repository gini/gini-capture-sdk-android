package net.gini.android.vision.example.shared;

import android.text.TextUtils;

import net.gini.android.DocumentMetadata;
import net.gini.android.Gini;
import net.gini.android.GiniApiType;
import net.gini.android.SdkBuilder;
import net.gini.android.vision.accounting.network.GiniCaptureAccountingNetworkApi;
import net.gini.android.vision.accounting.network.GiniCaptureAccountingNetworkService;
import net.gini.android.vision.network.GiniCaptureDefaultNetworkApi;
import net.gini.android.vision.network.GiniCaptureNetworkApi;
import net.gini.android.vision.network.GiniCaptureDefaultNetworkService;
import net.gini.android.vision.network.GiniCaptureNetworkService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

/**
 * <p>
 *     Facilitates the application wide usage of the Gini API SDK's {@link Gini} instance and a helper
 *     {@link SingleDocumentAnalyzer} instance.
 * </p>
 * <p>
 *     The {@link SingleDocumentAnalyzer} is used to analyze documents.
 *     It aids in starting the analysis of the document when the Review Screen starts and continuing it when the
 *     Analysis Screen was shown, if the document wasn't modified. In case it is modified the analysis is cancelled and
 *     only started when the Analysis Screen was shown where the reviewed final document is available.
 * </p>
 */
public abstract class BaseExampleApp extends MultiDexApplication {

    private static final Logger LOG = LoggerFactory.getLogger(BaseExampleApp.class);

    private Gini mGiniApi;
    private SingleDocumentAnalyzer mSingleDocumentAnalyzer;
    private GiniCaptureNetworkService mGiniCaptureNetworkService;
    private GiniCaptureNetworkApi mGiniCaptureNetworkApi;

    public SingleDocumentAnalyzer getSingleDocumentAnalyzer() {
        if (mSingleDocumentAnalyzer == null) {
            mSingleDocumentAnalyzer = new SingleDocumentAnalyzer(getGiniApi());
        }
        return mSingleDocumentAnalyzer;
    }

    protected abstract String getClientId();

    protected abstract String getClientSecret();

    public Gini getGiniApi() {
        if (mGiniApi == null) {
            createGiniApi();
        }
        return mGiniApi;
    }

    private void createGiniApi() {
        final String clientId = getClientId();
        final String clientSecret = getClientSecret();
        if (TextUtils.isEmpty(clientId) || TextUtils.isEmpty(clientSecret)) {
            LOG.warn(
                    "Missing Gini API client credentials. Either create a local.properties file "
                            + "with clientId and clientSecret properties or pass them in as gradle "
                            + "parameters with -PclientId and -PclientSecret.");
        }
        final SdkBuilder builder = new SdkBuilder(this,
                clientId,
                clientSecret,
                "example.com");
        mGiniApi = builder.build();
    }

    @NonNull
    public GiniCaptureNetworkService getGiniCaptureNetworkService(@NonNull final String appFlowApiType,
                                                                  @NonNull final GiniApiType giniApiType) {
        final String clientId = getClientId();
        final String clientSecret = getClientSecret();
        if (TextUtils.isEmpty(clientId) || TextUtils.isEmpty(clientSecret)) {
            LOG.warn(
                    "Missing Gini API client credentials. Either create a local.properties file "
                            + "with clientId and clientSecret properties or pass them in as gradle "
                            + "parameters with -PclientId and -PclientSecret.");
        }
        if (mGiniCaptureNetworkService == null) {
            final DocumentMetadata documentMetadata = new DocumentMetadata();
            documentMetadata.setBranchId("GCSExampleAndroid");
            documentMetadata.add("AppFlow", appFlowApiType);
            switch (giniApiType) {
                case DEFAULT:
                    mGiniCaptureNetworkService = GiniCaptureDefaultNetworkService.builder(this)
                            .setClientCredentials(clientId, clientSecret, "example.com")
                            .setDocumentMetadata(documentMetadata)
                            .build();
                    break;
                case ACCOUNTING:
                    mGiniCaptureNetworkService = GiniCaptureAccountingNetworkService.builder(this)
                            .setClientCredentials(clientId, clientSecret, "example.com")
                            .setDocumentMetadata(documentMetadata)
                            .build();
                    break;
                    default:
                        throw new UnsupportedOperationException("Unknown Gini API type: " + giniApiType);
            }
        }
        return mGiniCaptureNetworkService;
    }

    @NonNull
    public GiniCaptureNetworkApi getGiniCaptureNetworkApi() {
        if (mGiniCaptureNetworkApi == null) {
            if (mGiniCaptureNetworkService instanceof GiniCaptureDefaultNetworkService) {
                mGiniCaptureNetworkApi = GiniCaptureDefaultNetworkApi.builder()
                        .withGiniCaptureDefaultNetworkService(
                                (GiniCaptureDefaultNetworkService) mGiniCaptureNetworkService)
                        .build();
            } else if (mGiniCaptureNetworkService instanceof GiniCaptureAccountingNetworkService) {
                mGiniCaptureNetworkApi = GiniCaptureAccountingNetworkApi.builder()
                        .withGiniCaptureAccountingNetworkService(
                                (GiniCaptureAccountingNetworkService) mGiniCaptureNetworkService)
                        .build();
            } else {
                throw new UnsupportedOperationException("No network api class for "
                        + mGiniCaptureNetworkService.getClass().getName());
            }
        }
        return mGiniCaptureNetworkApi;
    }

    public void clearGiniCaptureNetworkInstances() {
        if (mGiniCaptureNetworkService != null) {
            mGiniCaptureNetworkService.cleanup();
            mGiniCaptureNetworkService = null;
        }
        if (mGiniCaptureNetworkApi != null) {
            mGiniCaptureNetworkApi = null;
        }
    }
}