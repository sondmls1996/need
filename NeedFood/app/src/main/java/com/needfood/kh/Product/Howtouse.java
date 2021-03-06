package com.needfood.kh.Product;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.Session;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Howtouse extends AppCompatActivity {
    String htu, simg;
    String priceprd, prdcode, titl;
    String maniid, info;
    TextView txtht, txts, nom;
    ImageView img;
    RatingBar ratingbar;
    Button submit;
    String idpr, idsl;
    TextView web;
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
        txtht = (TextView) findViewById(R.id.idwv);
//        nom = (TextView) findViewById(R.id.nom);
      //  web = (TextView)findViewById(R.id.idwv);

        txts = (TextView) findViewById(R.id.txtsp);
        img = (ImageView) findViewById(R.id.imgsp);
        if (htu.equals("")) {
//             nom.setVisibility(View.VISIBLE);
        } else {
            String editedTextReadable = android.text.Html.fromHtml(htu).toString();
            txtht.setText(editedTextReadable);
        }

        Picasso.with(getApplicationContext()).load(simg).into(img);
        txts.setText(titl);
//        addListenerOnButtonClick();
    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;

            id = getResources().getIdentifier(source, "drawable", getPackageName());

            if (id == 0) {
                // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
                id = getResources().getIdentifier(source, "drawable", "android");
            }

            if (id == 0) {
                // prevent a crash if the resource still can't be found
                return null;
            } else {
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        }

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
                        db.deleteAll();
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
