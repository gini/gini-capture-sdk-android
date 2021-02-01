package net.gini.android.capture.analysis;

import net.gini.android.capture.Document;
import net.gini.android.capture.GiniCapture;
import net.gini.android.capture.GiniCaptureError;
import net.gini.android.capture.network.GiniCaptureNetworkService;
import net.gini.android.capture.network.model.GiniCaptureSpecificExtraction;
import net.gini.android.capture.noresults.NoResultsFragmentCompat;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * <p>
 * Interface used by {@link AnalysisFragmentCompat} to dispatch events to the hosting Activity.
 * </p>
 */
public interface AnalysisFragmentListener {
    /**
     * <p>
     *     Called when the Analyze Document Fragment is started and the document can be analyzed.
     * </p>
     * @param document contains the image taken by the camera (original or modified)
     *
     * @Deprecated When a {@link GiniCapture} instance is available the document
     * is analyzed internally by using the configured {@link GiniCaptureNetworkService}
     * implementation. The extractions will be returned in {@link AnalysisFragmentListener#onExtractionsAvailable(Map, Map)}.
     */
    @Deprecated
    void onAnalyzeDocument(@NonNull Document document);

    /**
     * <p>
     * Called when an error occurred.
     * </p>
     * @param error details about what went wrong
     */
    void onError(@NonNull GiniCaptureError error);

    /**
     * Called when the document has been analyzed and extractions are available.
     *
     * @param extractions a map of the extractions with the extraction labels as keys
     */
    void onExtractionsAvailable(
            @NonNull final Map<String, GiniCaptureSpecificExtraction> extractions);

    /**
     * Called when the document has been analyzed and no extractions were received.
     * <p>
     * You should show the {@link NoResultsFragmentCompat}.
     *
     * @param document contains the reviewed document
     */
    void onProceedToNoExtractionsScreen(@NonNull final Document document);

    /**
     * Called when the default PDF app alert dialog was cancelled. You should close the
     * AnalysisFragment because the user decided not to continue with analysis.
     * <p>
     * This alert dialog is shown for PDFs imported from another app when your app was set as
     * default for opening PDFs.
     */
    void onDefaultPDFAppAlertDialogCancelled();

}
