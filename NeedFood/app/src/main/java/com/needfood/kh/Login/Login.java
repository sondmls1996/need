package com.needfood.kh.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    LoginButton lgb;
    CallbackManager callbackManager;
    Button lg;
    TextView tvreg, tvfor;
    Session ses;
    EditText edus, edpass;
    DataHandle db;
    String fullname, idfb, email = "", fone = "", adr = "";
    String dvtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
        dvtoken = sharedPreferences.getString(getString(R.string.F_CM), "");
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.login));
        db = new DataHandle(getApplicationContext());
        ses = new Session(getApplicationContext());
        edus = (EditText) findViewById(R.id.edus);
        edpass = (EditText) findViewById(R.id.edpas);
        lgb = (LoginButton) findViewById(R.id.login_button);

        lgb.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_location"));
        tvfor = (TextView) findViewById(R.id.tvfogot);
        tvfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), GetCode.class);
                startActivity(it);
            }
        });
        lg = (Button) findViewById(R.id.btnlg);
        tvreg = (TextView) findViewById(R.id.tvreg);
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

                final String phone = edus.getText().toString();
                final String pass = edpass.getText().toString();
                String link = getResources().getString(R.string.linklogin);

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
                                    String id = js.getString("id");
                                    String ava = js.getString("avatar");
                                    String accesstoken = js.getString("accessToken");
                                    postToken(accesstoken);
                                    addInfo(accesstoken, id, "0");
                                    Intent it = new Intent(getApplicationContext(), StartActivity.class);
                                    startActivity(it);
                                    finish();
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
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                final JSONObject json = response.getJSONObject();
                                Log.d("JSRE", response.toString());

                                try {
                                    fullname = json.getString("name");
                                    idfb = json.getString("id");
                                    if (json.has("location")) {
                                        JSONObject jw = json.getJSONObject("location");
                                        adr = jw.getString("name");
                                    }
                                    if (json.has("email")) {
                                        email = json.getString("email");
                                    }

//
                                    regisFB();
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,location");
                request.setParameters(parameters);
                request.executeAsync();
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

    private void regisFB() {
        final ProgressDialog progressDialog = DialogUtils.show(Login.this, getResources().getString(R.string.wait));
        String linkk = getResources().getString(R.string.linklogfb);
        Map<String, String> map = new HashMap<>();
        map.put("fullName", fullname);
        map.put("idFacebook", idfb);
        map.put("email", email);
        map.put("fone", fone);
        map.put("address", adr);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    progressDialog.dismiss();
                    ses.setLoggedin(true);
                    JSONObject jo = new JSONObject(response);
                    JSONObject js = jo.getJSONObject("Useronl");
//                                    String fullname = jo.getString("fullName");
//                                    String email = jo.getString("email");
//                                    String fone = jo.getString("fone");
//                                    String address = jo.getString("address");
//                                    String coin = jo.getString("coin");
                    String id = js.getString("id");
                    String accesstoken = js.getString("accessToken");
//                                    String pass = jo.getString("pass");

                    postToken(accesstoken);

                    addInfo(accesstoken, id, "1");
                    Intent it = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(it);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }


    private void addInfo(final String token, final String id, final String type) {
        String linkk = getResources().getString(R.string.linkgetinfo);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("idUseronl", id);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("LOGA", response);
                    JSONObject js = new JSONObject(response);
                    JSONObject jo = js.getJSONObject("Useronl");
                    String fullname = jo.getString("fullName");
                    String email = jo.getString("email");
                    String fone = jo.getString("fone");
                    String address = jo.getString("address");
                    String coin = jo.getString("coin");

                    Log.d("ABCLOG", token + "-" + id + "-" + fullname + "-" + email + "-" + fone + "-" + address + "-" + coin);
                    db.addInfo(new InfoConstructor(fullname, email, fone, "", address, id, token, coin, type));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);

    }

    private void postToken(String tokent) {
        Log.d("LOGr", dvtoken);
        String linkk = getResources().getString(R.string.linksavetoken);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", tokent);
        map.put("tokenDevice", dvtoken);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    if (code.equals("0")) {

                    } else {
                        Toast.makeText(getApplication(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
