package net.gini.android.capture.help;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.gini.android.capture.GiniCapture;
import net.gini.android.capture.R;

import java.util.ArrayList;
import java.util.List;

import static net.gini.android.capture.internal.util.FeatureConfiguration.isFileImportEnabled;

/**
 * Internal use only.
 *
 * @suppress
 */

class HelpItemsAdapter extends RecyclerView.Adapter<HelpItemsAdapter.HelpItemsViewHolder> {

    private final HelpItemSelectedListener mItemSelectedListener;
    private final List<HelpItem> mItems;

    HelpItemsAdapter(@NonNull final HelpItemSelectedListener itemSelectedListener) {
        mItemSelectedListener = itemSelectedListener;
        mItems = setUpItems();
    }

    @NonNull
    private List<HelpItem> setUpItems() {
        final ArrayList<HelpItem> items = new ArrayList<>();
        items.add(HelpItem.PHOTO_TIPS);
        if (isFileImportEnabled()) {
            items.add(HelpItem.FILE_IMPORT_GUIDE);
        }
        if (GiniCapture.hasInstance()
                && GiniCapture.getInstance().isSupportedFormatsHelpScreenEnabled()) {
            items.add(HelpItem.SUPPORTED_FORMATS);
        }
        return items;
    }

    @NonNull
    List<HelpItem> getItems() {
        return mItems;
    }

    @Override
    public HelpItemsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gc_item_help,
                parent, false);
        return new HelpItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HelpItemsViewHolder holder, final int position) {
        final HelpItem helpItem = mItems.get(position);
        holder.title.setText(helpItem.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int actualPosition = holder.getAdapterPosition();
                mItemSelectedListener.onItemSelected(mItems.get(actualPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HelpItemsViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        HelpItemsViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.gc_help_item_title);
        }
    }

    interface HelpItemSelectedListener {
        void onItemSelected(@NonNull final HelpItem helpItem);
    }
}
