package com.needfood.kh.More.History;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.OrderHisAdapter;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.OrderHisConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistory extends AppCompatActivity {
    DataHandle db;
    List<InfoConstructor> list;
    String token;
    String title, price, tickkm, status, id, fullname, fone, address;
    OrderHisAdapter adapter;
    List<OrderHisConstructor> ls;
    ListView lv;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.hisord));
        db = new DataHandle(getApplicationContext());
        ls = new ArrayList<>();
        list = db.getAllInfor();
        for (InfoConstructor ia : list) {
            token = ia.getAccesstoken();
        }
        lv = (ListView) findViewById(R.id.lvhisorr);
        adapter = new OrderHisAdapter(getApplicationContext(), ls);
        lv.setAdapter(adapter);
        getOrderHistory();
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            int firstVisibleItem, visibleItemCount, totalItemCount;

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
                    page++;
                    getOrderHistory();
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItemm, int visibleItemCountt, int totalItemCountt) {
                firstVisibleItem = firstVisibleItemm;
                visibleItemCount = visibleItemCountt;
                totalItemCount = totalItemCountt;
            }
        });
    }

    public void getOrderHistory() {

        final ProgressDialog progressDialog = DialogUtils.show(OrderHistory.this, getResources().getString(R.string.wait));
        String link = getResources().getString(R.string.linkorhis);
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessToken", token);
        map.put("page", page+"");
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("IMGG",response);
                    progressDialog.dismiss();

                    JSONArray jo = new JSONArray(response);
                    Log.d("id", jo.length() + "");
                    for (int i = 0; i < jo.length(); i++) {
                        JSONObject js = jo.getJSONObject(i);
                        JSONObject order = js.getJSONObject("Order");
                        id = order.getString("id");
                        status = order.getString("status");

                        JSONArray lp = order.getJSONArray("listProduct");
                        Log.d("size", lp.length() + "");
                        for (int j = 0; j < lp.length(); j++) {
                            JSONObject jaa = lp.getJSONObject(j);
                            title = jaa.getString("title");
                            price = jaa.getString("money");

                        }
                        JSONObject ic = order.getJSONObject("infoCustomer");
                        fullname = ic.getString("fullName");
                        fone = ic.getString("fone");
                        address = ic.getString("address");
                        Log.d("ABCCA", id + "-" + fullname + "-" + fone + "-" + address);
                        ls.add(new OrderHisConstructor(id, title, price, status, fullname, fone, address,",",""));
                    }

                    adapter.notifyDataSetChanged();
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
}

