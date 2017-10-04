package com.nf.vi.needfoodshipper.MainClass;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nf.vi.needfoodshipper.database.DBHandle;
import com.nf.vi.needfoodshipper.database.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewDealActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView imgShipper;
    private CountDownTimer ctime;
    private TextView tvTitle, tvTenShiper, tvBao;
    int mypage = 1;
    private MainAdapter adapter;
    private ArrayList<MainConstructor> arr;
    private int check = 0;
    Session ses;
//    int page;
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
        ses = new Session(getApplicationContext());
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
                if (ld.size() > 14) {
                    mypage++;
//                    ld.clear();
//                    adapter.notifyDataSetChanged();
                    order1(mypage);
//                    Toast.makeText(getApplication(),mypage+"",Toast.LENGTH_SHORT).show();
                    Log.d("pagedo",mypage+"");
                }

            }
        };
        order(mypage);
        // Adds the scroll listener to RecyclerView
        rc.addOnScrollListener(scrollListener);

    }
    //tabhost của cái này ở Oder in

    private void order(int page) {


        final String link = getResources().getString(R.string.getListOrderShiperAPI);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ASSS",response);
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
                                Log.d("OR", Order.toString());
                                JSONArray listProduct = Order.getJSONArray("listProduct");
                                JSONObject infoOrder = Order.getJSONObject("infoOrder");
                                JSONObject infoCustomer = Order.getJSONObject("infoCustomer");
                                for (int k = 0; k < listProduct.length(); k++) {
                                    JSONObject idx = listProduct.getJSONObject(k);
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
                                ld.add(new MainConstructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), Order.getString("note"), status, code, listProduct.toString()));
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

        OrderRequest loginRequest = new OrderRequest(page + "", accessToken, link, response);
        RequestQueue queue = Volley.newRequestQueue(NewDealActivity.this);
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
    private void order1(int page) {


        final String link = getResources().getString(R.string.getListOrderShiperAPI);
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
                        if (checktime == true) {
                            ctime.cancel();
                        }
                        Log.d("ASSS",response);
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
                                Log.d("OR", Order.toString());
                                JSONArray listProduct = Order.getJSONArray("listProduct");
                                JSONObject infoOrder = Order.getJSONObject("infoOrder");
                                JSONObject infoCustomer = Order.getJSONObject("infoCustomer");
                                for (int k = 0; k < listProduct.length(); k++) {
                                    JSONObject idx = listProduct.getJSONObject(k);
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
                                ld.add(new MainConstructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), Order.getString("note"), status, code, listProduct.toString()));
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

        OrderRequest loginRequest = new OrderRequest(page + "", accessToken, link, response);
        RequestQueue queue = Volley.newRequestQueue(NewDealActivity.this);
        queue.add(loginRequest);
    }
}
