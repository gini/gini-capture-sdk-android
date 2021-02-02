package net.gini.android.capture.help;

import androidx.annotation.StringRes;

import net.gini.android.capture.R;

/**
 * <p>
 *     This enum declares the items which are shown in the Help Screen.
 * </p>
 */
public enum HelpItem {
    /**
     * <p>
     *     Shows tips for taking better pictures.
     * </p>
     * <p>
     *     Item label customizable by overriding the string resource named {@code gc_help_item_photo_tips_title}
     * </p>
     */
    PHOTO_TIPS(R.string.gc_help_item_photo_tips_title),
    /**
     * <p>
     *     Shows a guide for importing files from other apps via "open with".
     * </p>
     * <p>
     *     Item label customizable by overriding the string resource named {@code gc_help_item_file_import_guide_title}
     * </p>
     */
    FILE_IMPORT_GUIDE(R.string.gc_help_item_file_import_guide_title),
    /**
     * <p>
     *     Shows information about the document formats supported by the Gini Capture SDK.
     * </p>
     * <p>
     *     Item label customizable by overriding the string resource named {@code gc_help_item_supported_formats_title}
     * </p>
     */
    SUPPORTED_FORMATS(R.string.gc_help_item_supported_formats_title);

    @StringRes
    final int title;

    HelpItem(@StringRes final int title) {
        this.title = title;
    }
}
