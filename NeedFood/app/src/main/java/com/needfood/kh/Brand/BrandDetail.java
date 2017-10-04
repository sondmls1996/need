package com.needfood.kh.Brand;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.ProductDetail.CheckConstructor;
import com.needfood.kh.Adapter.ProductDetail.DialogPreBrand;
import com.needfood.kh.Adapter.SearchAdapter;
import com.needfood.kh.Barcode.QRCamera;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.PreDialogConstructor;
import com.needfood.kh.Constructor.SearchConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.Login;
import com.needfood.kh.Maps.MapsActivity;
import com.needfood.kh.Product.Preview;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;
import com.needfood.kh.SupportClass.ViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.needfood.kh.R.menu.main;

public class BrandDetail extends AppCompatActivity {
    TabLayout tabLayout;
    TextView tvname,tvadr,bran,tvp;
    TextView txthang;
    ViewPager viewPager;
    ListView lvs;
    String mns, idu;
    Button bn2,edit2;
    Session session;
    SearchAdapter adapter;
    ArrayList<SearchConstructor> arrs;
    List<InfoConstructor> listu;
    ViewAdapter viewAdapter;
    ArrayList<PreDialogConstructor> precons;
    List<ListMN> list;
    List<CheckConstructor> listcheck;
    DataHandle db;
    String coin="";
    public static ArrayList<Integer> listship2;
    String typedis,codedis;
    EditText edsearch;
    public String idsl,fullname;
    public static String type="";
    public static TextView txttong2;
    public String tax,access;

    public static String idsel = "",idprd="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_detail);
        Intent it = getIntent();
        idsl = it.getStringExtra("ids");
        db = new DataHandle(this);
        listship2 = new ArrayList<>();
        type = it.getStringExtra("typeH");
        session = new Session(this);
        if(session.loggedin()){
            listu = db.getAllInfor();
            for(InfoConstructor lu:listu){
                access = lu.getAccesstoken();
                idu= lu.getId();
            }
        }



