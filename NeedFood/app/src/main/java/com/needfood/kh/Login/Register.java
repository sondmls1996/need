package com.needfood.kh.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Register extends AppCompatActivity {
    TextView tvname, tvfone, tvmail, tvpass, tvpass2, tvadr;
    Button btnokay;
    String fullname,email,adr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.regis));
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String regexStr = "^[0-9]$";
        tvname = (TextView) findViewById(R.id.tvfn);
        tvfone = (TextView) findViewById(R.id.tvf);
        tvmail = (TextView) findViewById(R.id.tvem);
        tvpass = (TextView) findViewById(R.id.tvp);
        tvpass2 = (TextView) findViewById(R.id.tvpa);
        tvadr = (TextView) findViewById(R.id.tvadr);
        btnokay = (Button) findViewById(R.id.btnreg);
        btnokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = DialogUtils.show(Register.this, getResources().getString(R.string.wait));

                String name = tvname.getText().toString();
                String fone = tvfone.getText().toString();
                String mail = tvmail.getText().toString();
                String pass = tvpass.getText().toString();
                String pass2 = tvpass2.getText().toString();
                String adr = tvadr.getText().toString();
                String link = getResources().getString(R.string.linkreg);
                Map<String, String> map = new HashMap<String, String>();

                if (name.matches("") || fone.matches("") || mail.matches("") || pass.matches("") || pass2.matches("") || adr.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
                } else if (fone.length() < 10 || fone.length() > 13 || fone.matches(regexStr) == false) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.checkfon), Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(pass2)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrpass), Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.shortpass), Toast.LENGTH_SHORT).show();
                } else if (!mail.matches(emailPattern)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailwr), Toast.LENGTH_SHORT).show();
                } else {

                    map.put("fullName", name);
                    map.put("email", mail);
                    map.put("fone", fone);
                    map.put("pass", pass);
                    map.put("passAgain", pass2);
                    map.put("address", adr);
                    Response.Listener<String> response = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString("code");
                                if (code.equals("0")) {
                                    progressDialog.dismiss();
                                    Intent it = new Intent(getApplicationContext(), Login.class);
                                    startActivity(it);
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();

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

            }
        });

    }
}
