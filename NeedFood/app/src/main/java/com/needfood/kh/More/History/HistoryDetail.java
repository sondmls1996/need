package com.needfood.kh.More.History;

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
import java.util.List;

public class HistoryDetail extends AppCompatActivity {
    String js,mn;
    RecyclerView rchis;
    ArrayList<PreConstructor> arr;
    List<ListMN> listmn;
    DataHandle db;
    PreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.orif));
        db = new DataHandle(this);
        Intent it = getIntent();
        js = it.getStringExtra("js");
        rchis = (RecyclerView)findViewById(R.id.rchgis);
        arr = new ArrayList<>();
        adapter = new PreAdapter(getApplicationContext(),arr);
        rchis.setAdapter(adapter);
        rchis.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        try {
            JSONObject jo = new JSONObject(js);
            JSONArray list = jo.getJSONArray("listProduct");
            for (int i=0;i<list.length();i++){
                JSONObject idx = list.getJSONObject(i);
                listmn = db.getMNid(idx.getString("typeMoneyId"));
                for (ListMN lu:listmn){
                    mn=lu.getMn();
                }
                arr.add(new PreConstructor(idx.getString("title"), idx.getString("quantity"), idx.getString("money"), mn));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}
