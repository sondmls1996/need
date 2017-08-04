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
import android.support.v7.widget.Toolbar;
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
import java.util.HashMap;
import java.util.Map;

public class News extends AppCompatActivity {
    RecyclerView lv;
    ArrayList<NewsConstructor> arr;
    EndlessScroll endlessScroll;
    TextView nop;
    int check = 0;
    LinearLayoutManager layoutManager;

    NewsAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        lv = (RecyclerView) findViewById(R.id.lvnew);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv.setHasFixedSize(true);
        arr = new ArrayList<>();
        nop = (TextView) findViewById(R.id.nop5);
        layoutManager = new LinearLayoutManager(this);
        lv.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(this, arr);
        lv.setAdapter(adapter);
        endlessScroll = new EndlessScroll(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                getData(page);
            }
        };
        lv.addOnScrollListener(endlessScroll);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        getData(1);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                        it.putExtra("idprd", arr.get(position).getIdprd());
                        it.putExtra("statuss","new");
                        startActivity(it);

                        // TODO Handle item click
                    }
                })
        );
//   lv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//       @Override
//       public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//           if (scrollY > oldScrollY) {
//               StartActivity.toolbar.setVisibility(View.VISIBLE);
//           }
//           if (scrollY < oldScrollY) {
//               StartActivity.toolbar.setVisibility(View.GONE);
//           }
//       }
//   });

    }

    private void getData(int page) {

        final String link = getResources().getString(R.string.linknew);
        final Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        final Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("aa", response + "");
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
//                            JSONObject votec = vote.getJSONObject("user");
                            arr.add(new NewsConstructor("http://needfood.webmantan.com" + imgs.getString(0), prd.getString("id"),
                                    prd.getString("idSeller"),
                                    prd.getString("title"), prd.getString("nameSeller"), prd.getString("price")
                                    , "", prd.getString("priceOther"), vote.getString("point"), prd.getString("nameUnit"),
                                    prd.getString("typeMoneyId")));
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }

        };
        PostCL post = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }

    private void refresh() {
        arr.clear();
        getData(1);
    }

    @Override
    public void onBackPressed() {
        check++;
        if (check < 2) {
            Toast.makeText(getBaseContext(), "Nhấn 2 lần để thoát", Toast.LENGTH_SHORT).show();
        } else if (check >= 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(News.this);
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
                            System.exit(1);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {

        }
    }
}
