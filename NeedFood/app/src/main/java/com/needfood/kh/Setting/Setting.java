package com.needfood.kh.Setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.Language;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Setting extends AppCompatActivity {
    LoginButton lgb;
    AccessTokenTracker accessTokenTracker;
    Session ses;
    LoginManager loginManager;
    CallbackManager callbackManager;
    TextView logout;
    DataHandle db;
    List<InfoConstructor> list;
    String type,token;

    String ngonngu[] = {"Tiếng Anh", "Tiếng Việt"};
    Spinner sp;
    Locale myLocale;
    List<Language> lt;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.action_settings));
        ses = new Session(getApplicationContext());
        lgb = (LoginButton) findViewById(R.id.loginset);
        logout = (TextView) findViewById(R.id.logout);
        callbackManager = CallbackManager.Factory.create();
        db = new DataHandle(getApplicationContext());
        list = db.getAllInfor();
        for (InfoConstructor it : list) {
            token = it.getAccesstoken();
            type = it.getType();
        }
        if(type.equals("0")){
            lgb.setVisibility(View.GONE);
        }else{
            logout.setVisibility(View.GONE);
        }
        lt = db.getLan();
        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        loadLocale();
        init();
        lgb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user clicks on facebook logout
                    ses.setLoggedin(false);
                    Intent it = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        };
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogout();
            }
        });


    }

    public void init() {
        sp = (Spinner) findViewById(R.id.sp);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                ngonngu);
        sp.setAdapter(spinnerArrayAdapter);
        if (lt.get(lt.size() - 1).getId().equals("0")) {
            sp.setSelection(1);
        } else {
            sp.setSelection(0);
        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String lang = "vi";
                String a = ngonngu[position];

                if (a == "Tiếng Anh") {
                    lang = "en";
                    db.addCheckLan(new Language("1"));

                } else if (a=="Tiếng Việt"){
                    lang = "vi";
                    db.addCheckLan(new Language("0"));

                }
                changeLang(lang);
                loadLocale();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ASS", String.valueOf(data));
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkLogout() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng xuất");
        progressDialog.show();

        Map<String, String> map = new HashMap<String, String>();
        map.put("accessToken", token);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    Log.d("CODELOG", code);

                        progressDialog.dismiss();
                        db.deleteInfo();
                        ses.setLoggedin(false);
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);
                        finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL logout = new PostCL(getResources().getString(R.string.linklogout), map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(logout);
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
}
