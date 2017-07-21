package com.needfood.kh.More;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.ChangePass;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreContanct extends AppCompatActivity implements View.OnClickListener {
    TextView change, changepass, save;
    EditText name_id, email_id, phone_id, pay, tvName, addr;
    DataHandle db;
    String id_name, email, phone;
    List<InfoConstructor> list;
    String fname, fone, address, id, token, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_contanct);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.yourin));
        db = new DataHandle(getApplicationContext());
        list = db.getAllInfor();
        for (InfoConstructor it : list) {
            fname = it.getFullname();
            fone = it.getFone();
            address = it.getAddress();
            id = it.getId();
            token = it.getAccesstoken();
            email = it.getEmail();
            pass = it.getPass();
        }
        tvName = (EditText) findViewById(R.id.mor_name);
        name_id = (EditText) findViewById(R.id.acc_id);
        email_id = (EditText) findViewById(R.id.email_id);
        phone_id = (EditText) findViewById(R.id.fone_id);
//        pay = (EditText) findViewById(R.id.pay);
        change = (TextView) findViewById(R.id.change);
        save = (TextView) findViewById(R.id.save);
        changepass = (TextView) findViewById(R.id.changepass);
        addr = (EditText) findViewById(R.id.address_id);
        tvName.setText(fname);
        name_id.setText(id);
        email_id.setText(email);
        phone_id.setText(fone);
        addr.setText(list.get(0).getAddress());
        tvName.setEnabled(false);
        name_id.setEnabled(false);
        email_id.setEnabled(false);
        phone_id.setEnabled(false);
        addr.setEnabled(false);
//        pay.setEnabled(false);
        change.setOnClickListener(this);
        changepass.setOnClickListener(this);
        save.setOnClickListener(this);
        save.setVisibility(View.GONE);
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
                                    db.updateinfo(namee, emaill, fone, pass, addresss, id, token);
                                    progressDialog.dismiss();
                                    tvName.setEnabled(false);
                                    email_id.setEnabled(false);
                                    addr.setEnabled(false);
                                    Intent it = new Intent(getApplicationContext(), MoreContanct.class);
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

                break;
            case R.id.changepass:
                Intent it = new Intent(getApplicationContext(), ChangePass.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }
}
