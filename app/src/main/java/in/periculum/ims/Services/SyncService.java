package in.periculum.ims.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.periculum.ims.Dashboard;
import in.periculum.ims.MpinLogin;
import in.periculum.ims.R;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Material;
import in.periculum.ims.ds.Sites;
import in.periculum.ims.ds.Stock;
import in.periculum.ims.ds.StockRcv;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;

/**
 * Created by ROYAL on 11/5/2015.
 */
public class SyncService extends Service {


    public SyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent resultIntent = new Intent(this, Dashboard.class);
        resultIntent.putExtra("intntdata",intent.getStringExtra("intntdata"));
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Sets an ID for the notification, so it can be updated
        int notifyID = 9001;
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Alert")
                .setContentText("Database OutOfSync!")
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText("You need to Sync Database");
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());

        if(!ImsUtility.getIsSyncing(getApplicationContext())) {
            startActivity(resultIntent);
        }
        return START_STICKY;
    }

}