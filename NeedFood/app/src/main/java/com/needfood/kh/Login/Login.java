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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    LoginButton lgb;
    CallbackManager callbackManager;
    Button lg;
    TextView tvreg,tvfor;
    Session ses;
    EditText edus, edpass;
    DataHandle db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DataHandle(getApplicationContext());
        ses = new Session(getApplicationContext());
        edus = (EditText) findViewById(R.id.edus);
        edpass = (EditText) findViewById(R.id.edpas);
        lgb = (LoginButton) findViewById(R.id.login_button);
        lg = (Button) findViewById(R.id.btnlg);
        tvreg = (TextView) findViewById(R.id.tvreg);
        edus = (EditText)findViewById(R.id.edus);
        edpass = (EditText)findViewById(R.id.edpas);
        lgb = (LoginButton)findViewById(R.id.login_button);
        tvfor = (TextView)findViewById(R.id.tvfogot);
        tvfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),GetCode.class);
                startActivity(it);
            }
        });
        lg = (Button)findViewById(R.id.btnlg);
        tvreg = (TextView)findViewById(R.id.tvreg);
        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), Register.class);
                startActivity(it);
            }
        });
        callbackManager = CallbackManager.Factory.create();
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = DialogUtils.show(Login.this, getResources().getString(R.string.wait));

                String phone = edus.getText().toString();
                String pass = edpass.getText().toString();
                String link = getResources().getString(R.string.linklogin);
                Log.d("LOGr", phone + "-" + pass);
                Map<String, String> map = new HashMap<String, String>();
                if (phone.matches("") || pass.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
                } else {
                    map.put("fone", phone);
                    map.put("pass", pass);

                    Response.Listener<String> response = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("LOGr", response);
                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString("code");
                                if (code.equals("0")) {
                                    progressDialog.dismiss();
                                    ses.setLoggedin(true);
                                    JSONObject js = jo.getJSONObject("Useronl");
                                    String fullname = js.getString("fullName");
                                    String email = js.getString("email");
                                    String fone = js.getString("fone");
                                    String pass = js.getString("pass");
                                    String address = js.getString("address");
                                    String id = js.getString("id");
                                    String accesstoken = js.getString("accessToken");

                                    db.addInfo(new InfoConstructor(fullname, email, fone, pass, address, id, accesstoken));
                                    Intent it = new Intent(getApplicationContext(), StartActivity.class);
                                    startActivity(it);
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();

                                }
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
        });

        lgb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ses.setLoggedin(true);
                Intent it = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(it);
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
