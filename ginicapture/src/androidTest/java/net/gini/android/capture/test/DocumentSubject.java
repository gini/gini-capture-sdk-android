package net.gini.android.capture.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.truth.FailureStrategy;
import com.google.common.truth.Subject;
import com.google.common.truth.SubjectFactory;

import net.gini.android.capture.Document;
import net.gini.android.capture.document.ImageDocument;
import net.gini.android.capture.internal.camera.photo.JpegByteArraySubject;

import androidx.annotation.Nullable;

public class DocumentSubject extends Subject<DocumentSubject, Document> {

    private final JpegByteArraySubject mJpegByteArraySubject;

    public static SubjectFactory<DocumentSubject, Document> document() {
        return new SubjectFactory<DocumentSubject, Document>() {

            @Override
            public DocumentSubject getSubject(final FailureStrategy fs, final Document that) {
                return new DocumentSubject(fs, that);
            }
        };
    }

    private DocumentSubject(final FailureStrategy failureStrategy,
            @Nullable final Document subject) {
        super(failureStrategy, subject);
        isNotNull();
        //noinspection ConstantConditions
        mJpegByteArraySubject = new JpegByteArraySubject(failureStrategy, subject.getData());
    }

    public void isEqualToDocument(final Document other) {
        final Document document = getSubject();
        if (document == null) {
            fail("is equal to another Document - subject is null");
        }
        if (other == null) {
            fail("is equal to another Document - comparing to null");
        }

        //noinspection ConstantConditions - null check done above
        final Bitmap bitmap = BitmapFactory.decodeByteArray(document.getData(), 0,
                document.getData().length);
        //noinspection ConstantConditions - null check done above
        final Bitmap otherBitmap = BitmapFactory.decodeByteArray(other.getData(), 0,
                other.getData().length);

        if (!bitmap.sameAs(otherBitmap)) {
            fail("is equal to Document " + other + " - contain different bitmaps");
        } else {
            if (document instanceof ImageDocument && other instanceof ImageDocument) {
                if (((ImageDocument) document).getRotationForDisplay() != ((ImageDocument) other).getRotationForDisplay()) {
                    fail("is equal to Document " + other + " - have different rotation");
                }
            }
        }
    }

    public void hasSameContentIdInUserCommentAs(final Document other) {
        isNotNull();
        final String verb = "has same User Comment ContentId";

        if (other == null) {
            fail(verb, (Object) null);
            return;
        }

        mJpegByteArraySubject.hasSameContentIdInUserCommentAs(other.getData());
    }

    public void hasRotationDeltaInUserComment(final int rotationDelta) {
        isNotNull();
        mJpegByteArraySubject.hasRotationDeltaInUserComment(rotationDelta);
    }
}
