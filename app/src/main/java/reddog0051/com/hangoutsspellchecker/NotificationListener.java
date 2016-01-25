package reddog0051.com.hangoutsspellchecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;

public class NotificationListener extends NotificationListenerService {

    private static String TAG = "TRANSLATOR - SERVICE";
    private static String mPackageName = "com.google.android.talk";
    private static int SBNMAXSIZE = 20;
    private ServiceReceiver mServiceReceiver;
    private ArrayList<String> mTexts = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.i(TAG, "onCreate");
        mServiceReceiver = new ServiceReceiver();
        registerReceiver(mServiceReceiver, new IntentFilter("MsgService"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mServiceReceiver);
    }

    // Send all notifications
    private void sendNotificationList() {
        //Log.i(TAG, "sendNotificationList");
        Intent i = new Intent("MsgClient");
        i.putExtra("command", "list");
        i.putStringArrayListExtra("list", mTexts);
        sendBroadcast(i);
    }

    // Send the latest notification
    private void sendNotification(){
        //Log.i(TAG, "sendNotification " + mTexts.get(mTexts.size() - 1));
        Intent i = new Intent("MsgClient");
        i.putExtra("command", "notification");
        i.putExtra("notification", mTexts.get(mTexts.size() - 1));
        sendBroadcast(i);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //Log.i(TAG, "onNotificationPosted");
        String pack = sbn.getPackageName();
        if (pack.equals(mPackageName) || pack.equals(getPackageName())) {
            //String ticker = sbn.getNotification().tickerText.toString();
            //Bundle extras = sbn.getNotification().extras;
            //String title = extras.getString("android.title");
            //String text = extras.getCharSequence("android.text").toString();
            //Log.i(TAG , "BUNDLE" + extras.describeContents());
            //Log.i(TAG, "PACK: " + pack);
            //Log.i(TAG, "TICKER: " + ticker);
            //Log.i(TAG, "TITLE: " + title);
            //Log.i(TAG, "TEXT: " + text);

            //FILO
            if (mTexts.size() >= SBNMAXSIZE)
                mTexts.remove(0);

            mTexts.add(sbn.getNotification().tickerText.toString());

            sendNotification();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //Log.i(TAG, "onNotificationRemoved");
    }

    private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.i(TAG, "onReceive");
            if (intent != null) {
                if (intent.getStringExtra("command").equals("list")) {
                    if (!mTexts.isEmpty()) {
                        sendNotificationList();
                    }
                }
            }
        }
    }
}
