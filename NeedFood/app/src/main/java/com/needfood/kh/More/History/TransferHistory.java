package com.needfood.kh.More.History;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.TranfHisAdapter;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.TranfConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.ChangeTimestamp;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferHistory extends AppCompatActivity {
    ListView lv;
    int page = 1;
    DataHandle db;
    List<InfoConstructor> list;
    String token;

    long time = 0;
    String mess, coin, idu, id;


    ChangeTimestamp chan;
    List<TranfConstructor> arr;
    TranfHisAdapter adapter;
    TextView nop,tit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        nop = (TextView) findViewById(R.id.nop9);
        tit = (TextView) findViewById(R.id.titletxt);
        tit.setText(getResources().getString(R.string.histran));
        db = new DataHandle(this);
        chan = new ChangeTimestamp();
        lv = (ListView) findViewById(R.id.lvtran);
        list = db.getAllInfor();
        for (InfoConstructor ic : list) {
            token = ic.getAccesstoken();
        }
        arr = new ArrayList<>();
        adapter = new TranfHisAdapter(getApplicationContext(), arr);
        lv.setAdapter(adapter);
        getHisTran();

    }

    public void getHisTran() {
        final ProgressDialog progressDialog = DialogUtils.show(TransferHistory.this, getResources().getString(R.string.wait));
        String link = getResources().getString(R.string.linkhiscoin);
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessToken", token);
        map.put("page", page + "");
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("IMGGA", response);
                    progressDialog.dismiss();

                    JSONArray jo = new JSONArray(response);
                    if (jo.length() == 0) {
                        if (arr.size() == 0) {
                            nop.setVisibility(View.VISIBLE);
                        } else {
                            nop.setVisibility(View.GONE);
                        }
                    } else {
                        nop.setVisibility(View.GONE);
                        Log.d("id", jo.length() + "");
                        for (int i = 0; i < jo.length(); i++) {
                            JSONObject js = jo.getJSONObject(i);
                            JSONObject order = js.getJSONObject("History");
                            id = order.getString("id");
                            time = order.getLong("time");
                            mess = order.getString("mess");
                            coin = order.getString("coin");
                            idu = order.getString("idUseronl");
                            String timedate = chan.getDateCurrentTimeZone(time);
                            arr.add(new TranfConstructor(id, mess, timedate, coin, idu, ""));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        PostCL post = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(),MoreHistory.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
