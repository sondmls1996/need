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
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.MenuBrandConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.PostCL;

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
    String idl;
    List<ListMN> list;
    RecyclerView rc1;
    DataHandle db;
    String money;
    MenuBrandAdapter adapter1,adapter2,adapter3;
    ArrayList<MenuBrandConstructor> arr1,arr2,arr3;
    public MenuBrand() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BrandDetail activity = (BrandDetail) getActivity();
         idl = activity.getMyData();
        db = new DataHandle(getApplicationContext());
        getPRDSell();
        View v = inflater.inflate(R.layout.fragment_menu_brand, container, false);
        rc1 = (RecyclerView)v.findViewById(R.id.rc1);
        arr1 = new ArrayList<>();


        adapter1 = new MenuBrandAdapter(getContext(),arr1);
        rc1.setAdapter(adapter1);
        rc1.setLayoutManager(new LinearLayoutManager(getContext()));




        // Inflate the layout for this fragment
        return v;
    }

    private void getPRDSell() {
        final String link = getResources().getString(R.string.linkprdsell);
        Map<String,String> map = new HashMap<>();
        map.put("idSeller",idl);
        map.put("page",page+"");
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SELLPR",response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0;i<ja.length();i++){
                        JSONObject idx = ja.getJSONObject(i);
                        JSONObject prd = idx.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");
                        String tyemn = prd.getString("typeMoneyId");
                        list = db.getMNid(tyemn);
                        for (ListMN lu:list){
                            money=lu.getMn();
                        }
                        arr1.add(new MenuBrandConstructor(prd.getString("title"),"http://needfood.webmantan.com"+jaimg.getString(0),
                                prd.getString("price"),prd.getString("priceOther"),money,prd.getString("id"),prd.getString("nameUnit")));
                    }
                adapter1.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link,map,response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }


}
