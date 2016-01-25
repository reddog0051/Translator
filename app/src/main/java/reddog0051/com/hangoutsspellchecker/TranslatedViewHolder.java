package reddog0051.com.hangoutsspellchecker;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by Peter on 1/24/2016.
 */
public class TranslatedViewHolder extends ParentViewHolder {
    public TextView mTranslatedTextView;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public TranslatedViewHolder(View itemView) {
        super(itemView);
        mTranslatedTextView = (TextView) itemView.findViewById(R.id.textView);
    }
}
