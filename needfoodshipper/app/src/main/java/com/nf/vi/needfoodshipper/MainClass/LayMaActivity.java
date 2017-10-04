package com.nf.vi.needfoodshipper.MainClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.LayMaRepuest;


import org.json.JSONException;
import org.json.JSONObject;

public class LayMaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtsdtlm;
    private Button btnNhanMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_ma);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));

        //     }
        edtsdtlm = (EditText) findViewById(R.id.edtsdtlm);
        btnNhanMa = (Button) findViewById(R.id.btnNhanMa);

        btnNhanMa.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnNhanMa){
            String sdt = edtsdtlm.getText().toString();
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("code");
                        //  Log.d("ABCC", code);
                        if (code.equals("0")) {
                            Toast.makeText(getApplicationContext(), "Gửi thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(), SentPassEmail.class);
                            i.putExtra("fone", edtsdtlm.getText().toString());
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            LayMaRepuest glq = new LayMaRepuest(sdt, getResources().getString(R.string.sendCodePassShiperAPI), response);
            RequestQueue qe = Volley.newRequestQueue(getApplication());
            qe.add(glq);
        }

    }
}
