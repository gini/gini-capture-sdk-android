package net.gini.android.vision.internal.pdf;

import static com.google.common.truth.Truth.assertThat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;

import net.gini.android.vision.internal.util.Size;
import net.gini.android.vision.test.Helpers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Alpar Szotyori on 09.08.2018.
 *
 * Copyright (c) 2018 Gini GmbH.
 */

@SdkSuppress(minSdkVersion = 21)
public class RendererLollipopTest {

    private static final String PDF = "invoice.pdf";
    private static final String PDF_WITH_PASSWORD = "invoice-password.pdf";

    private static Uri sPdfContentUri;
    private static Uri sPdfWithPasswordContentUri;


    @BeforeClass
    public static void setUpClass() throws Exception {
        sPdfContentUri = Helpers.getAssetFileFileContentUri(PDF);
        sPdfWithPasswordContentUri = Helpers.getAssetFileFileContentUri(PDF_WITH_PASSWORD);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Helpers.deleteAssetFileFromContentUri(PDF);
        Helpers.deleteAssetFileFromContentUri(PDF_WITH_PASSWORD);
    }

    @Test
    public void should_renderToBitmap_pdfWithoutPassword() throws Exception {
        // Given
        final RendererLollipop renderer = new RendererLollipop(sPdfContentUri,
                InstrumentationRegistry.getTargetContext());
        // When
        final Bitmap bitmap = renderer.toBitmap(new Size(200, 200));
        // Then
        assertThat(bitmap).isNotNull();
    }

    @Test
    public void should_returnNullBitmap_whenRendering_pdfWithPassword() throws Exception {
        // Given
        final RendererLollipop renderer = new RendererLollipop(sPdfWithPasswordContentUri,
                InstrumentationRegistry.getTargetContext());
        // When
        final Bitmap bitmap = renderer.toBitmap(new Size(200, 200));
        // Then
        assertThat(bitmap).isNull();
    }

    @Test
    public void should_getPageCount_forPdfWithoutPassword() throws Exception {
        // Given
        final RendererLollipop renderer = new RendererLollipop(sPdfContentUri,
                InstrumentationRegistry.getTargetContext());
        // When
        final int pageCount = renderer.getPageCount();
        // Then
        assertThat(pageCount).isEqualTo(1);
    }

    @Test
    public void should_returnZeroPageCount_forPdfWithPassword() throws Exception {
        // Given
        final RendererLollipop renderer = new RendererLollipop(sPdfWithPasswordContentUri,
                InstrumentationRegistry.getTargetContext());
        // When
        final int pageCount = renderer.getPageCount();
        // Then
        assertThat(pageCount).isEqualTo(0);
    }

    @Test
    public void should_detectPdfWithPassword() throws Exception {
        // Given
        final RendererLollipop renderer = new RendererLollipop(sPdfWithPasswordContentUri,
                InstrumentationRegistry.getTargetContext());
        // When
        final boolean passwordProtected = renderer.isPdfPasswordProtected();
        // Then
        assertThat(passwordProtected).isTrue();
    }

    @Test
    public void should_detectPdfWithoutPassword() throws Exception {
        // Given
        final RendererLollipop renderer = new RendererLollipop(sPdfContentUri,
                InstrumentationRegistry.getTargetContext());
        // When
        final boolean passwordProtected = renderer.isPdfPasswordProtected();
        // Then
        assertThat(passwordProtected).isFalse();
    }
}