package reddog0051.com.hangoutsspellchecker;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String TAG = "TRANSLATOR";
    private Context context = this;
    private RecyclerView mTranslatedTextView;
    private ExpandableTranslatorAdapter mAdapter;
    private NotificationReceiver mNotificationReceiver;
    private List<TranslatedItem> mTranslatedItems = new ArrayList<TranslatedItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder ncomp = new NotificationCompat.Builder(getApplicationContext());
                Random r = new Random();
                int i = r.nextInt(100);
                ncomp.setContentTitle("My Notification");
                ncomp.setContentText("This si a tets " + i);
                ncomp.setTicker("This si a tets " + i);
                ncomp.setSmallIcon(R.drawable.ic_notifications_black_24dp);
                nManager.notify((int) System.currentTimeMillis(), ncomp.build());
            }
        });

        mTranslatedTextView = (RecyclerView) findViewById(R.id.translatedText);
        mTranslatedTextView.setHasFixedSize(true);
        mTranslatedTextView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(false);
        mLayoutManager.setReverseLayout(false);
        mTranslatedTextView.setLayoutManager(mLayoutManager);

        mAdapter = new ExpandableTranslatorAdapter(context, mTranslatedItems);
        mTranslatedTextView.setAdapter(mAdapter);

        mNotificationReceiver = new NotificationReceiver();
        registerReceiver(mNotificationReceiver, new IntentFilter("MsgClient"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNotificationReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Check if this app was given permission read notifications
        if (!Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners")
                .contains(getApplicationContext().getPackageName())) {
            //Service is not enabled try to enabled by calling...
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Grant permission to read your notifications?");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getApplicationContext().startActivity(new Intent(
                            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        } else {
            //Retrieve a list of past notifications
            Intent i = new Intent("MsgService");
            i.putExtra("command", "list");
            sendBroadcast(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getStringExtra("command").equals("list")) {
                    int itemCount = mTranslatedItems.size();
                    mTranslatedItems.clear();
                    mAdapter.notifyParentItemRangeRemoved(0, itemCount);
                    // Receive list of untranslated text
                    ArrayList<String> untranslatedText = intent.getStringArrayListExtra("list");
                    for (int i = 0; i < untranslatedText.size(); i++) {
                        // Generate an empty translated parent item and an untranslated child
                        TranslatedItem item = new TranslatedItem(context, "", untranslatedText.get(i), i);
                        mTranslatedItems.add(item);
                    }
                    mAdapter.notifyParentItemRangeInserted(0, mTranslatedItems.size());
                    mTranslatedTextView.scrollToPosition(mTranslatedItems.size() - 1);
                }
                else if (intent.getStringExtra("command").equals("notification")) {
                    String txt = intent.getStringExtra("notification");
                    TranslatedItem item = new TranslatedItem(context, "", txt, mTranslatedItems.size());
                    mTranslatedItems.add(item);
                    mAdapter.notifyParentItemInserted(mTranslatedItems.size() - 1);
                    mTranslatedTextView.scrollToPosition(mTranslatedItems.size() - 1);
                }
                else if (intent.getStringExtra("command").equals("update")){
                    int index = intent.getIntExtra("position", 0);
                    mAdapter.notifyParentItemChanged(index);
                }
            }
        }
    }
}
