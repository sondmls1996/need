package com.needfood.kh;

import android.*;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.SupportClass.GPSTracker;
import com.needfood.kh.SupportClass.GetCL;
import com.needfood.kh.SupportClass.NetworkCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.needfood.kh.R.menu.main;

public class WellcomeActivity extends AppCompatActivity {
    NetworkCheck networkCheck;
    DataHandle db;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    GPSTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        db = new DataHandle(this);
        tracker = new GPSTracker(this);
        insertDummyContactWrapper();
        networkCheck = new NetworkCheck();
        Boolean conn = networkCheck.checkNow(getApplicationContext());
        if (conn == false) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        }


    }

    private void checkDB() {
        if (db.isMoneyEmpty()) {
            getMoney();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(WellcomeActivity.this, StartActivity.class);
                    WellcomeActivity.this.startActivity(mainIntent);
                    WellcomeActivity.this.finish();
                }
            }, 2000);
        }
    }


    private void getMoney() {
        String link = getResources().getString(R.string.linkmn);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MNT", response);
                try {
                    JSONObject jo = new JSONObject(response);

                    for (int i2 = 1; i2 < jo.length(); i2++) {

                        JSONObject jo2 = jo.getJSONObject(i2 + "");
                        db.addMN(new ListMN(jo2.getString("id"), jo2.getString("name")));
                    }
                    checkDB();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetCL get = new GetCL(link, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_CALENDAR))
            permissionsNeeded.add("Calendar");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsNeeded.add("GPS");
        } else {
            checkDB();
        }


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // hien thi tắt
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                }

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }


        }

        return;
    }


    // luu lai su lua chon
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // Check for ACCESS_FINE_LOCATION
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    checkDB();
                } else {
                    // Permission Denied
//                    Toast.makeText(WellcomeActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
//                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
