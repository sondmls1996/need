package com.nf.vi.needfoodshipper.MainClass;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaittingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView tvTitle, tvTenShiper;
    WaittingAdapter adapter;
    ArrayList<MainConstructor> arr;
    int check = 0;
    private List<WaittingContructor> ld;
    RecyclerView rc;
    private DBHandle db;
    private List<ListUserContructor> list;
    String fullName, accessToken;
    SwipeRefreshLayout swipeRefresh;

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

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshwaitting);

        rc = (RecyclerView) findViewById(R.id.rcvwaitting);
        ld = new ArrayList<>();
        adapter = new WaittingAdapter(getApplicationContext(), ld);
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        swipeRefresh.setOnRefreshListener(this);
        order();
    }

    private void order() {
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
//        dvtoken = sharedPreferences.getString(getString(R.string.F_CM), "");
        String page = "1";
        final String link = getResources().getString(R.string.getListOrderWaitingShiperAPI);

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
                        ld.add(new WaittingContructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"), infoOrder.getString("moneyShip"), status, code));


                    }
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

        WaittingRequest loginRequest = new WaittingRequest(page, accessToken, link, response);
        RequestQueue queue = Volley.newRequestQueue(WaittingActivity.this);
        queue.add(loginRequest);
    }


    @Override
    public void onRefresh() {
        ld.clear();
        order();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(), "Nhấn 2 lần để thoát", Toast.LENGTH_SHORT).show();
        check++;
        if (check == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WaittingActivity.this);
            alertDialogBuilder.setTitle("ManMo");
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