        bn2 = (Button)findViewById(R.id.bn2);
        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });
        edit2 = (Button)findViewById(R.id.bnedit2);
        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreDialog();
            }
        });
        txthang = (TextView) findViewById(R.id.txthang);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int tong = intent.getIntExtra(BubbleService.INTENTNAME, 0);

                        txthang.setText(NumberFormat.getNumberInstance(Locale.UK).format(tong));
                    }
                }, new IntentFilter(BubbleService.ACTION_LOCATION_BROADCAST)
        );
        startService(new Intent(BrandDetail.this, BubbleService.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvname = (TextView) findViewById(R.id.brname);
        bran = (TextView)findViewById(R.id.brn);

        if(!type.equals("0")){
            db.deleteAllPRD();
        }
        arrs = new ArrayList<>();
        tvadr = (TextView)findViewById(R.id.adr);
        tvp = (TextView)findViewById(R.id.tvphone);
        edsearch = (EditText) findViewById(R.id.edsearch);
        edsearch.setInputType(InputType.TYPE_NULL);
        edsearch.requestFocus();
        edsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog();
            }
        });
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest);
        scrollView.setFillViewport(true);


        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.vpage);
        viewAdapter = new ViewAdapter(getSupportFragmentManager());
        viewAdapter.addFragment(new MenuBrand(), getResources().getString(R.string.menu));

        viewAdapter.addFragment(new CommentBrand(), getResources().getString(R.string.comment));

        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(viewPager);
        viewAdapter.notifyDataSetChanged();

        viewAdapter.notifyDataSetChanged();
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.red));
        getBrand();
        addInfo();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private void addInfo() {
        String linkk = getResources().getString(R.string.linkgetinfo);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", access);
        map.put("idUseronl", idu);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("LOGA", response);
                    JSONObject js = new JSONObject(response);
                    JSONObject jo = js.getJSONObject("Useronl");

                    coin = jo.getString("coin");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);

    }
    private void sendOrder() {

        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        if (session.loggedin()) {
            int mnship = 0;
            int tong = 0;
            typedis = "0";
            codedis="";
            String quan;




            JSONArray jsonArray = new JSONArray();


            try {
                listcheck = db.getPrd();
                for (CheckConstructor lu : listcheck) {
                    listship2.add(Integer.parseInt(lu.getMns()));
                    JSONObject j1 = new JSONObject();
                    tong = Integer.parseInt(lu.getQuanli()) * Integer.parseInt(lu.getPrice()) + tong;
                    j1.put("quantity", lu.getQuanli());
                    j1.put("price", lu.getPrice());
                    j1.put("tickKM", lu.getTickkm());
                    j1.put("tickKM_percent", lu.getTickkm2());
                    j1.put("tickKM_money", lu.getTickkm3());
                    j1.put("barcode", lu.getBarcode());
                    j1.put("code", lu.getCode());
                    j1.put("title", lu.getTitle());
                    j1.put("money", Integer.parseInt(lu.getQuanli()) * Integer.parseInt(lu.getPrice()));
                    j1.put("note", lu.getNote());
                    j1.put("id", lu.getId());
                    j1.put("typeMoneyId", lu.getTypeid());
                    jsonArray.put(j1);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            mnship = Collections.max(listship2);

            Log.d("HAJAR", jsonArray.toString());

            int money = 0;


            HashMap<String, String> map = new HashMap<>();
            map.put("accessToken", access);
            map.put("listProduct", jsonArray.toString());
            map.put("money", tong + "");
            map.put("totalMoneyProduct", tong + (tong * (Integer.parseInt(tax))) / 100 + "");
            map.put("fullName", "");
            map.put("moneyShip", mnship + "");
            map.put("timeShiper", "");
            map.put("address", "");
            map.put("note", "");
            map.put("fone", "");
            // map.put("idUseronl",idu);
            map.put("idSeller", idsl);
//             Log.d("GTGT",  (money * (Integer.parseInt(tax)+1))/100+  "");
            Intent it = new Intent(getApplicationContext(), Preview.class);
            it.putExtra("map", map);
            it.putExtra("min", "VND");
            it.putExtra("stt", "nom");
            it.putExtra("tymn", "1");
            it.putExtra("codedis",codedis);
            it.putExtra("typediss",typedis);
            it.putExtra("tax", tax);
            it.putExtra("coin",coin);
            startActivity(it);
            pro.dismiss();
        } else {
            pro.dismiss();
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
            db.deleteAllPRD();
        }

    }
    private void showPreDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogprev);
        precons = new ArrayList<>();

        int tong = 0;
        RecyclerView rcp = (RecyclerView) dialog.findViewById(R.id.rcpre);
         txttong2 = (TextView) dialog.findViewById(R.id.txttong);
        DialogPreBrand preadap = new DialogPreBrand(getApplicationContext(), precons);

        rcp.setAdapter(preadap);
        LinearLayoutManager lnm = new LinearLayoutManager(getApplicationContext());
        rcp.setLayoutManager(lnm);
        listcheck = db.getPrd();

        for (CheckConstructor lu : listcheck) {
            tong = Integer.parseInt(lu.getPrice()) * Integer.parseInt(lu.getQuanli()) + tong;
            Log.d("SHOWALL", "quanli:" + lu.getQuanli() + "\n" + "price:" + lu.getPrice() + "\n" + "ID:" + lu.getId() + "name:" + lu.getTitle());
            if(!lu.getQuanli().equals("0")){
                precons.add(new PreDialogConstructor(lu.getQuanli(), lu.getPrice(), lu.getTitle(), lu.getId(), lu.getTypeid()));
            }


        }
        txttong2.setText(NumberFormat.getNumberInstance(Locale.UK).format(tong));
        preadap.notifyDataSetChanged();
        dialog.show();
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

    private void getBrand() {
        final String link = getResources().getString(R.string.linkifsel);
        Map<String, String> map = new HashMap<>();
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SLL", response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject sl = jo.getJSONObject("Seller");
                    tvname.setText(sl.getString("branchName"));
                    tvadr.setText(sl.getString("address"));
                    bran.setText(sl.getString("branchName"));
                    tvp.setText(sl.getString("fone"));
                    tax = sl.getString("taxNumber");
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
    public void onBackPressed() {
        if(!type.equals("0")){
            db.deleteAllPRD();
        }
        super.onBackPressed();
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
    public String getMyData() {
        return idsl;
    }
    public String getTax() {
        return tax;
    }

}
