package com.needfood.kh.Product;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.R;
import com.squareup.picasso.Picasso;

public class Howtouse extends AppCompatActivity {
    String htu, simg;
    String priceprd, prdcode, titl;
    String maniid, info;
    TextView txtht,txts,nom;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.howtou));
        Intent intent = getIntent();
        htu = intent.getStringExtra("htu");
        titl = intent.getStringExtra("tit");
        simg = intent.getStringExtra("img");
        txtht = (TextView)findViewById(R.id.txtht);
        nom = (TextView)findViewById(R.id.nom);
        txts = (TextView)findViewById(R.id.txtsp);
        img = (ImageView)findViewById(R.id.imgsp);
        if(htu.equals("")){
            nom.setVisibility(View.VISIBLE);
        }else{
            nom.setVisibility(View.GONE);
            txtht.setText(Html.fromHtml(Html.fromHtml(htu).toString()));
        }

        Picasso.with(getApplicationContext()).load(simg).into(img);
        txts.setText(titl);
    }




}
