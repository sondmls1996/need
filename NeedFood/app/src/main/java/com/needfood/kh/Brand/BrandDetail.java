package com.needfood.kh.Brand;


import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.SearchAdapter;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.SearchConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Maps.MapsActivity;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.Barcode.QRCamera;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.ViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.needfood.kh.R.menu.main;

public class BrandDetail extends AppCompatActivity {
    TabLayout tabLayout;
    TextView tvname,tvadr,bran,tvp,txthang;
    ViewPager viewPager;
    ListView lvs;
    String mns;
    SearchAdapter adapter;
    ArrayList<SearchConstructor> arrs;
    ViewAdapter viewAdapter;
    List<ListMN> list;
    DataHandle db;
    EditText edsearch;
    String idsl;
    public static String idsel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_detail);
        Intent it = getIntent();
        idsl = it.getStringExtra("ids");
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
        db = new DataHandle(this);
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
                    tvname.setText(sl.getString("fullName"));
                    tvadr.setText(sl.getString("address"));
                    bran.setText(sl.getString("branchName"));
                    tvp.setText(sl.getString("fone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
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

}
