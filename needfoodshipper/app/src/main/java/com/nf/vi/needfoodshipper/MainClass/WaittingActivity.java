package com.nf.vi.needfoodshipper.MainClass;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Adapter.MainAdapter;
import com.nf.vi.needfoodshipper.Adapter.WaittingAdapter;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.Constructor.WaittingContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.OrderRequest;
import com.nf.vi.needfoodshipper.Request.WaittingRequest;
import com.nf.vi.needfoodshipper.SupportClass.EndlessRecyclerViewScrollListener;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaittingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private WaittingAdapter adapter;
    private ArrayList<MainConstructor> arr;
    private CountDownTimer ctime;
    private boolean checktime = false;
    private int check = 0;
    private TextView tvTitle, tvTenShiper, tvBao;

    private List<WaittingContructor> ld;
    private RecyclerView rc;
    private DBHandle db;
    private List<ListUserContructor> list;
    private String fullName, accessToken;
    private SwipeRefreshLayout swipeRefresh;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitting);
        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            fullName = nu.getFullName();
            accessToken = nu.getAccessToken();

        }
        tvBao = (TextView) findViewById(R.id.tvBao);


        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshwaitting);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc = (RecyclerView) findViewById(R.id.rcvwaitting);
        ld = new ArrayList<>();
        adapter = new WaittingAdapter(getApplicationContext(), ld);
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

    @Override
    protected void onResume() {
        super.onResume();
        ld.clear();
        adapter.notifyDataSetChanged();
        order(1);
    }

    private void order(int page) {
        ld.clear();
        adapter.notifyDataSetChanged();
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
//        dvtoken = sharedPreferences.getString(getString(R.string.F_CM), "");
//        String page = "1";
        final String link = getResources().getString(R.string.getListOrderWaitingShiperAPI);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("GG", response);
                    if (checktime == true) {
                        ctime.cancel();
                    }
                    if (response.equals("")) {
                        tvBao.setVisibility(View.VISIBLE);

                    }
//                    JSONArray arr = new JSONArray(response);
//                    for (int i = 0; i < arr.length(); i++) {
//                        StringBuilder sb = new StringBuilder();
//                        StringBuilder soluong = new StringBuilder();
//                        StringBuilder sanpham = new StringBuilder();
//                        StringBuilder dongia = new StringBuilder();
//                        StringBuilder thanhtien = new StringBuilder();
//                        String money;
//                        JSONObject json = arr.getJSONObject(i);
//                        JSONObject Order = json.getJSONObject("Order");
//
//
//                        JSONObject infoOrder = Order.getJSONObject("infoOrder");
//                        JSONObject infoCustomer = Order.getJSONObject("infoCustomer");
//
//
//                        JSONArray listProduct = Order.getJSONArray("listProduct");
//                        for (int j = 0; j < listProduct.length(); j++) {
//                            JSONObject json1 = listProduct.getJSONObject(j);
//                            String title = json1.getString("title");
//                            String quantity = json1.getString("quantity");
//                            String price = json1.getString("price");
//                            String money1 = json1.getString("money");
//                            sb.append(quantity + title + ";" + "\t");
//
//                        }
//                        String timeShiper = infoOrder.getString("timeShiper");
//
//                        String fullName = infoCustomer.getString("fullName");
//                        String fone = infoCustomer.getString("fone");
//                        String address = infoCustomer.getString("address");
//                        String id = Order.getString("id");
//                        String status = Order.getString("status");
//                        String code = Order.getString("code");
//
//                        Log.d("hh", fullName);
//                        ld.add(new WaittingContructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), infoOrder.getString("moneyShip"), status, code, listProduct.toString()));
//=======
                     else {
                        tvBao.setVisibility(View.GONE);
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

                            JSONArray listProduct = Order.getJSONArray("listProduct");
                            JSONObject infoOrder = Order.getJSONObject("infoOrder");
                            JSONObject infoCustomer = Order.getJSONObject("infoCustomer");

                            for (int l = 0; l < listProduct.length(); l++) {
                                JSONObject idx = listProduct.getJSONObject(l);
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
                            String status = Order.getString("status");
                            String code = Order.getString("code");

                            Log.d("hh", fullName);
                            ld.add(new WaittingContructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), infoOrder.getString("moneyShip"), status, code, listProduct.toString()));



                        }
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

        WaittingRequest loginRequest = new WaittingRequest(page + "", accessToken, link, response);
        RequestQueue queue = Volley.newRequestQueue(WaittingActivity.this);
        queue.add(loginRequest);
    }


    @Override
    public void onRefresh() {



        ctime = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                checktime = true;
                ld.clear();
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
//<<<<<<< HEAD
//        Toast.makeText(getBaseContext(), getString(R.string.dhgailan), Toast.LENGTH_SHORT).show();
//=======
        Toast.makeText(getBaseContext(), getResources().getString(R.string.dclick), Toast.LENGTH_SHORT).show();

        check++;
        if (check == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WaittingActivity.this);
            alertDialogBuilder.setTitle("Shipper");
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.evs))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.dhkhong), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            check = 0;
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.dhdongy), new DialogInterface.OnClickListener() {
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
