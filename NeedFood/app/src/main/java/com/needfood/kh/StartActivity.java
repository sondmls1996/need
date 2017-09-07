package com.needfood.kh;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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
import com.needfood.kh.News.Hotdeal;
import com.needfood.kh.News.TabFragment;
import com.needfood.kh.Notif.Notif;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.Sugges.SuggessFrag;
import com.needfood.kh.SupportClass.NetworkCheck;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
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
    public static Toolbar toolbar;
    TextView nf, su, no, mo;
    TextView se;
    CoordinatorLayout activity_news;
    NetworkCheck networkCheck;
    public static int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
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
//        if (db.isMoneyEmpty()) {
//            getMoney();
//        }
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
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.needfood.kh",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

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
//        insertDummyContactWrapper();

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

    @Override
    protected void onResume() {
        super.onResume();
        // .... other stuff in my onResume ....
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


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cam:
                Dialogchoice();
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
                fragmentClass = Hotdeal.class;
                ReplaceFrag(fragmentClass);
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
    public void Dialogchoice(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_scan);
        dialog.show();
        LinearLayout lnqr = (LinearLayout) dialog.findViewById(R.id.lnqr);
        LinearLayout lnbar = (LinearLayout) dialog.findViewById(R.id.lnbar);
        lnqr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), QRCamera.class);
                startActivity(it);

            }
        });
        lnbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), BarcodeCamera.class);
                startActivity(it);
            }
        });

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // Check for ACCESS_FINE_LOCATION
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

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


    @Override
    public void onBackPressed() {
        check++;
        if (check < 2) {
            Toast.makeText(getBaseContext(), "Nhấn 2 lần để thoát", Toast.LENGTH_SHORT).show();
        } else if (check >= 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
            alertDialogBuilder.setTitle("Needfood");
            alertDialogBuilder
                    .setMessage("Bạn thực sự muốn thoát ứng dụng Manmo ?")
                    .setCancelable(false)
                    .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            check = 0;

                        }
                    })
                    .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            check = 0;
                            finish();
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {

        }
    }

}
