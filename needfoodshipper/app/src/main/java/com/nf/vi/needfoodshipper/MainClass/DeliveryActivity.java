package com.nf.vi.needfoodshipper.MainClass;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nf.vi.needfoodshipper.Adapter.HistoryAdapter;
import com.nf.vi.needfoodshipper.Adapter.ListviewAdapter;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.ListviewContructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.TrangThaiRequest;
import com.nf.vi.needfoodshipper.SupportClass.ChangeDatetoTimestamp;
import com.nf.vi.needfoodshipper.SupportClass.ChangeTimeToHours;
import com.nf.vi.needfoodshipper.SupportClass.ChangeTimestamp;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.R.id.tabhost;

public class DeliveryActivity extends AppCompatActivity {

    private Button btnFinish, btnDirect;
    private TextView tvTitle, tvre, tvStt, tvCode, tvod, tvloc, tvct, tvtm, tvpay, textstt, tvMoneyShiper, tvTimeleft, tvSanpham, tvSoluong, tvDongia, tvThanhtien;
    private Dialog dialog;
    private ImageView imgstt;
    SimpleDateFormat dateFormatter;
    private Calendar cal, cal1;
    private String a, id, order, lc, ct, re, tl, pay, moneyship, stt, code, stt2, listsanpham, tm;
    private Button btnDFinish, btnDCancel1, btnDSend, btnDCancel;
    private RelativeLayout rfDialog;
    ChangeDatetoTimestamp change;
    ChangeTimeToHours changetime;
    private ListviewAdapter adapter;

    private List<ListviewContructor> ld;
    String timeleftc;
    DBHandle db;
    EditText edfedd;
    List<ListUserContructor> list;
    private Date date;
    private String timest, timestgh, timeleft, tk, note, sanpham, soluong, dongia, thanhtien;
    private int time, ngay, thang, nam, gio, phut;
    private long timesttl, giocl, phutcl, tinhgio, ngaycl;
    private RelativeLayout rtButton;
    private ListView lvSanpham;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        changetime = new ChangeTimeToHours();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor lu : list) {
            tk = lu.getAccessToken();
        }

        Calendar cal = Calendar.getInstance();
//        timest = change.main(today+"");


        // Toast.makeText(getApplication(), timest + "", Toast.LENGTH_LONG).show();

        imgstt = (ImageView) findViewById(R.id.imgStt);
        textstt = (TextView) findViewById(R.id.tvStt);
        tvStt = (TextView) findViewById(R.id.tvStt);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvre = (TextView) findViewById(R.id.tvre);
//        tvod = (TextView) findViewById(R.id.tvod);
        tvloc = (TextView) findViewById(R.id.tvloc);
        tvct = (TextView) findViewById(R.id.tvct);
        tvtm = (TextView) findViewById(R.id.tvtm);
        tvpay = (TextView) findViewById(R.id.tvpay);
        tvMoneyShiper = (TextView) findViewById(R.id.tvMoneyShip);
        tvTimeleft = (TextView) findViewById(R.id.tvTimeLeft);
        lvSanpham = (ListView) findViewById(R.id.lvSanpham);
        ld = new ArrayList<ListviewContructor>();
        adapter = new ListviewAdapter(getBaseContext(), R.layout.custom_lv, ld);
        lvSanpham.setAdapter(adapter);
