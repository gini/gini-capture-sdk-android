package net.gini.android.vision.onboarding;

import static net.gini.android.vision.internal.util.ActivityHelper.enableHomeAsUp;

import android.os.Bundle;
import android.view.MenuItem;

import net.gini.android.vision.GiniCapture;
import net.gini.android.vision.GiniCaptureError;
import net.gini.android.vision.R;
import net.gini.android.vision.analysis.AnalysisActivity;
import net.gini.android.vision.camera.CameraActivity;
import net.gini.android.vision.review.ReviewActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <h3>Screen API</h3>
 *
 * <p>When you use the Screen API, the {@code OnboardingActivity} displays important advice for
 * correctly photographing a document.
 *
 * <p>The {@code OnboardingActivity} is started by the {@link CameraActivity} when the latter is
 * launched for the first time. You may disable this behavior - we highly recommend keeping it - by
 * passing {@code false} to {@link GiniCapture.Builder#setShouldShowOnboardingAtFirstRun(boolean)}
 * when creating a {@link GiniCapture} instance. If you don't use {@link GiniCapture} yet you can also
 * use the extra {@link CameraActivity#EXTRA_IN_SHOW_ONBOARDING_AT_FIRST_RUN} with {@code false}
 * when starting the {@link CameraActivity}.
 *
 * <p> You can change the number of displayed pages and their content (image and short text) by
 * setting an {@code ArrayList} containing {@link OnboardingPage} objects when building a {@link
 * GiniCapture} instance with {@link GiniCapture.Builder#setCustomOnboardingPages(ArrayList)}. If you
 * don't use {@link GiniCapture} yet you can also provide the list using the extra {@link
 * CameraActivity#EXTRA_IN_ONBOARDING_PAGES} for the Screen API and {@link
 * OnboardingFragmentCompat#createInstance(ArrayList)} or {@link OnboardingFragmentStandard#createInstance(ArrayList)}
 * for the Component API.
 *
 * <h3>Customizing the Onboarding Screen</h3>
 *
 * <p> Customizing the look of the Onboarding Screen is done via overriding of app resources or by
 * providing your own pages with your own strings and drawable resources.
 *
 * <p> The following items are customizable:
 *
 * <ul>
 *
 * <li> <b>Next button icon:</b> via images for mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi named {@code
 * gc_onboarding_fab_next.png}
 *
 * <li> <b>Next button color:</b> via the color resources named {@code gc_onboarding_fab} and {@code
 * gc_onboarding_fab_pressed}
 *
 * <li> <b>Page indicators:</b> via images for mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi named {@code
 * gc_onboarding_indicator_active.png} and {@code gc_onboarding_indicator_inactive.png}
 *
 * <li> <b>Onboarding message color:</b> via the color resource named {@code gc_onboarding_message}
 *
 * <li> <b>Onboarding message font:</b> via overriding the style named {@code
 * GiniCaptureTheme.Onboarding.Message.TextStyle} and setting an item named {@code gcCustomFont} with
 * the path to the font file in your {@code assets} folder
 *
 * <li> <b>Onboarding message text style:</b> via overriding the style named {@code
 * GiniCaptureTheme.Onboarding.Message.TextStyle} and setting an item named {@code android:textStyle}
 * to {@code normal}, {@code bold} or {@code italic}
 *
 * <li> <b>Onboarding message text size:</b> via overriding the style named {@code
 * GiniCaptureTheme.Onboarding.Message.TextStyle} and setting an item named {@code android:textSize}
 * to the desired {@code sp} size
 *
 * <li> <b>Tablet Onboarding Pages:</b>
 *
 * <ul>
 *
 * <li> <b>First page image</b> via images for sw600dp-mdpi, sw600dp-hdpi, sw600dp-xhdpi,
 * sw600dp-xxhdpi, sw600dp-xxxhdpi named {@code gc_onboarding_lighting.png}
 *
 * <li> <b>First page text:</b> via the string resource named {@code gc_onboarding_ligthing}
 *
 * <li> <b>Second page image</b> via images for sw600dp-mdpi, sw600dp-hdpi, sw600dp-xhdpi,
 * sw600dp-xxhdpi, sw600dp-xxxhdpi named {@code gc_onboarding_flat.png}
 *
 * <li> <b>Second page text:</b> via the string resource named {@code gc_onboarding_flat}
 *
 * <li> <b>Third page image:</b> via images for sw600dp-mdpi, sw600dp-hdpi, sw600dp-xhdpi,
 * sw600dp-xxhdpi, sw600dp-xxxhdpi named {@code gc_onboarding_parallel.png}
 *
 * <li> <b>Third page text:</b> via the string resource named {@code gc_onboarding_parallel}
 *
 * <li> <b>Fourth page image:</b> via images for sw600dp-mdpi, sw600dp-hdpi, sw600dp-xhdpi,
 * sw600dp-xxhdpi, sw600dp-xxxhdpi named {@code gc_onboarding_align.png}
 *
 * <li> <b>Fourth page text:</b> via the string resource named {@code gc_onboarding_align}
 *
 * </ul>
 *
 * <li> <b>Phone Onboarding Pages:</b>
 *
 * <ul>
 *
 * <li> <b>First page image:</b> via images for mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi named {@code
 * gc_onboarding_flat.png}
 *
 * <li> <b>First page text:</b> via the string resource named {@code gc_onboarding_flat}
 *
 * <li> <b>Second page image:</b> via images for mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi named {@code
 * gc_onboarding_parallel.png}
 *
 * <li> <b>Second page text:</b> via the string resource named {@code gc_onboarding_parallel}
 *
 * <li> <b>Third page image:</b> via images for mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi named {@code
 * gc_onboarding_align.png}
 *
 * <li> <b>Third page text:</b> via the string resource named {@code gc_onboarding_align}
 *
 * </ul>
 *
 * <li> <b>Background transparency:</b> via the string resource named {@code
 * gc_onboarding_page_fragment_background_alpha} which must contain a real number between [0,1].
 *
 * <li> <b>Background color:</b> via the color resource named {@code gc_background}. <b>Note:</b>
 * this color resource is global to all Activities ({@link CameraActivity}, {@link
 * OnboardingActivity}, {@link ReviewActivity}, {@link AnalysisActivity})
 *
 * </ul>
 *
 *
 * <p> <b>Important:</b> All overriden styles must have their respective {@code Root.} prefixed
 * style as their parent. Ex.: the parent of {@code GiniCaptureTheme.Onboarding.Message.TextStyle}
 * must be {@code Root.GiniCaptureTheme.Onboarding.Message.TextStyle}.
 *
 *
 * <h3>Customizing the Action Bar</h3>
 *
 * <p> Customizing the Action Bar is also done via overriding of app resources and each one - except
 * the title string resource - is global to all Activities ({@link CameraActivity}, {@link
 * OnboardingActivity}, {@link ReviewActivity}, {@link AnalysisActivity}).
 *
 * <p> The following items are customizable:
 *
 * <ul>
 *
 * <li> <b>Background color:</b> via the color resource named {@code gc_action_bar} (highly
 * recommended for Android 5+: customize the status bar color via {@code gc_status_bar})
 *
 * <li> <b>Title:</b> via the string resource named {@code gc_title_onboarding}
 *
 * <li> <b>Title color:</b> via the color resource named {@code gc_action_bar_title}
 *
 * <li> <b>Back button:</b> via images for mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi named
 * {@code gc_action_bar_back}
 *
 * </ul>
 */
public class OnboardingActivity extends AppCompatActivity implements OnboardingFragmentListener {

    /**
     * @suppress
     * @Deprecated Configuration should be applied by creating a {@link GiniCapture} instance using
     * {@link GiniCapture#newInstance()} and the returned {@link GiniCapture.Builder}.
     */
    public static final String EXTRA_ONBOARDING_PAGES = "GC_EXTRA_PAGES";

    private static final String ONBOARDING_FRAGMENT = "ONBOARDING_FRAGMENT";

    private ArrayList<OnboardingPage> mPages; // NOPMD - ArrayList required (Bundle)
    private OnboardingFragmentCompat mOnboardingFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gc_activity_onboarding);
        readExtras();
        initFragment();
        setupHomeButton();
    }

    private void setupHomeButton() {
        if (GiniCapture.hasInstance() && GiniCapture.getInstance().areBackButtonsEnabled()) {
            enableHomeAsUp(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readExtras() {
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPages = extras.getParcelableArrayList(EXTRA_ONBOARDING_PAGES);
        }
    }

    private void initFragment() {
        if (!isFragmentShown()) {
            createFragment();
            showFragment();
        }
    }

    private boolean isFragmentShown() {
        return getSupportFragmentManager().findFragmentByTag(ONBOARDING_FRAGMENT) != null;
    }

    private void createFragment() {
        if (mPages != null) {
            mOnboardingFragment = OnboardingFragmentCompat.createInstance(mPages);
            return;
        }
        if (GiniCapture.hasInstance()) {
            final ArrayList<OnboardingPage> onboardingPages =
                    GiniCapture.getInstance().getCustomOnboardingPages();
            if (onboardingPages != null) {
                mOnboardingFragment = OnboardingFragmentCompat.createInstance(
                        onboardingPages);
                return;
            }
        }
        mOnboardingFragment = new OnboardingFragmentCompat();
    }

    private void showFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.gc_fragment_onboarding, mOnboardingFragment, ONBOARDING_FRAGMENT)
                .commit();
    }

    @Override
    public void onCloseOnboarding() {
        finish();
    }

    @Override
    public void onError(@NonNull final GiniCaptureError giniCaptureError) {

    }

    @VisibleForTesting
    void showFragment(@NonNull final OnboardingFragmentCompat onboardingFragment) {
        if (mOnboardingFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mOnboardingFragment)
                    .commit();
        }
        mOnboardingFragment = onboardingFragment;
        showFragment();
    }
}
