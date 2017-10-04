package com.nf.vi.needfoodshipper.MainClass;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Checkinternet.ConnectivityReceiver;
import com.nf.vi.needfoodshipper.Constructor.ListLangContructor;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.LoginRequest;
import com.nf.vi.needfoodshipper.Request.PostTokenRequest;
import com.nf.vi.needfoodshipper.SupportClass.Networks;
import com.nf.vi.needfoodshipper.database.DBHandle;
import com.nf.vi.needfoodshipper.database.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    Button btnLogin;
    EditText edtSdt, edtPass;
    String sdt, pass;
    private int check = 0;
    DBHandle db;
    Context context;
    LocationManager locationManager;
    boolean GpsStatus;
    String token, iddb, lang1;
    List<ListUserContructor> list;
    List<ListLangContructor> list1;
    Session ses;
    String dvtoken;
    ProgressDialog progressDialog;
    TextView forgetpass;
    private Locale myLocale;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
        dvtoken = sharedPreferences.getString(getString(R.string.F_CM), "");
        db = new DBHandle(getApplicationContext());
        ses = new Session(getApplicationContext());
        list = db.getAllUser();
        list1 = db.getAllLang();
        for (ListUserContructor nu : list) {
            iddb = nu.getId();
            token = nu.getAccessToken();

        }
        for (ListLangContructor nl : list1) {
            lang1 = nl.getLang();
        }
        String lang = "";
        if (lang1 == "Viet Nam") {
            lang = "vi"; // your language

        } else if
                (lang1 == "English") {
            lang = "en"; // your language

        }
        context = getApplicationContext();
        changeLang(lang);
        loadLocale();


        edtSdt = (EditText) findViewById(R.id.edtSdt);
        edtPass = (EditText) findViewById(R.id.edtPass);
        forgetpass = (TextView) findViewById(R.id.forgetpass);
        progressDialog = new ProgressDialog(this);
        db = new DBHandle(this);
        ses = new Session(this);
        checkSS();


        btnLogin = (Button) findViewById(R.id.btnLogin);
//        checkConnection();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ckeckmang();



            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LayMaActivity.class));
            }
        });
        insertDummyContactWrapper();
    }

    private void login() {


        sdt = edtSdt.getText().toString();
        pass = edtPass.getText().toString();
        final String link = getResources().getString(R.string.checkLoginAPI);
        progressDialog.setMessage(getString(R.string.diangnhap));
        progressDialog.show();
        if (sdt.matches("") || pass.matches("")) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getString(R.string.dithieu), Toast.LENGTH_SHORT).show();
        } else {
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("HCODE",response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("code");
                        if (code.equals("0")) {
                            Toast.makeText(getApplication(), getString(R.string.diathanhcong), Toast.LENGTH_SHORT).show();
                            JSONObject user = json.getJSONObject("user");
                            String fullname = user.getString("fullName");
                            String email1 = user.getString("email");
                            String phone1 = user.getString("fone");
                            String pass1 = user.getString("pass");
                            String address1 = user.getString("address");
                            String skype1 = user.getString("skype");
                            String birthday1 = user.getString("birthday");
                            String facebook1 = user.getString("facebook");
                            String description1 = user.getString("description");
                            String code1 = user.getString("code");
                            String accessToken1 = user.getString("accessToken");
                            String id1 = user.getString("id");
                            postToken(accessToken1);

                            Log.d("ACESS", response);
                            db.addUser(new ListUserContructor(id1, code1, fullname, phone1, email1, address1, birthday1, accessToken1, pass1, skype1, facebook1, description1));
                            db.addLang(new ListLangContructor(id1, "Viet Nam"));
                            ses.setLoggedin(true);
                            progressDialog.dismiss();

                            Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i1);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplication(), getString(R.string.disaiten), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(sdt, pass, link, response);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }

    }

    public void checkSS() {
        if (ses.loggedin()) {
            Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i2);
            finish();
        }
    }

    private void postToken(final String tokent) {
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    //Log.d("RESPONSE",response);
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    Log.d("TOKEN", dvtoken);
                    if (code.equals("0")) {

                    } else {
                        Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostTokenRequest post = new PostTokenRequest(tokent, dvtoken, getResources().getString(R.string.saveTokenDeviceShiperAPI), response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";

            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        updateTexts();
    }


    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void ckeckmang() {
        Networks networks = new Networks();
        Boolean conn = networks.checkNow(this.getApplicationContext());
        if (conn == true) {
            gps();

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.kmang), Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
    }

    public void gps() {
        CheckGpsStatus();
            if (GpsStatus == true) {
                login();
//            textview.setText("Location Services Is Enabled");
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            textview.setText("Location Services Is Disabled");
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);


    }

    public void CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("chụp ảnh");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Lấy ảnh từ thư viện");
        if (!addPermission(permissionsList, Manifest.permission.LOCATION_HARDWARE))
            permissionsNeeded.add("Vị trí GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Vị trí GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Bộ nhớ");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }

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
}