//        tvSanpham=(TextView)findViewById(R.id.tvSanpham);
//        tvSoluong=(TextView)findViewById(R.id.tvSoluong);
//        tvDongia=(TextView)findViewById(R.id.tvDongia) ;
//        tvThanhtien=(TextView)findViewById(R.id.tvThanhtien);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        rtButton = (RelativeLayout) findViewById(R.id.rlButton);
        btnDirect = (Button) findViewById(R.id.btnDirect);


        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.dltoobar));

        Intent data = getIntent();
        id = data.getStringExtra("id");
        order = data.getStringExtra("order");
        lc = data.getStringExtra("lc");
        ct = data.getStringExtra("ct");
        re = data.getStringExtra("re");
        tl = data.getStringExtra("tl");
        pay = data.getStringExtra("pay");
        moneyship = data.getStringExtra("moneyship");
        code = data.getStringExtra("code");
        stt = data.getStringExtra("stt");
        listsanpham = data.getStringExtra("listsanpham");
        Log.d("listsanpham", listsanpham);


        try {

            JSONObject json = new JSONObject(listsanpham);
            Iterator<String> ite = json.keys();
            while (ite.hasNext())
//            for (int i = 0; !ite.hasNext(); i++)
            {
                String key = ite.next();
                JSONObject idx = json.getJSONObject(key);

                soluong = idx.getString("quantity");
                sanpham = idx.getString("title");
                dongia = idx.getString("price");
                thanhtien = idx.getString("money");

                ld.add(new ListviewContructor(sanpham, soluong, dongia, thanhtien));


            }

            adapter.notifyDataSetChanged();


        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        if (stt.equals("waiting")) {
            rtButton.setVisibility(View.VISIBLE);

//            imgstt.setImageDrawable(getResources(R.drawable.grdot));
            imgstt.setImageResource(R.drawable.grdot);
            textstt.setText(this.getResources().getString(R.string.wait));
//            textstt.setTextColor(this.getResources().R.color.greenL));
//            textstt.setTextColor(getResources().getColor(R.color.greenL));
            textstt.setTextColor(getResources().getColor(R.color.greenL));

        } else if (stt.equals("process")) {
            rtButton.setVisibility(View.GONE);
        }

        tvre.setText(re);
        tvCode.setText(code);
//        tvod.setText(order);
        tvloc.setText(lc);
        tvct.setText(ct);
        tvtm.setText(tl);
        tvpay.setText(pay + " đ");
        tvMoneyShiper.setText(moneyship + " đ");


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogdelivery();
            }
        });
        btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myla + "," + mylo+"&daddr="+ hotella + "," + hotello ));
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + "&daddr=" + lc));
                startActivity(intent);
            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormatter.format(cal.getTime());
        ngay = cal.get(Calendar.DAY_OF_MONTH);
        thang = cal.get(Calendar.MONTH);
        nam = cal.get(Calendar.YEAR);
        gio = cal.get(Calendar.HOUR_OF_DAY);
        phut = cal.get(Calendar.MINUTE);
        String ngaythang = ngay + "/" + (thang + 1) + "/" + nam + " " + gio + ":" + phut;
        if (tl.equals("")) {
            timeleftc = "";
            tvTimeleft.setText("");
        } else {
            timest = change.main(ngaythang);


            timestgh = change.main(tl);
//        Toast.makeText(getApplication(),timestgh+"",Toast.LENGTH_LONG).show();
            timesttl = Long.parseLong(timestgh) - Long.parseLong(timest);
            Log.d("timest", timesttl + "");


//        timeleft = changetime.times(timesttl);
//          Toast.makeText(getApplication(), timesttl+"", Toast.LENGTH_LONG).show();
            if (timesttl < 0) {
                tinhgio = -timesttl;
                imgstt.setImageResource(R.drawable.reddot);
                textstt.setText(this.getResources().getString(R.string.wait));
//            textstt.setTextColor(this.getResources().R.color.greenL));
//            textstt.setTextColor(getResources().getColor(R.color.greenL));
                textstt.setTextColor(getResources().getColor(R.color.red));
            } else {
                tinhgio = timesttl;
            }
            new CountDownTimer(tinhgio * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    ngaycl = millisUntilFinished / (3600000 * 24);
                    giocl = (millisUntilFinished % (3600000 * 24)) / 3600000;
                    phutcl = (millisUntilFinished % 3600000) / 60000;
                    timeleftc = String.valueOf(ngaycl) + "day " + String.valueOf(giocl) + "h " + String.valueOf(phutcl) + "m";
//                Toast.makeText(getApplication(),timeleftc,Toast.LENGTH_LONG).show();
                    if (timesttl < 0) {
                        tvTimeleft.setText(getResources().getString(R.string.late) + " " + timeleftc);
                    } else {
                        tvTimeleft.setText(timeleftc);
                    }
//                tvTimeleft.setText(timeleftc);
                }

                public void onFinish() {

                }

            }.start();
        }


    }

    private void showdialogdelivery() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delivery);
        btnDFinish = (Button) dialog.findViewById(R.id.btnDFinish);
        btnDCancel1 = (Button) dialog.findViewById(R.id.btnDCancel1);
        btnDSend = (Button) dialog.findViewById(R.id.btnDSend);
        btnDCancel = (Button) dialog.findViewById(R.id.btnDDeny);
        edfedd = (EditText) dialog.findViewById(R.id.edfedd);
        rfDialog = (RelativeLayout) dialog.findViewById(R.id.rlDialog);
        btnDFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "được k", Toast.LENGTH_LONG).show();
                note = edfedd.getText().toString();
                stt2 = "done";
                sendSV();

            }
        });
        btnDCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rfDialog.setVisibility(View.VISIBLE);
                btnDCancel1.setEnabled(false);
                btnDFinish.setEnabled(false);
                btnDFinish.setBackgroundColor(getResources().getColor(R.color.gray));
                btnDCancel1.setBackgroundColor(getResources().getColor(R.color.gray));
                btnDFinish.setVisibility(View.GONE);
                btnDCancel1.setVisibility(View.GONE);
            }
        });
        btnDCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rfDialog.setVisibility(View.GONE);
                btnDFinish.setEnabled(true);
                btnDFinish.setBackgroundResource(R.drawable.custombt);
            }
        });
        btnDSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = edfedd.getText().toString();

                stt2 = "cancel";
                if (note.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.dslydo), Toast.LENGTH_SHORT).show();
                } else {
                    sendSV();
                }


//                rfDialog.setVisibility(View.GONE);
//                btnDFinish.setEnabled(true);
//                btnDFinish.setBackgroundResource(R.drawable.custombt);

            }
        });
        dialog.show();
    }

    private void sendSV() {
        tm = tvTimeleft.getText().toString();


        String link = getResources().getString(R.string.saveStatusOrderAPI);
//        String link = "http://needfood.webmantan.com/c";
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    Log.d("CODEAA", code);

                    if (code.equals("0")) {
                        dialog.dismiss();
                        Toast.makeText(getApplication(), getString(R.string.dsthanhcong), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
//                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TrangThaiRequest save = new TrangThaiRequest(tk, note, stt2, id, tm, link, response);
        RequestQueue qe = Volley.newRequestQueue(getApplication());
        qe.add(save);

    }


}
