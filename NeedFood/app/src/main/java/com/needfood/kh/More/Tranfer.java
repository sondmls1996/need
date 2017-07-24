package com.needfood.kh.More;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
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
import java.util.List;
import java.util.Map;

public class Tranfer extends AppCompatActivity {
    Button btnsend;
    Session ses;
    EditText edphone, edcoin, ednote;
    DataHandle db;
    List<InfoConstructor> list;
    String acc, coinn, idu;
    Class fragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.tranhis));
        db = new DataHandle(this);
        list = db.getAllInfor();
        acc = list.get(list.size() - 1).getAccesstoken();
        idu = list.get(list.size() - 1).getId();
        addInfo();
        btnsend = (Button) findViewById(R.id.btnsend);
        edphone = (EditText) findViewById(R.id.edrephone);
        edcoin = (EditText) findViewById(R.id.edcoin);
        ednote = (EditText) findViewById(R.id.edcont);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCoin();
            }
        });

    }

    public void ReplaceFrag(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addInfo();
    }

    private void sendCoin() {
        String phone = edphone.getText().toString();
        String coin = edcoin.getText().toString();
        String note = ednote.getText().toString();
        String link = getResources().getString(R.string.linktranf);
        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        if (phone.equals("") || coin.equals("")) {
            pro.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(coin) > Double.parseDouble(coinn)) {
            pro.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.can), Toast.LENGTH_SHORT).show();
        } else

        {
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", acc);
            map.put("coin", coin);
            map.put("fone", phone);
            map.put("note", note);
            Response.Listener<String> response = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("COIN", response);
                    try {
                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString("code");
                        pro.dismiss();
                        if (code.equals("0")) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.transuc), Toast.LENGTH_SHORT).show();
                        } else if (code.equals("-1")) {
                            AlertDialog alertDialog = taoMotAlertDialog();
                            alertDialog.show();
                        } else {
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

    private void addInfo() {
        String linkk = getResources().getString(R.string.linkgetinfo);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", acc);
        map.put("idUseronl", idu);
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
                    coinn = jo.getString("coin");
                    db.updateinfo(fullname, email, address, idu, coinn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);

    }

    private AlertDialog taoMotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Thiết lập tiêu đề hiển thị
        builder.setTitle(getResources().getString(R.string.er));
        //Thiết lập thông báo hiển thị

        builder.setMessage(getResources().getString(R.string.lostss));
        builder.setCancelable(false);
        //Tạo nút Chu hang
        builder.setNegativeButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        db.deleteInfo();
                        ses = new Session(getBaseContext());
                        ses.setLoggedin(false);
                        Intent i = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
