package com.needfood.kh.News;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.NewsAdapter;
import com.needfood.kh.Constructor.NewsConstructor;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.EndlessScroll;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestQuality extends AppCompatActivity {
    RecyclerView lvb;

    List<NewsConstructor> arr;
    List<NewsConstructor> arr1;
    int check = 0;
    EndlessScroll endlessScroll;
    NewsAdapter adapter;
    LinearLayoutManager layoutManager;
    TextView nop;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_quality);
        lvb = (RecyclerView) findViewById(R.id.lvbest);
        lvb.setHasFixedSize(true);
        arr = new ArrayList<>();
        arr1 = new ArrayList<>();
        nop = (TextView) findViewById(R.id.nop7);
        layoutManager = new LinearLayoutManager(this);
        lvb.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(this, arr);
        lvb.setAdapter(adapter);
        endlessScroll = new EndlessScroll(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                getData(page);
            }
        };
        getData(1);
        lvb.addOnScrollListener(endlessScroll);
        lvb.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                        it.putExtra("idprd", arr.get(position).getIdprd());
                        it.putExtra("namesel", arr.get(position).getNameauth());
                        it.putExtra("idsel", arr.get(position).getIdsl());
                        it.putExtra("statuss","new");
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout2);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void getData(int page) {
        final String link = getResources().getString(R.string.linkbest);
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
                    } else {
                        nop.setVisibility(View.GONE);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j1 = ja.getJSONObject(i);
                            JSONObject prd = j1.getJSONObject("Product");
                            JSONArray imgs = prd.getJSONArray("images");
                            JSONObject vote = prd.getJSONObject("vote");

                            arr.add(new NewsConstructor("http://needfood.webmantan.com" + imgs.getString(0), prd.getString("id"),
                                    prd.getString("idSeller"),
                                    prd.getString("title"), prd.getString("nameSeller"), prd.getString("price")
                                    , "", prd.getString("priceOther"), vote.getString("point"), prd.getString("nameUnit"),
                                    prd.getString("typeMoneyId")));
                        }
                        adapter.notifyDataSetChanged();
                        sapxep();
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

    public void sapxep() {
        Collections.sort(arr, new Comparator<NewsConstructor>() {
            @Override
            public int compare(NewsConstructor o1, NewsConstructor o2) {
                if (Float.parseFloat(o1.getVt()) < Float.parseFloat(o2.getVt())) {
                    return 1;
                } else {
                    if (Float.parseFloat(o1.getVt()) == Float.parseFloat(o2.getVt())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        check++;
        if (check < 2) {
            Toast.makeText(getBaseContext(), "Nhấn 2 lần để thoát", Toast.LENGTH_SHORT).show();
        } else if (check >= 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BestQuality.this);
            alertDialogBuilder.setTitle("Needfood");
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
                            check = 0;
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {

        }
    }
    public void refresh(){
        arr.clear();
        getData(1);
    }

}
