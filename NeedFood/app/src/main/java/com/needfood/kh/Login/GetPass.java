package com.needfood.kh.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetPass extends AppCompatActivity {
    EditText code;
    Button next;
    String fone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pass);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.forget));
        Intent it = getIntent();
        fone = it.getStringExtra("fone");
        code = (EditText) findViewById(R.id.code);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senCode();
            }
        });
    }

    public void senCode() {
        final ProgressDialog pro = DialogUtils.show(GetPass.this, getResources().getString(R.string.wait));
        String xid = code.getText().toString();
        Map<String, String> map = new HashMap<String, String>();
        if (xid.matches("")) {
            pro.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
        } else {
            map.put("fone", fone);
            map.put("codeForgetPass", xid);
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
                            Intent i = new Intent(GetPass.this, StartActivity.class);
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
            PostCL glq = new PostCL(getResources().getString(R.string.linktypecode), map, response);
            RequestQueue qe = Volley.newRequestQueue(getApplication());
            qe.add(glq);
        }
    }
}
