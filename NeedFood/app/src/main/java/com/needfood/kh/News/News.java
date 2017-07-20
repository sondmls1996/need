package com.needfood.kh.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.NewsAdapter;
import com.needfood.kh.Constructor.NewsConstructor;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
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

    int page = 1;
    LinearLayoutManager layoutManager;
    NewsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        lv = (RecyclerView)findViewById(R.id.lvnew);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv.setHasFixedSize(true);
        arr = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        lv.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(this, arr);
        lv.setAdapter(adapter);

        getData();
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                        it.putExtra("idprd",arr.get(position).getIdprd());

                        startActivity(it);

                        // TODO Handle item click
                    }
                })
        );
  /*      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                it.putExtra("idprd",arr.get(i).getIdprd());
                startActivity(it);
            }
        });*/
     /*   lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (absListView.getId() == lv.getId()) {
                    final int currentFirstVisibleItem = lv.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        getSupportActionBar().hide();
                        getSupportActionBar().hide();
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        // getSherlockActivity().getSupportActionBar().show();
                        getSupportActionBar().show();
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }
        });*/

    }

    private void getData() {
        final String link = getResources().getString(R.string.linknew);
        final Map<String,String> map = new HashMap<>();
        map.put("page",page+"");
        final Response.Listener<String> response= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("aa",response+"");
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i =0;i<ja.length();i++){
                        JSONObject j1 = ja.getJSONObject(i);
                        JSONObject prd = j1.getJSONObject("Product");
                        JSONArray imgs = prd.getJSONArray("images");
                        arr.add(new NewsConstructor("http://needfood.webmantan.com"+imgs.getString(0),prd.getString("id"),
                                prd.getString("idSeller"),
                                prd.getString("title"),prd.getString("nameSeller"),prd.getString("price")
                                ,"",prd.getString("priceOther"),prd.getString("vote"),prd.getString("nameUnit"),
                                prd.getString("typeMoneyId")));
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }



            }

        };
        PostCL post = new PostCL(link,map,response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }



}
