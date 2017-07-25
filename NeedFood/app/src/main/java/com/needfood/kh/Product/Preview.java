package com.needfood.kh.Product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Adapter.PreAdapter;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.PreConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Preview extends AppCompatActivity {
    String json,mid;
    RecyclerView lv;
    ArrayList<PreConstructor> arr;
    DataHandle dataHandle;
    List<ListMN> list;
    PreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.conor));
        dataHandle = new DataHandle(this);
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv = (RecyclerView) findViewById(R.id.lvpre);
        arr = new ArrayList<>();
        adapter = new PreAdapter(getApplicationContext(),arr);
        lv.setAdapter(adapter);
        lv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        json = hashMap.get("listProduct");
        mid = intent.getStringExtra("min");
        try {
            JSONArray jo = new JSONArray(json);
            for (int i =0; i<jo.length();i++){
                JSONObject idx = jo.getJSONObject(i);

                arr.add(new PreConstructor(idx.getString("title"),idx.getString("quantity"),idx.getString("money"),mid));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
