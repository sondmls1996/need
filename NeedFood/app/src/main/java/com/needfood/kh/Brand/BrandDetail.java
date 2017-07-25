package com.needfood.kh.Brand;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.ViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BrandDetail extends AppCompatActivity {
    TabLayout tabLayout;
    TextView tvname;
    ViewPager viewPager;
    ViewAdapter viewAdapter;
    String idsl;
    public static String idsel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_detail);
        Intent it = getIntent();
        idsl = it.getStringExtra("ids");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvname = (TextView) findViewById(R.id.brname);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest);
        scrollView.setFillViewport(true);


        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.vpage);
        viewAdapter = new ViewAdapter(getSupportFragmentManager());
        viewAdapter.addFragment(new MenuBrand(), getResources().getString(R.string.menu));

        viewAdapter.addFragment(new CommentBrand(), getResources().getString(R.string.comment));

        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(viewPager);
        viewAdapter.notifyDataSetChanged();

        viewAdapter.notifyDataSetChanged();
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.red));
        getBrand();

    }

    private void getBrand() {
        final String link = getResources().getString(R.string.linkifsel);
        Map<String, String> map = new HashMap<>();
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SLL", response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject sl = jo.getJSONObject("Seller");
                    tvname.setText(sl.getString("branchName"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    public String getMyData() {
        return idsl;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
