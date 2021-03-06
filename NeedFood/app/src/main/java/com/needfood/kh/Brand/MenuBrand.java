package com.needfood.kh.Brand;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.MenuBrandAdapter;
import com.needfood.kh.Adapter.ProductDetail.CheckConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.EndlessScroll;
import com.needfood.kh.SupportClass.GetCL;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuBrand extends Fragment {
    int page = 1;
    String idl,access;
    List<ListMN> list;

    RecyclerView rc1;
    LinearLayoutManager layoutManager;
    DataHandle db;
    long timeend = 0,now = 0;
    String tyemn;
    String money,idpr,pricesel,tax,tym,mnn;
    Session ses;
    EndlessScroll endlessScroll;
    MenuBrandAdapter adapter1, adapter2, adapter3;
    ArrayList<OftenConstructor> arr1, arr2, arr3;
    List<CheckConstructor> listcheck;
    public MenuBrand() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BrandDetail activity = (BrandDetail) getActivity();
        ses = new Session(getContext());
        idl = activity.getMyData();
        tax = activity.getTax();
        db = new DataHandle(getApplicationContext());
        getTime();
        getPRDSell(1);
        View v = inflater.inflate(R.layout.fragment_menu_brand, container, false);
        rc1 = (RecyclerView) v.findViewById(R.id.rc1);
        arr1 = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        idpr = BrandDetail.idprd;
        adapter1 = new MenuBrandAdapter(getContext(), arr1);
        rc1.setAdapter(adapter1);
        rc1.setLayoutManager(layoutManager);

        endlessScroll = new EndlessScroll(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                getPRDSell(page);
            }
        };
        rc1.addOnScrollListener(endlessScroll);
        // Inflate the layout for this fragment
        return v;
    }

    private void getPRDSell(int mypg) {
        final String link = getResources().getString(R.string.linkprdsell);
        Map<String, String> map = new HashMap<>();
        map.put("idSeller", idl);
        map.put("page", mypg + "");
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SELLPR", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject idx = ja.getJSONObject(i);
                        JSONObject prd = idx.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");
                         tyemn = prd.getString("typeMoneyId");

                        pricesel = prd.getString("price");
                        if(prd.has("sellingOut")){
                            JSONObject jsel = prd.getJSONObject("sellingOut");
                            timeend = jsel.getLong("timeEnd");
                            if(timeend-now>0){
                                pricesel = jsel.getString("price");
                            }

                        }
                        list = db.getMNid(tyemn);
                        for (ListMN lu : list) {
                            money = lu.getMn();
                        }
                        arr1.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                pricesel, money, prd.getString("nameUnit"), false, prd.getString("id"), prd.getString("code"),
                                "", prd.getString("id"), prd.getString("moneyShip"),tyemn ,timeend,now));

                    }
                    adapter1.notifyDataSetChanged();
                    getupdateSellerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void getupdateSellerView() {
        final String link = getResources().getString(R.string.linkupdateViewSellerAPI);
        Map<String, String> map = new HashMap<>();
        map.put("idSeller", idl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    Log.d("CODEEEE", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void getTime() {

        final String link = getResources().getString(R.string.linktimenow);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    now = jo.getLong("0");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetCL get = new GetCL(link, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

}
