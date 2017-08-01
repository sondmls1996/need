package com.needfood.kh.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.GetCL;

public class DetailPolicy extends AppCompatActivity {
    String stt;
    String link;
    TextView txtpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_policy);
        Intent it = getIntent();
        txtpl = (TextView)findViewById(R.id.txtpoly);
        stt = it.getStringExtra("stt");
        if(stt.equals("u")){
            link = getResources().getString(R.string.linkroleuser);
        }else{
            link = getResources().getString(R.string.linkrolesell);
        }
        getRole();
    }

    private void getRole() {
        Response.Listener<String> response = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                txtpl.setText(Html.fromHtml(Html.fromHtml(response).toString()));
            }
        };
        GetCL get = new GetCL(link,response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }
}
