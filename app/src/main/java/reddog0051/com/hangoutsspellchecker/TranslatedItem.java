package reddog0051.com.hangoutsspellchecker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Peter on 1/24/2016.
 */
public class TranslatedItem implements ParentListItem, SpellCheckerSession.SpellCheckerSessionListener {

    private String TAG = "TranslatedItem";
    private Context mContext;
    private String mTranslatedText;
    private List<UntranslatedItem> mUntranslatedItemList;
    private int mPosition;
    private boolean mIsExpanded;
    private SpellCheckerSession mSpellCheckerSession;

    public TranslatedItem(Context context, String translatedText, String untranslatedText, int position) {
        setTranslatedText(translatedText);
        setChildItemList(untranslatedText);
        mPosition = position;
        mContext = context;
        mIsExpanded = false;

        TextServicesManager textServicesManager = (TextServicesManager) mContext.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mSpellCheckerSession = textServicesManager.newSpellCheckerSession(null, Locale.US, this, true);

        translateText();
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setIsExpanded(boolean IsExpanded) {
        mIsExpanded = IsExpanded;
    }

    public String getTranslatedText() {
        return mTranslatedText;
    }

    public void setTranslatedText(String text) {
        mTranslatedText = text;
    }

    @Override
    public List<UntranslatedItem> getChildItemList() {
        return mUntranslatedItemList;
    }

    // Supports only one String now
    public void setChildItemList(String text) {
        UntranslatedItem untranslatedItem = new UntranslatedItem(text);
        mUntranslatedItemList = Arrays.asList(untranslatedItem);
    }

    public void translateText() {
        if (mSpellCheckerSession != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mSpellCheckerSession.getSentenceSuggestions(new TextInfo[]{
                        new TextInfo(getChildItemList().get(0).getUntranslatedText(), 0, 0)}, 5);
            } else {
                //Pre Jelly Bean
                mSpellCheckerSession.getSuggestions(new TextInfo(getChildItemList().get(0).getUntranslatedText()), 5);
            }
        }
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {
        // Pre-Jelly Bean NYI
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        StringBuilder sentence = new StringBuilder("");
        for (SentenceSuggestionsInfo result : results) {
            for (int i = 0; i < result.getSuggestionsCount(); i++) {
                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();
                if (m == 0) {
                    String[] tokenized = getChildItemList().get(0).getUntranslatedText().split("\\s+");
                    if (i < tokenized.length) {
                        sentence.append(tokenized[i]);
                    } else {
                        break;
                    }
                }
                else if (m > 0) {
                    sentence.append(result.getSuggestionsInfoAt(i).getSuggestionAt(0));
                }
                // Append a space between words
                sentence.append(' ');
            }
        }

        setTranslatedText(sentence.toString());

        // Update adapter
        Intent i = new Intent("MsgClient");
        i.putExtra("command", "update");
        i.putExtra("position", mPosition);
        mContext.sendBroadcast(i);
    }
}
