package com.nf.vi.needfoodshipper.MainClass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Adapter.SearchAdapter;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.SearchContructer;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.SearchRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private SearchAdapter adapterh;
    ArrayList<SearchContructer> arr;
    private RecyclerView rcvSearch;
    private TextView tvTitle;
    private DBHandle db;
    private List<ListUserContructor> list;
    String fullName, accessToken;
    private List<SearchContructer> lh2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            fullName = nu.getFullName();
            accessToken = nu.getAccessToken();

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
//        done();
        rcvSearch = (RecyclerView) findViewById(R.id.rcvSearch);
        tvTitle = (TextView) findViewById(R.id.tvTitle);



        lh2 = new ArrayList<>();
        adapterh = new SearchAdapter(getApplicationContext(), lh2);
//        adapterh = new Sear(getApplicationContext(), lh2);
        rcvSearch.setAdapter(adapterh);
        rcvSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
    }

    private void search() {
//        lh.clear();
        lh2.clear();
        adapterh.notifyDataSetChanged();
        String key = edtSearch.getText().toString();
        String page = "1";
        final String link = getResources().getString(R.string.searchOrderShiperAPI);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GG", response);
                try {

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
                        String timeShiper1 = infoOrder.getString("timeShiper");

                        String fullName = infoCustomer.getString("fullName");
                        String fone1 = infoCustomer.getString("fone");
                        String address = infoCustomer.getString("address");
                        String id = Order.getString("id");
                        String status = Order.getString("status");
                        String code1 = Order.getString("code");

                        Log.d("hh2", status);
                        lh2.add(new SearchContructer(id, sb.toString(), address, fone1, fullName, timeShiper1, infoOrder.getString("totalMoneyProduct"), status, code1));


                    }
                    adapterh.notifyDataSetChanged();
//                    swipeRefresh.setRefreshing(false);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

//        SearchRequest loginRequest = new SearchRequest(page, accessToken, key, link, response);
        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
//        queue.add(loginRequest);
    }
}
