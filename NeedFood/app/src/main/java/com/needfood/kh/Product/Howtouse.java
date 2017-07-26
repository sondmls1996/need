package com.needfood.kh.Product;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Howtouse extends AppCompatActivity {
    String htu, simg;
    String priceprd, prdcode, titl;
    String maniid, info;
    TextView txtht, txts, nom;
    ImageView img;
    RatingBar ratingbar;
    Button submit;
    String idpr, idsl;
    Session ses;
    DataHandle db;
    List<InfoConstructor> listu;
    String namesl, access, idu, fullname, phone, bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.howtou));
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ses = new Session(getApplicationContext());
        db = new DataHandle(getApplicationContext());
        if (ses.loggedin()) {
            listu = db.getAllInfor();
            access = listu.get(listu.size() - 1).getAccesstoken();
        }
        Intent intent = getIntent();
        htu = intent.getStringExtra("htu");
        titl = intent.getStringExtra("tit");
        simg = intent.getStringExtra("img");
        idpr = intent.getStringExtra("idpr");
        idsl = intent.getStringExtra("idsl");
        txtht = (TextView) findViewById(R.id.txtht);
        nom = (TextView) findViewById(R.id.nom);
        txts = (TextView) findViewById(R.id.txtsp);
        img = (ImageView) findViewById(R.id.imgsp);
        if (htu.equals("")) {
            nom.setVisibility(View.VISIBLE);
        } else {
            nom.setVisibility(View.GONE);
            txtht.setText(Html.fromHtml(Html.fromHtml(htu).toString()));
        }

        Picasso.with(getApplicationContext()).load(simg).into(img);
        txts.setText(titl);
        addListenerOnButtonClick();
    }

    public void addListenerOnButtonClick() {
        ratingbar = (RatingBar) findViewById(R.id.ratingBar1);
        submit = (Button) findViewById(R.id.button1);
        //Performing action on Button Click
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating = String.valueOf(ratingbar.getRating());
                final String link = getResources().getString(R.string.linkvote);

                Map<String, String> map = new HashMap<>();
                map.put("idProduct", idpr);
                map.put("accessToken", access);
                map.put("point", rating);

                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("EEE", response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString("code");
                            if (code.equals("0")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
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
                PostCL get = new PostCL(link, map, response);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                que.add(get);
            }

        });
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
