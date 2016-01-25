package reddog0051.com.hangoutsspellchecker;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by Peter on 1/24/2016.
 */
public class UntranslatedViewHolder extends ChildViewHolder {
    public TextView mUntranslatedTextView;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public UntranslatedViewHolder(View itemView) {
        super(itemView);
        mUntranslatedTextView = (TextView) itemView.findViewById(R.id.textView2);
    }
}
