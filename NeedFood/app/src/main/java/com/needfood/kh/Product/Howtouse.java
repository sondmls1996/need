package com.needfood.kh.Product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.needfood.kh.R;

public class Howtouse extends AppCompatActivity {
    String idprd, idsl, namesl, access, idu, fullname, phone, bar, cata;
    String priceprd, prdcode, titl;
    String maniid, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.howtou));
    }


}
