package com.needfood.kh.More;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.ChangePass;
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

import static com.needfood.kh.R.id.proper;

public class MoreContanct extends AppCompatActivity implements View.OnClickListener {

    TextView change, changepass, save,pro;

    EditText name_id, email_id, phone_id, pay, tvName, addr;
    DataHandle db;
    String id_name, email, phone, coin;
    List<InfoConstructor> list;
    String fname, fone, address, id, token, pass;
    Session ses;
    Class fragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_contanct);
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.yourin));
        db = new DataHandle(getApplicationContext());
        ses = new Session(getApplicationContext());
        list = db.getAllInfor();
        for (InfoConstructor it : list) {
            id = it.getId();
            token = it.getAccesstoken();
            fname = it.getFullname();
            fone = it.getFone();
            address = it.getAddress();
            email = it.getEmail();

        }
        tvName = (EditText) findViewById(R.id.mor_name);
        name_id = (EditText) findViewById(R.id.acc_id);
        email_id = (EditText) findViewById(R.id.email_id);
        phone_id = (EditText) findViewById(R.id.fone_id);

        pro = (TextView) findViewById(proper);
        change = (TextView) findViewById(R.id.change);
        save = (TextView) findViewById(R.id.save);

        changepass = (TextView) findViewById(R.id.changepass);
        addr = (EditText) findViewById(R.id.address_id);
        addInfo();
        name_id.setText(id);
        tvName.setText(fname);
        email_id.setText(email);
        phone_id.setText(fone);
        addr.setText(address);

        tvName.setEnabled(false);
        name_id.setEnabled(false);
        email_id.setEnabled(false);
        phone_id.setEnabled(false);
        addr.setEnabled(false);
        change.setOnClickListener(this);
        changepass.setOnClickListener(this);
        save.setOnClickListener(this);
        save.setVisibility(View.GONE);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change:
                change.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                tvName.setEnabled(true);
                email_id.setEnabled(true);
                addr.setEnabled(true);
                break;
            case R.id.save:
                save.setVisibility(View.GONE);
                change.setVisibility(View.VISIBLE);
                final ProgressDialog progressDialog = DialogUtils.show(MoreContanct.this, getResources().getString(R.string.wait));
                final String namee = tvName.getText().toString();
                final String emaill = email_id.getText().toString();
                final String addresss = addr.getText().toString();
                String link = getResources().getString(R.string.linkupdate);
                Map<String, String> map = new HashMap<String, String>();
                if (namee.matches("") || emaill.matches("") || addresss.matches("")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
                } else {
                    map.put("accessToken", token);
                    map.put("fullName", namee);
                    map.put("email", emaill);
                    map.put("address", addresss);
                    Response.Listener<String> response = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString("code");
                                if (code.equals("0")) {
                                    db.updateinfo(namee, emaill, addresss, id, "");
                                    progressDialog.dismiss();
                                    tvName.setEnabled(false);
                                    email_id.setEnabled(false);
                                    addr.setEnabled(false);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                } else if (code.equals("-1")) {
                                    AlertDialog alertDialog = taoMotAlertDialog();
                                    alertDialog.show();
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

                break;
            case R.id.changepass:
                Intent it = new Intent(getApplicationContext(), ChangePass.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }

    private void addInfo() {
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
                    pro.setText(coin+" coins");
                    db.updateinfo(fullname, email, address, id, coin);
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
