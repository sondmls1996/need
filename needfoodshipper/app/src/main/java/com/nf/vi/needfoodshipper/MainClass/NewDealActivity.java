package com.nf.vi.needfoodshipper.MainClass;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.nf.vi.needfoodshipper.Adapter.MainAdapter;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.OrderRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewDealActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView imgShipper;

    private TextView tvTitle, tvTenShiper;
    MainAdapter adapter;
    ArrayList<MainConstructor> arr;

    private List<MainConstructor> ld;
    RecyclerView rc;
    private DBHandle db;
    private List<ListUserContructor> list;
    String fullName, accessToken;
    SwipeRefreshLayout swipeRefresh;

    private LinearLayout lnYourInformation, lnHistory, lnSettings;

    GoogleMap gg;
    double la = 0, lo = 0, la2, lo2;
    GoogleApiClient client;
    LocationManager locationManager;
    Location l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);
        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            fullName = nu.getFullName();
            accessToken = nu.getAccessToken();

        }

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.SwipeRefresh);

        rc = (RecyclerView) findViewById(R.id.rcv);
        ld = new ArrayList<>();
        adapter = new MainAdapter(getApplicationContext(), ld);
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        swipeRefresh.setOnRefreshListener(this);
        order();
    }
    private void order() {
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
//        dvtoken = sharedPreferences.getString(getString(R.string.F_CM), "");
        String page = "1";
        final String link = getResources().getString(R.string.getListOrderShiperAPI);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("GG", response);
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        StringBuilder sb = new StringBuilder();
                        String money;
                        JSONObject json = arr.getJSONObject(i);
                        JSONObject Order = json.getJSONObject("Order");

                        JSONObject listProduct = Order.getJSONObject("listProduct");
                        JSONObject infoOrder = Order.getJSONObject("infoOrder");
                        JSONObject infoCustomer = Order.getJSONObject("infoCustomer");


                        Iterator<String> ite = listProduct.keys();

                        while (ite.hasNext()) {
                            String key = ite.next();

                            JSONObject idx = listProduct.getJSONObject(key);
                            sb.append((idx.getString("quantity") + idx.getString("title")) + ";" + "\t");


                        }
                        String timeShiper = infoOrder.getString("timeShiper");

                        String fullName = infoCustomer.getString("fullName");
                        String fone = infoCustomer.getString("fone");
                        String address = infoCustomer.getString("address");
                        String id = Order.getString("id");
                        String status = Order.getString("status");
                        String code = Order.getString("code");

                        Log.d("hh", fullName);
                        ld.add(new MainConstructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"),infoOrder.getString("moneyShip"),status,code));


                    }
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

        OrderRequest loginRequest = new OrderRequest(page, accessToken, link, response);
        RequestQueue queue = Volley.newRequestQueue(NewDealActivity.this);
        queue.add(loginRequest);
    }

    @Override
    public void onRefresh() {
        ld.clear();
        order();
    }
}
