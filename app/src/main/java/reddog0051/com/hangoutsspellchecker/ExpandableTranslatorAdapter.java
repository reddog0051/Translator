package reddog0051.com.hangoutsspellchecker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by Peter on 1/24/2016.
 */
public class ExpandableTranslatorAdapter extends ExpandableRecyclerAdapter<TranslatedViewHolder, UntranslatedViewHolder>{

    private LayoutInflater mInflater;

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p/>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public ExpandableTranslatorAdapter(Context context, @NonNull List<ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TranslatedViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflater.inflate(R.layout.translated_text_view, parentViewGroup, false);
        return new TranslatedViewHolder(view);
    }

    @Override
    public UntranslatedViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflater.inflate(R.layout.untranslated_text_view, childViewGroup, false);
        return new UntranslatedViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TranslatedViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        TranslatedItem translatedItem = (TranslatedItem) parentListItem;
        parentViewHolder.mTranslatedTextView.setText(translatedItem.getTranslatedText());
    }

    @Override
    public void onBindChildViewHolder(UntranslatedViewHolder childViewHolder, int position, Object childListItem) {
        UntranslatedItem untranslatedItem = (UntranslatedItem) childListItem;
        childViewHolder.mUntranslatedTextView.setText(untranslatedItem.getUntranslatedText());
    }
}
