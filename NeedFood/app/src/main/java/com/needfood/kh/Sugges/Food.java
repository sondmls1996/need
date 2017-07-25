package com.needfood.kh.Sugges;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class Food extends AppCompatActivity {
    NewsAdapter adapter;
    ArrayList<NewsConstructor> arr;
    RecyclerView lv;
    LinearLayoutManager layoutManager;
    int page = 1;
    TextView nop;
    EndlessScroll endlessScroll;
    String namep="food";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        lv = (RecyclerView)findViewById(R.id.lvfood);
        nop = (TextView)findViewById(R.id.nop3);
        arr=new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        lv.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(getApplicationContext(),arr);
        lv.setAdapter(adapter);
        endlessScroll = new EndlessScroll(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                getData(page);
            }
        };
        getData(page);

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

    }
    private void getData(int page) {
        final String link = getResources().getString(R.string.linksug);
        final Map<String,String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("suggestion",namep);
        final Response.Listener<String> response= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("sugest",response);
                    JSONArray ja = new JSONArray(response);
                    if(ja.length()==0){
                        if(arr.size()==0){
                            nop.setVisibility(View.VISIBLE);
                        }else{
                            nop.setVisibility(View.GONE);
                        }

                    }else {
                        nop.setVisibility(View.GONE);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j1 = ja.getJSONObject(i);
                            JSONObject prd = j1.getJSONObject("Product");
                            JSONArray imgs = prd.getJSONArray("images");
                            arr.add(new NewsConstructor("http://needfood.webmantan.com" + imgs.getString(0), prd.getString("id"),
                                    prd.getString("idSeller"),
                                    prd.getString("title"), prd.getString("nameSeller"), prd.getString("price")
                                    , "", prd.getString("priceOther"), prd.getString("vote"), prd.getString("nameUnit"), prd.getString("typeMoneyId")));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };
        PostCL post = new PostCL(link,map,response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }
}
