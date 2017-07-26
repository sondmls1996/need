package com.nf.vi.needfoodshipper.MainClass;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nf.vi.needfoodshipper.SupportClass.EndlessRecyclerViewScrollListener;
import com.nf.vi.needfoodshipper.SupportClass.EndlessScroll;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewDealActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView imgShipper;
    private CountDownTimer ctime;
    private TextView tvTitle, tvTenShiper, tvBao;
    private MainAdapter adapter;
    private ArrayList<MainConstructor> arr;
    private int check = 0;

    private List<MainConstructor> ld;
    private RecyclerView rc;
    private DBHandle db;
    boolean checktime = false;
    private List<ListUserContructor> list;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String fullName, accessToken;
    private SwipeRefreshLayout swipeRefresh;

    private LinearLayout lnYourInformation, lnHistory, lnSettings;

    private GoogleMap gg;
    double la = 0, lo = 0, la2, lo2;
    private GoogleApiClient client;
    private LocationManager locationManager;
    private Location l2;
    private LinearLayoutManager linearLayoutManager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);
        db = new DBHandle(this);
        list = db.getAllUser();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        for (ListUserContructor nu : list) {
            fullName = nu.getFullName();
            accessToken = nu.getAccessToken();

        }
        tvBao = (TextView) findViewById(R.id.tvBao);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefresh);

        rc = (RecyclerView) findViewById(R.id.rcv);
        ld = new ArrayList<>();
        adapter = new MainAdapter(getApplicationContext(), ld);
        rc.setAdapter(adapter);
        rc.setLayoutManager(linearLayoutManager);
        swipeRefresh.setOnRefreshListener(this);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                page++;

                    order(page);

            }
        };
        order(1);
        // Adds the scroll listener to RecyclerView
        rc.addOnScrollListener(scrollListener);

    }

    private void order(int page) {


        final String link = getResources().getString(R.string.getListOrderShiperAPI);


        Response.Listener<String> response = new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    Log.d("GG", response);
                    if (checktime == true) {
                        ctime.cancel();
                    }
                    if (response.equals("[]")) {
                        tvBao.setVisibility(View.VISIBLE);
                    }

                    JSONArray arr = new JSONArray(response);

                    for (int i = 0; i < arr.length(); i++) {
                        StringBuilder sb = new StringBuilder();
                        StringBuilder soluong = new StringBuilder();
                        StringBuilder sanpham = new StringBuilder();
                        StringBuilder dongia = new StringBuilder();
                        StringBuilder thanhtien = new StringBuilder();
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
                            soluong.append(idx.getString("quantity") + "\n");
                            sanpham.append(idx.getString("title") + "\n");
                            dongia.append(idx.getString("price") + "\n");
                            thanhtien.append(idx.getString("money") + "\n");
                        }

                        String timeShiper = infoOrder.getString("timeShiper");
                        String fullName = infoCustomer.getString("fullName");
                        String fone = infoCustomer.getString("fone");
                        String address = infoCustomer.getString("address");
                        String id = Order.getString("id");
                        Log.d("IDD", Order.getString("id"));
                        String status = Order.getString("status");
                        String code = Order.getString("code");

                        Log.d("hh", fullName);
                        ld.add(new MainConstructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), infoOrder.getString("moneyShip"), status, code, soluong.toString(), sanpham.toString(), dongia.toString(), thanhtien.toString()));
                    }

                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

        OrderRequest loginRequest = new OrderRequest(page + "", accessToken, link, response);
        RequestQueue queue = Volley.newRequestQueue(NewDealActivity.this);
        queue.add(loginRequest);
    }

    @Override
    public void onRefresh() {
        ld.clear();
        adapter.notifyDataSetChanged();
        ctime = new CountDownTimer(15000, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                checktime = true;

                order(1);

            }

            @Override
            public void onFinish() {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getApplicationContext(), getString(R.string.dhloiketnoi), Toast.LENGTH_SHORT).show();
            }

        };
        ctime.start();
    }

    @Override
    public void onBackPressed() {

        check++;
        if (check == 1) {
            Toast.makeText(getBaseContext(), getString(R.string.dhgailan), Toast.LENGTH_SHORT).show();
        }
        if (check == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDealActivity.this);
            alertDialogBuilder.setTitle("Needfood");
            alertDialogBuilder
                    .setMessage(getString(R.string.dhhoithoat))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dhkhong), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            check = 0;
                        }
                    })
                    .setNegativeButton(getString(R.string.dhdongy), new DialogInterface.OnClickListener() {
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
