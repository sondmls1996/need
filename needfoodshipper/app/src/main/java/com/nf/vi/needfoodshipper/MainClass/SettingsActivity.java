package com.nf.vi.needfoodshipper.MainClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Constructor.ListLangContructor;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.DangXuatRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;
import com.nf.vi.needfoodshipper.database.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private Spinner spthang;
    public String[] Dsthang = {"Viet Nam", "English"};
    private String ketqua = "";
    private TextView tvTitle;
    private Button btnSignout;
    private ProgressDialog progressDialog;
    String lang;

    DBHandle db;
    Session ses;
    String token, iddb, lang1;
    List<ListUserContructor> list;
    List<ListLangContructor> list1;
    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        progressDialog = new ProgressDialog(this);


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnSignout = (Button) findViewById(R.id.btnSignout);
        tvTitle.setText(getString(R.string.cdtoobar));
        spthang = (Spinner) findViewById(R.id.spthang);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Dsthang);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spthang.setAdapter(adapter);
//        Toast.makeText(getApplicationContext(), lang1, Toast.LENGTH_SHORT).show();
        String lang = "";
        if (lang1.equals("Viet Nam")) {
            lang = "vi"; // your language
            spthang.setSelection(0);
        } else if (lang1.equals("English")) {
            lang = "en"; // your language
            spthang.setSelection(1);
        }
        changeLang(lang);
        loadLocale();
        spthang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ketqua = Dsthang[position];
                String lang = "";
                if (ketqua == "Viet Nam") {
                    lang = "vi"; // your language

                } else if
                        (ketqua == "English") {
                    lang = "en"; // your language

                }
                changeLang(lang);
//                Toast.makeText(getApplicationContext(), ketqua, Toast.LENGTH_SHORT).show();
                db.updateinfo1(iddb, ketqua);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//               ketqua = "";

            }


        });


        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplication(),"chnas",Toast.LENGTH_LONG).show();
                logout();
            }
        });
    }

    public void logout() {
        final String link = getResources().getString(R.string.checkLogoutShiperAPI);
        progressDialog.setMessage(getResources().getString(R.string.logut));
        String it = token;
        Log.d("CODELOG", it);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    Log.d("tsdfsd", code);

                    if (code.equals("0")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                        db.deleteAll();
                        ses.setLoggedin(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (code.equals("-1")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                        db.deleteAll();
                        ses.setLoggedin(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        progressDialog.dismiss();
                        db.deleteAll();
                        ses.setLoggedin(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DangXuatRequest dangXuatRequest = new DangXuatRequest(it, link, response);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(dangXuatRequest);
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(),MainActivity.class));
        finish();

    }
}
