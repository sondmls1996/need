package com.nf.vi.needfoodshipper;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.nf.vi.needfoodshipper.MainClass.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Tung-PC on 20/03/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private String TAG = "Registration";
    public String urln, title, notif, open, idTask, time;
    public String id, name;
    public String idUser;
    Calendar c;
    Intent it;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        if (remoteMessage.getData().size() > 0) {
                Log.e("MESSS", remoteMessage.getData() + "");

            JSONObject jobj = new JSONObject(remoteMessage.getData());
            try {
                if (jobj.has("title")) {
                    title = jobj.getString("title");
                }
                notif = jobj.getString("functionCall");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (notif.equals("saveOrderAjax")) {
                it = new Intent(this, MainActivity.class);
            }
            if(notif.equals("infoOrder")){
                it = new Intent(this,MainActivity.class);
            }

        }
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText("Need Food");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setVibrate(new long[]{0, 500});
        builder.setSound(uri);
        builder.setSmallIcon(R.drawable.logoappc, 20);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("NeedFood - Shipper"));
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(m, builder.build());
        checkNotificationSetting();
        isNLServiceCrashed();
    }

    private boolean checkNotificationSetting() {

        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();

        return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName));
    }

    private boolean isNLServiceCrashed() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = manager.getRunningServices(Integer.MAX_VALUE);

        if (runningServiceInfos != null) {
            for (ActivityManager.RunningServiceInfo service : runningServiceInfos) {

                //NotificationListener.class is the name of my class (the one that has to extend from NotificationListenerService)
                if (FirebaseMessagingService.class.getName().equals(service.service.getClassName())) {

                    if (service.crashCount > 0) {
                        // in this situation we know that the notification listener service is not working for the app
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
}


