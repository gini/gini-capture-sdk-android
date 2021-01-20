package net.gini.android.vision.accounting.network.model;

import net.gini.android.models.Extraction;
import net.gini.android.vision.network.model.GiniCaptureExtraction;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Alpar Szotyori on 30.01.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */

/**
 * Helper class to map the {@link Extraction} from the Gini API SDK to the Gini Capture SDK's
 * {@link GiniCaptureExtraction} and vice versa.
 */
public final class ExtractionMapper {

    /**
     * Map a list of {@link Extraction}s from the Gini API SDK to a list of Gini Capture SDK
     * {@link GiniCaptureExtraction}s.
     *
     * @param sourceList list of Gini API SDK {@link Extraction}s
     *
     * @return list of Gini Capture SDK {@link GiniCaptureExtraction}s
     */
    @NonNull
    public static List<GiniCaptureExtraction> mapListToGiniCapture(
            @NonNull final List<Extraction> sourceList) {
        final List<GiniCaptureExtraction> targetList = new ArrayList<>(sourceList.size());
        for (final net.gini.android.models.Extraction source : sourceList) {
            targetList.add(map(source));
        }
        return targetList;
    }

    /**
     * Map an {@link Extraction} from the Gini API SDK to the Gini Capture SDK's {@link
     * GiniCaptureExtraction}.
     *
     * @param source Gini API SDK {@link Extraction}
     *
     * @return a Gini Capture SDK {@link GiniCaptureExtraction}
     */
    @NonNull
    public static GiniCaptureExtraction map(
            @NonNull final net.gini.android.models.Extraction source) {
        final GiniCaptureExtraction giniCaptureExtraction = new GiniCaptureExtraction(
                source.getValue(), source.getEntity(),
                BoxMapper.map(source.getBox()));
        giniCaptureExtraction.setIsDirty(source.isDirty());
        return giniCaptureExtraction;
    }

    /**
     * Map a list of {@link GiniCaptureExtraction}s from the Gini Capture SDK to a list of Gini
     * API SDK {@link Extraction}s.
     *
     * @param sourceList list of Gini Capture SDK {@link GiniCaptureExtraction}s
     *
     * @return list of Gini API SDK {@link Extraction}s
     */
    @NonNull
    public static List<Extraction> mapListToApiSdk(
            @NonNull final List<GiniCaptureExtraction> sourceList) {
        final List<Extraction> targetList = new ArrayList<>(sourceList.size());
        for (final GiniCaptureExtraction source : sourceList) {
            targetList.add(map(source));
        }
        return targetList;
    }

    /**
     * Map a {@link GiniCaptureExtraction} from the Gini Capture SDK to the Gini API SDK's {@link
     * Extraction}.
     *
     * @param source Gini Capture SDK {@link GiniCaptureExtraction}
     *
     * @return Gini API SDK {@link Extraction}
     */
    @NonNull
    public static Extraction map(@NonNull final GiniCaptureExtraction source) {
        final Extraction extraction = new Extraction(source.getValue(), source.getEntity(),
                BoxMapper.map(source.getBox()));
        extraction.setIsDirty(source.isDirty());
        return extraction;
    }

    private ExtractionMapper() {
    }
}