package com.needfood.kh;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.SearchAdapter;
import com.needfood.kh.Constructor.Language;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.SearchConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Maps.MapsActivity;
import com.needfood.kh.More.More;
import com.needfood.kh.News.TabFragment;
import com.needfood.kh.Notif.Notif;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.Sugges.SuggessFrag;
import com.needfood.kh.SupportClass.GetCL;
import com.needfood.kh.SupportClass.NetworkCheck;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.needfood.kh.R.menu.main;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    LinearLayout imgnewss, imgsug, imgnotif, imgmore;
    Class fragmentClass;
    DataHandle db;
    public static int pg = 0;
    ImageView img0;
    List<ListMN> list;
    String mns;
    EditText edsearch;
    ProgressBar prgb;
    FrameLayout contf;
    List<Language> lt;
    Locale myLocale;
    ListView lvs;
    ArrayList<SearchConstructor> arrs;
    SearchAdapter adapter;
    TextView nf, su, no, mo;
    TextView se;
    CoordinatorLayout activity_news;
    NetworkCheck networkCheck;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkCheck = new NetworkCheck();
        Boolean conn = networkCheck.checkNow(getApplicationContext());
        if (conn == true) {

        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
        activity_news = (CoordinatorLayout) findViewById(R.id.activity_news);
        edsearch = (EditText) findViewById(R.id.edsearch);
        edsearch.setInputType(InputType.TYPE_NULL);
        edsearch.requestFocus();
        db = new DataHandle(this);
        arrs = new ArrayList<>();
        contf = (FrameLayout) findViewById(R.id.contentContainer);
        if (db.isMoneyEmpty()) {
            getMoney();
        }
        lt = db.getLan();
        if (lt.size() > 0) {

            String a = "en";
            if (lt.get(lt.size() - 1).getId().equals("1")) {
                a = "en";
                edsearch.setHint("Search....");
                nf = (TextView) findViewById(R.id.nf);
                su = (TextView) findViewById(R.id.su);
                no = (TextView) findViewById(R.id.no);
                mo = (TextView) findViewById(R.id.mo);
                nf.setText("New Feed");
                su.setText("Suggestion");
                no.setText("Notification");
                mo.setText("More");
                changeLocale(a);
            } else {
                a = "vi";
            }
            changeLocale(a);
        } else {
            db.addCheckLan(new Language("0"));
            String a = "vi";
            changeLocale(a);
        }

        edsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog();
            }
        });
        img0 = (ImageView) findViewById(R.id.dod);
        imgnewss = (LinearLayout) findViewById(R.id.imgnewstart);
        imgsug = (LinearLayout) findViewById(R.id.sug);
        imgnotif = (LinearLayout) findViewById(R.id.notif);
        imgmore = (LinearLayout) findViewById(R.id.more);
        insertDummyContactWrapper();

        img0.setOnClickListener(this);
        imgnewss.setOnClickListener(this);
        imgsug.setOnClickListener(this);
        imgnotif.setOnClickListener(this);
        imgmore.setOnClickListener(this);

        fragmentClass = TabFragment.class;
        ReplaceFrag(fragmentClass);

    }

    private void showLog() {
        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchdialog);
        dialog.setCanceledOnTouchOutside(true);
        lvs = (ListView) dialog.findViewById(R.id.lvsearch);
        adapter = new SearchAdapter(getApplicationContext(), R.layout.customsearch, arrs);
        lvs.setAdapter(adapter);
        lvs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                it.putExtra("idprd", arrs.get(position).getId());
                startActivity(it);
            }
        });
        final EditText edt = (EditText) dialog.findViewById(R.id.searched);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StringBuilder sb = new StringBuilder(edt.getText().toString());
                arrs.clear();
                adapter.notifyDataSetChanged();
                searchSP(sb.toString());
            }
        });
        dialog.show();
    }

    private void searchSP(String s) {
        lvs.setVisibility(View.VISIBLE);
        String link = getResources().getString(R.string.linksearch);
        Map<String, String> map = new HashMap<>();
        map.put("page", "1");
        map.put("key", s);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("SEARS", response);
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject idx = ja.getJSONObject(i);
                        JSONObject prd = idx.getJSONObject("Product");
                        JSONArray imgs = prd.getJSONArray("images");
                        String typemn = prd.getString("typeMoneyId");
                        list = db.getMNid(typemn);
                        for (ListMN lu : list) {
                            mns = lu.getMn();
                        }
                        arrs.add(new SearchConstructor(imgs.getString(0), prd.getString("title"), prd.getString("id"), prd.getString("price"),
                                mns, prd.getString("nameUnit")));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }


    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cam:
                Intent it = new Intent(getApplicationContext(), QRCamera.class);
                startActivity(it);
                break;
            case R.id.mark:
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    public void ReplaceFrag(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgnewstart:
                fragmentClass = TabFragment.class;
                ReplaceFrag(fragmentClass);
                pg = 0;
                break;
            case R.id.dod:
                fragmentClass = TabFragment.class;
                ReplaceFrag(fragmentClass);
                pg = 1;
                break;
            case R.id.sug:

                fragmentClass = SuggessFrag.class;
                ReplaceFrag(fragmentClass);
                break;
            case R.id.notif:
                fragmentClass = Notif.class;
                ReplaceFrag(fragmentClass);
                break;
            case R.id.more:
                fragmentClass = More.class;
                ReplaceFrag(fragmentClass);
                break;

        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.READ_CALENDAR))
            permissionsNeeded.add("Calendar");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("GPS-LOCAL");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read Storage");
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

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(StartActivity.this)
                .setMessage(message)
                .setPositiveButton("Đòng ý", okListener)
                .setNegativeButton("Hủy", null)
                .create()
                .show();
    }

    public void changeLocale(String lang) {

        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.dclick), Toast.LENGTH_SHORT).show();
        check++;
        if (check == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
            alertDialogBuilder.setTitle("ManMo");
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.exit))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            check = 0;
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

}
