package net.gini.android.capture.camera;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gini.android.capture.GiniCaptureFeatureConfiguration;
import net.gini.android.capture.internal.ui.FragmentImplCallback;
import net.gini.android.capture.internal.util.AlertDialogHelperStandard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <h3>Component API</h3>
 *
 * <p>
 *     {@code CameraFragmentStandard} is the main entry point to the Gini Capture SDK when using the Component API without the Android Support Library.
 * </p>
 * <p>
 *     It shows a camera preview with tap-to-focus functionality, a trigger button and an optional flash on/off button. The camera preview also shows document corner guides to which the user should align the document.
 * </p>
 * <p>
 *     If instantiated with {@link CameraFragmentStandard#createInstance(GiniCaptureFeatureConfiguration)} then a button for importing documents is shown next to the trigger button. A hint popup is displayed the first time the Gini Capture SDK is used to inform the user about document importing.
 * </p>
 * <p>
 *     For importing documents {@code READ_EXTERNAL_STORAGE} permission is required and if the permission is not granted the Gini Capture SDK will prompt the user to grant the permission. See @{code Customizing the Camera Screen} on how to override the message and button titles for the rationale and on permission denial alerts.
 * </p>
 * <p>
 *     Include the {@code CameraFragmentStandard} into your layout either directly with {@code <fragment>} in your Activity's layout or using the {@link android.app.FragmentManager} and one of the {@code createInstance()} methods.
 * </p>
 * <p>
 *     A {@link CameraFragmentListener} instance must be available until the {@code CameraFragmentStandard} is attached to an activity. Failing to do so will throw an exception.
 *     The listener instance can be provided either implicitly by making the hosting Activity implement the {@link CameraFragmentListener} interface or explicitly by
 *     setting the listener using {@link CameraFragmentCompat#setListener(CameraFragmentListener)}.
 * </p>
 * <p>
 *     Your Activity is automatically set as the listener in {@link CameraFragmentStandard#onAttach(Context)}.
 * </p>
 *
 * <h3>Customizing the Camera Screen</h3>
 *
 * <p>
 *     See the {@link CameraActivity} for details.
 * </p>
 */
public class CameraFragmentStandard extends Fragment implements CameraFragmentInterface,
        FragmentImplCallback {

    private CameraFragmentListener mListener;

    public static CameraFragmentStandard createInstance() {
        return new CameraFragmentStandard();
    }

    /**
     * <p>
     *     Factory method for creating a new instance of the Fragment with document import enabled for the specified file types.
     * </p>
     * @param giniCaptureFeatureConfiguration feature configuration
     * @return a new instance of the Fragment
     */
    public static CameraFragmentStandard createInstance(
            @NonNull final GiniCaptureFeatureConfiguration giniCaptureFeatureConfiguration) {
        final CameraFragmentStandard fragment = new CameraFragmentStandard();
        fragment.setArguments(
                CameraFragmentHelper.createArguments(giniCaptureFeatureConfiguration));
        return fragment;
    }

    private CameraFragmentImpl mFragmentImpl;

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mFragmentImpl = new CameraFragmentHelper().createFragmentImpl(this, getArguments());
        CameraFragmentHelper.setListener(mFragmentImpl, context, mListener);
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        mFragmentImpl = new CameraFragmentHelper().createFragmentImpl(this, getArguments());
        CameraFragmentHelper.setListener(mFragmentImpl, activity, mListener);
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentImpl.onCreate(savedInstanceState);
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return mFragmentImpl.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onStart() {
        super.onStart();
        mFragmentImpl.onStart();
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onResume() {
        super.onResume();
        mFragmentImpl.onResume();
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onStop() {
        super.onStop();
        mFragmentImpl.onStop();
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentImpl.onSaveInstanceState(outState);
    }

    /**
     * Internal use only.
     *
     * @suppress
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentImpl.onDestroy();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        final boolean handled = mFragmentImpl.onActivityResult(requestCode, resultCode, data);
        if (!handled) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setListener(@NonNull final CameraFragmentListener listener) {
        if (mFragmentImpl != null) {
            mFragmentImpl.setListener(listener);
        }
        mListener = listener;
    }

    @Deprecated
    @Override
    public void showDocumentCornerGuides() {
        if (mFragmentImpl == null) {
            return;
        }
        mFragmentImpl.showDocumentCornerGuides();
    }

    @Deprecated
    @Override
    public void hideDocumentCornerGuides() {
        if (mFragmentImpl == null) {
            return;
        }
        mFragmentImpl.hideDocumentCornerGuides();
    }

    @Deprecated
    @Override
    public void showCameraTriggerButton() {
        if (mFragmentImpl == null) {
            return;
        }
        mFragmentImpl.showCameraTriggerButton();
    }

    @Deprecated
    @Override
    public void hideCameraTriggerButton() {
        if (mFragmentImpl == null) {
            return;
        }
        mFragmentImpl.hideCameraTriggerButton();
    }

    @Override
    public void showInterface() {
        if (mFragmentImpl == null) {
            return;
        }
        mFragmentImpl.showInterface();
    }

    @Override
    public void hideInterface() {
        if (mFragmentImpl == null) {
            return;
        }
        mFragmentImpl.hideInterface();
    }

    @Override
    public void showActivityIndicatorAndDisableInteraction() {
        mFragmentImpl.showActivityIndicatorAndDisableInteraction();
    }

    @Override
    public void hideActivityIndicatorAndEnableInteraction() {
        mFragmentImpl.hideActivityIndicatorAndEnableInteraction();
    }

    @Override
    public void showError(@NonNull final String message, final int duration) {
        mFragmentImpl.showError(message, duration);
    }

    @Override
    public void showAlertDialog(@NonNull final String message,
            @NonNull final String positiveButtonTitle,
            @NonNull final DialogInterface.OnClickListener positiveButtonClickListener,
            @Nullable final String negativeButtonTitle,
            @Nullable final DialogInterface.OnClickListener negativeButtonClickListener,
            @Nullable final DialogInterface.OnCancelListener cancelListener) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        AlertDialogHelperStandard.showAlertDialog(activity, message, positiveButtonTitle,
                positiveButtonClickListener, negativeButtonTitle, negativeButtonClickListener,
                cancelListener);
    }
}
