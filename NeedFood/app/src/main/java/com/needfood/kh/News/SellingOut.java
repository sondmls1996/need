package com.needfood.kh.News;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.SellingOutAdapter;
import com.needfood.kh.Constructor.SellingConstructor;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.EndlessScroll;
import com.needfood.kh.SupportClass.GetCL;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellingOut extends AppCompatActivity {
    RecyclerView lvb;
    long now;
    List<SellingConstructor> arr;

    EndlessScroll endlessScroll;
    SellingOutAdapter adapter;
    LinearLayoutManager layoutManager;
    TextView nop;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_out);

        lvb = (RecyclerView) findViewById(R.id.lvsell);
        lvb.setHasFixedSize(true);
        arr = new ArrayList<>();

        nop = (TextView) findViewById(R.id.nop8);
        layoutManager = new LinearLayoutManager(this);
        lvb.setLayoutManager(layoutManager);
        adapter = new SellingOutAdapter(this, arr);
        lvb.setAdapter(adapter);
        endlessScroll = new EndlessScroll(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                getTime(page);
            }
        };
        getTime(1);
        lvb.addOnScrollListener(endlessScroll);
        lvb.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                        it.putExtra("idprd", arr.get(position).getIdprd());
                        it.putExtra("sell","true");
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipsell);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void getTime(final int page) {

        final String link = getResources().getString(R.string.linktimenow);
        Response.Listener<String> response = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                     now = jo.getLong("0");
                    getData(page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetCL get = new GetCL(link,response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void getData(int page) {
        final String link = getResources().getString(R.string.linkselling);
        final Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        final Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("aaza", response + "");
                try {
                    JSONArray ja = new JSONArray(response);
                    if (ja.length() == 0) {
                        if (arr.size() == 0) {
                            nop.setVisibility(View.VISIBLE);
                        } else {
                            nop.setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        nop.setVisibility(View.GONE);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j1 = ja.getJSONObject(i);
                            JSONObject prd = j1.getJSONObject("Product");
                            JSONArray imgs = prd.getJSONArray("images");
                            JSONObject vote = prd.getJSONObject("vote");
                            JSONObject  sell = prd.getJSONObject("sellingOut");
                            arr.add(new SellingConstructor("http://needfood.webmantan.com" + imgs.getString(0), prd.getString("id"),
                                    prd.getString("idSeller"),
                                    prd.getString("title"), prd.getString("nameSeller"), sell.getString("price")
                                    , "", prd.getString("price"), vote.getString("point"), prd.getString("nameUnit"),
                                    prd.getString("typeMoneyId"),sell.getLong("timeEnd"),now));
                        }
                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };
        PostCL post = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }

    public void refresh(){
        arr.clear();
        getTime(1);
    }
}
