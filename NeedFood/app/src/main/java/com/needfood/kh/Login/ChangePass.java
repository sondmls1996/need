package com.needfood.kh.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePass extends AppCompatActivity {
    EditText old, newpass, newpassagain;
    Button btn;
    DataHandle db;
    List<InfoConstructor> list;
    Session ses;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        db = new DataHandle(getApplicationContext());
        list = db.getAllInfor();
        for (InfoConstructor it : list) {
            token = it.getAccesstoken();
        }
        old = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        newpassagain = (EditText) findViewById(R.id.newpassagain);
        btn = (Button) findViewById(R.id.btnChange);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changpass();
            }
        });
    }

    public void changpass() {
        final ProgressDialog progressDialog = DialogUtils.show(this, getResources().getString(R.string.wait));
        final String passold = old.getText().toString();
        final String passnew = newpass.getText().toString();
        final String passagain = newpassagain.getText().toString();
        Map<String, String> map = new HashMap<String, String>();
        if (passold.matches("") || passnew.matches("") || passagain.matches("")) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
        } else {
            map.put("accessToken", token);
            map.put("oldPass", passold);
            map.put("pass", passnew);
            map.put("rePass", passagain);

            Response.Listener<String> response = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString("code");
                        if (code.equals("0")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), StartActivity.class);
                            startActivity(i);
                            finish();
                        } else if (code.equals("-1")) {
                            AlertDialog alertDialog = taoMotAlertDialog();
                            alertDialog.show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            PostCL change = new PostCL(getResources().getString(R.string.linkchangep), map, response);
            RequestQueue que = Volley.newRequestQueue(getApplicationContext());
            que.add(change);
        }
    }

    // hoi xoa
    private AlertDialog taoMotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Thiết lập tiêu đề hiển thị
        builder.setTitle("Lỗi đăng nhập");
        //Thiết lập thông báo hiển thị

        builder.setMessage("Tài khoản đã được đăng nhập ở điện thoại khác");
        builder.setCancelable(false);
        //Tạo nút Chu hang
        builder.setNegativeButton("Đồng ý",
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
