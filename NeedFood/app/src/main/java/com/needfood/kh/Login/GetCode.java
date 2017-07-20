package com.needfood.kh.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetCode extends AppCompatActivity {
    Button btnnext;
    EditText ed1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        btnnext = (Button) findViewById(R.id.btnn1);
        ed1 = (EditText) findViewById(R.id.edphonenum);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPass();
            }
        });
    }

    public void ForgetPass() {
        final ProgressDialog pro = DialogUtils.show(GetCode.this, getResources().getString(R.string.wait));
        String xid = ed1.getText().toString();
        Map<String, String> map = new HashMap<String, String>();
        if (xid.matches("")) {
            pro.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
        } else {
            map.put("fone", xid);
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("code");
                        Log.d("LOGr",code);
                        if (code.equals("0")) {
                            pro.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(GetCode.this, GetPass.class);
                            i.putExtra("fone", ed1.getText().toString());
                            startActivity(i);
                            finish();
                        } else {
                            pro.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        pro.dismiss();
                        e.printStackTrace();
                    }
                }
            };
            PostCL glq = new PostCL(getResources().getString(R.string.linkgetcode), map, response);
            RequestQueue qe = Volley.newRequestQueue(getApplicationContext());
            qe.add(glq);
        }
    }
}
