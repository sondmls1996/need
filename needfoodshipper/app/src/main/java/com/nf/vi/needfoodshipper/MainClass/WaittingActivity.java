package com.nf.vi.needfoodshipper.MainClass;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.nf.vi.needfoodshipper.database.Session;

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
    int mypage=1;
    private TextView tvTitle, tvTenShiper, tvBao;
    Session ses;
    int page;
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
        ses = new Session(getApplicationContext());
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
                if (ld.size() > 14) {
                    mypage++;
                    order1(mypage);
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        rc.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        ld.clear();
        adapter.notifyDataSetChanged();
        order(mypage);
    }

    private void order(int page) {
        ld.clear();
        adapter.notifyDataSetChanged();
        final String link = getResources().getString(R.string.getListOrderWaitingShiperAPI);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("{\"code\":-1}")) {
                        if (checktime == true) {
                            ctime.cancel();
                        }
//                        JSONObject json1 = new JSONObject(response);
//                        String code1 = json1.getString("code");
//                        if (code1.equals("-1")) {
                        swipeRefresh.setRefreshing(false);
//                        progressDialog.dismiss();
                        Toast.makeText(getApplication(), getString(R.string.codemot), Toast.LENGTH_SHORT).show();
                        db.deleteAll();
                        ses.setLoggedin(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

//                        }
                    } else {
                        ld.clear();
                        adapter.notifyDataSetChanged();
                        Log.d("GG", response);
                        if (checktime == true) {
                            ctime.cancel();
                        }
                        JSONArray arr = new JSONArray(response);
                        if (arr.length() == 0) {
                            if(ld.size()==0){
                                tvBao.setVisibility(View.VISIBLE);
                                rc.setVisibility(View.GONE);
                            }else{
                                tvBao.setVisibility(View.GONE);
                            }

                            swipeRefresh.setRefreshing(false);
//                        ld.clear();
//                        swipeRefresh.setRefreshing(false);
                        } else {
                            tvBao.setVisibility(View.GONE);
                            rc.setVisibility(View.VISIBLE);
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
                                ld.add(new WaittingContructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), Order.getString("note"), status, code, listProduct.toString()));
                            }
                            adapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
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
                mypage=1;
                ld.clear();
                adapter.notifyDataSetChanged();
                order(mypage);
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
        Toast.makeText(getBaseContext(), getResources().getString(R.string.dclick), Toast.LENGTH_SHORT).show();
        check++;
        if (check == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WaittingActivity.this);
            alertDialogBuilder.setTitle("Needfood");
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

    private void order1(int page) {
//        ld.clear();
//        adapter.notifyDataSetChanged();
        final String link = getResources().getString(R.string.getListOrderWaitingShiperAPI);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("{\"code\":-1}")) {
                        if (checktime == true) {
                            ctime.cancel();
                        }
//                        JSONObject json1 = new JSONObject(response);
//                        String code1 = json1.getString("code");
//                        if (code1.equals("-1")) {
                        swipeRefresh.setRefreshing(false);
//                        progressDialog.dismiss();
                        Toast.makeText(getApplication(), getString(R.string.codemot), Toast.LENGTH_SHORT).show();
                        db.deleteAll();
                        ses.setLoggedin(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

//                        }
                    } else {
                        Log.d("GG", response);
                        if (checktime == true) {
                            ctime.cancel();
                        }
                        JSONArray arr = new JSONArray(response);
                        if (arr.length() == 0) {
                            if(ld.size()==0){
                                tvBao.setVisibility(View.VISIBLE);
                                rc.setVisibility(View.GONE);
                            }else{
                                tvBao.setVisibility(View.GONE);
                            }

                            swipeRefresh.setRefreshing(false);
//                        ld.clear();
//                        swipeRefresh.setRefreshing(false);
                        } else {
                            tvBao.setVisibility(View.GONE);
                            rc.setVisibility(View.VISIBLE);
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
                                ld.add(new WaittingContructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), Order.getString("note"), status, code, listProduct.toString()));
                            }
                            adapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
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

}
