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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.HotdealAdapter;
import com.needfood.kh.Constructor.HotdealConstructor;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.Login;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.EndlessScroll;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.RecyclerItemClickListener;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Hotdeal extends AppCompatActivity implements View.OnClickListener {
    Button btnlog;
    Session ses;
    DataHandle db;
    View v;
    ListView lv;
    RecyclerView lvb;

    ArrayList<HotdealConstructor> arr;
    HotdealAdapter adapter;
    LinearLayoutManager layoutManager;
    String token;
    List<InfoConstructor> list;
    EndlessScroll endlessScroll;
    TextView nop;
    SwipeRefreshLayout swipeRefreshLayout;

    public Hotdeal() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ses = new Session(this);


        if (ses.loggedin()) {
            setContentView(R.layout.activity_hotdeal);
            db = new DataHandle(this);
            list = db.getAllInfor();
            for (InfoConstructor ic : list) {
                token = ic.getAccesstoken();
            }
            arr = new ArrayList<>();
            nop = (TextView) findViewById(R.id.nop6);
            lvb = (RecyclerView) findViewById(R.id.lvhostdeal);
            lvb.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getApplicationContext());
            lvb.setLayoutManager(layoutManager);
            adapter = new HotdealAdapter(getApplicationContext(), arr);
            lvb.setAdapter(adapter);
            endlessScroll = new EndlessScroll(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    page++;
                    getData(page);
                }
            };
            getData(1);
            lvb.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                            it.putExtra("idprd", arr.get(position).getIdprd());
                            it.putExtra("namesel", arr.get(position).getNameauth());
                            it.putExtra("idsel", arr.get(position).getIdsl());
                            startActivity(it);
                            // TODO Handle item click
                        }
                    })
            );
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout1);
            swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });
        } else {
            setContentView(R.layout.fragment_frag_log);
            btnlog = (Button) findViewById(R.id.btnlog);
            btnlog.setOnClickListener(this);


        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnlog:
                Intent it3 = new Intent(getApplicationContext(), Login.class);
                startActivity(it3);
                break;
        }
    }
    private AlertDialog taoMotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Thiết lập tiêu đề hiển thị
        builder.setTitle(getResources().getString(R.string.er));
        //Thiết lập thông báo hiển thị

        builder.setMessage(getResources().getString(R.string.lostss));
        builder.setCancelable(false);
        //Tạo nút Chu hang
        builder.setNegativeButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        db.deleteInfo();
                        ses = new Session(getBaseContext());
                        ses.setLoggedin(false);
                        Intent i = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
    private void getData(int page) {
        final String link = getResources().getString(R.string.linkhotdeal);
        final Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("page", page + "");
        final Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("aa", response + "");
                try {
                    if(response.equals("{\"code\":-1}")){
                        swipeRefreshLayout.setRefreshing(false);
                        AlertDialog alertDialog = taoMotAlertDialog();
                        alertDialog.show();
                    }else{
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


                                arr.add(new HotdealConstructor("http://needfood.webmantan.com" + imgs.getString(0), prd.getString("id"),
                                        prd.getString("idSeller"),
                                        prd.getString("title"), prd.getString("nameSeller"), prd.getString("price")
                                        , "", prd.getString("priceOther"), prd.getString("vote"), prd.getString("nameUnit"), prd.getString("typeMoneyId"), prd.getString("numberShare")));

                            }
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
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

    public void refresh() {
        arr.clear();
        getData(1);
    }
}
