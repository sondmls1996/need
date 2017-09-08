package com.needfood.kh.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.needfood.kh.Adapter.ProductDetail.CheckConstructor;
import com.needfood.kh.Database.DataHandle;

import java.util.List;


/**
 * Created by Vi on 6/14/2017.
 */

public class BubbleService extends Service implements View.OnClickListener {
    DataHandle db;
    List<CheckConstructor> arr;

    public static final String ACTION_LOCATION_BROADCAST = BubbleService.class.getName() + "LocationBroadcast",
            INTENTNAME = "itn";

    public BubbleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        arr = db.getPrd();
        int tong = 0;
        for (CheckConstructor lu:arr){
            tong = Integer.parseInt(lu.getPrice())*Integer.parseInt(lu.getQuanli())+tong;
        }
        sendBroadcastMessage(tong);

        return START_STICKY;
    }
    private void sendBroadcastMessage(Integer tong) {
        if (tong != null) {
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(INTENTNAME, tong);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DataHandle(this);

        //Inflate the chat head layout we created

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {


    }


}