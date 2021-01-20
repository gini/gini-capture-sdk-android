package net.gini.android.capture.review.multipage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import jersey.repackaged.jsr166e.CompletableFuture
import net.gini.android.capture.GiniCapture
import net.gini.android.capture.GiniCaptureHelper
import net.gini.android.capture.document.GiniCaptureDocument
import net.gini.android.capture.document.ImageDocumentFake
import net.gini.android.capture.internal.network.NetworkRequestResult
import net.gini.android.capture.internal.network.NetworkRequestsManager
import net.gini.android.capture.tracking.Event
import net.gini.android.capture.tracking.EventTracker
import net.gini.android.capture.tracking.ReviewScreenEvent
import net.gini.android.capture.tracking.ReviewScreenEvent.UPLOAD_ERROR_DETAILS_MAP_KEY.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any

/**
 * Created by Alpar Szotyori on 02.03.2020.
 *
 * Copyright (c) 2020 Gini GmbH.
 */

@RunWith(AndroidJUnit4::class)
class MultipageReviewFragmentTest {

    @After
    fun after() {
        GiniCaptureHelper.setGiniCaptureInstance(null)
//        GiniVision.cleanup(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun `triggers Next event`() {
        // Given
        val giniCapture = mock<GiniCapture>()
        GiniCaptureHelper.setGiniCaptureInstance(giniCapture)

        val internal = mock<GiniCapture.Internal>()
        `when`(giniCapture.internal()).thenReturn(internal)

        val eventTracker = spy<EventTracker>()
        `when`(giniCapture.internal().eventTracker).thenReturn(eventTracker)

        val fragment = MultiPageReviewFragment()
        fragment.setListener(mock())

        // When
        fragment.onNextButtonClicked()

        // Then
        verify(eventTracker).onReviewScreenEvent(Event(ReviewScreenEvent.NEXT))
    }

    @Test
    fun `triggers Upload Error event`() {
        // Given
        // Note: Use FragmentScenario in the future
        val fragment = mock<MultiPageReviewFragment>()
        fragment.setListener(mock())

        `when`(fragment.activity).thenReturn(mock())
        fragment.mThumbnailsAdapter = mock()
        fragment.mMultiPageDocument = mock()
        fragment.mDocumentUploadResults = mock()

        `when`(fragment.uploadDocument(any())).thenCallRealMethod()

        val exception = RuntimeException("error message")

        val future = CompletableFuture<NetworkRequestResult<GiniCaptureDocument>>()
        future.completeExceptionally(exception)

        val networkRequestsManager = mock<NetworkRequestsManager>()
        `when`(networkRequestsManager.upload(any(), any())).thenReturn(future)

        val internal = mock<GiniCapture.Internal>()
        `when`(internal.networkRequestsManager).thenReturn(networkRequestsManager)

        val giniCapture = mock<GiniCapture>()
        GiniCaptureHelper.setGiniCaptureInstance(giniCapture)

        `when`(giniCapture.internal()).thenReturn(internal)

        val eventTracker = spy<EventTracker>()
        `when`(giniCapture.internal().eventTracker).thenReturn(eventTracker)

        // When
        fragment.uploadDocument(ImageDocumentFake())

        // Then
        val errorDetails = mapOf(
                MESSAGE to exception.message,
                ERROR_OBJECT to exception
        )
        Mockito.verify(eventTracker).onReviewScreenEvent(Event(ReviewScreenEvent.UPLOAD_ERROR, errorDetails))
    }
}