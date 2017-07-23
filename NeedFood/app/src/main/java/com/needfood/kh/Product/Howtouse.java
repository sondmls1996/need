package com.needfood.kh.Product;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Howtouse extends AppCompatActivity {
    String idprd, idsl, namesl, access, idu, fullname, phone, bar, cata;
    String priceprd, prdcode, titl;
    String maniid, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);

        Intent intent = getIntent();
        idprd = intent.getStringExtra("idp");
        getProductDT();
    }

    private void getProductDT() {
        final String link = getResources().getString(R.string.linkprdde);
        Map<String, String> map = new HashMap<>();
        map.put("idProduct", idprd);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("PDTR", response);

                    JSONObject jo = new JSONObject(response);
                    JSONObject prd = jo.getJSONObject("Product");
                    cata = prd.getJSONArray("category").toString();
                    String tym = prd.getString("typeMoneyId");
                    String dvs = prd.getString("nameUnit");
                    titl = prd.getString("title");
                    namesl = prd.getString("nameSeller");
                    prdcode = prd.getString("code");
                    bar = prd.getString("barcode");
                    priceprd = prd.getString("price");
                    idsl = prd.getString("idSeller");
                    maniid = prd.getString("manufacturerId");
                    info = prd.getString("info");
                    String plantext = String.valueOf(Html.fromHtml(Html.fromHtml(info).toString()));
                    Log.d("PDTR", plantext);
//                    list = db.getMNid(tym);
//                    for (ListMN lu : list) {
//                        tvgia1.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("price"))) + lu.getMn());
//                        tvgia2.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("priceOther"))) + lu.getMn(), TextView.BufferType.SPANNABLE);
//                        Spannable spannable = (Spannable) tvgia2.getText();
//                        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, tvgia2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        tvprize.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("price"))) + lu.getMn());
//                    }
//                    tvco.setText(prd.getString("code"));
//                    tvcodes.setText(prd.getString("storeID"));
//
//                    dess.setText(prd.getString("description"));
                    JSONArray ja = prd.getJSONArray("images");
//                    Picasso.with(getApplicationContext()).load("http://needfood.webmantan.com" + ja.getString(0)).into(imgprd);
//
//                    getPrdDK();
//                    getCommen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.howtou));
    }


}
