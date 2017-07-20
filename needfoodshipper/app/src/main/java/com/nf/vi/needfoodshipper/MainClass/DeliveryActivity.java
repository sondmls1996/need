package com.nf.vi.needfoodshipper.MainClass;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.TrangThaiRequest;
import com.nf.vi.needfoodshipper.SupportClass.ChangeDatetoTimestamp;
import com.nf.vi.needfoodshipper.SupportClass.ChangeTimeToHours;
import com.nf.vi.needfoodshipper.SupportClass.ChangeTimestamp;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.id.tabhost;

public class DeliveryActivity extends AppCompatActivity {

    private Button btnFinish, btnDirect;
    private TextView tvTitle, tvre, tvStt, tvCode, tvod, tvloc, tvct, tvtm, tvpay, textstt, tvMoneyShiper, tvTimeleft;
    private Dialog dialog;
    private ImageView imgstt;
    SimpleDateFormat dateFormatter;
    private Calendar cal, cal1;
    private String a, id, order, lc, ct, re, tl, pay, moneyship, stt, code, stt2;
    private Button btnDFinish, btnDCancel1, btnDSend, btnDCancel;
    private RelativeLayout rfDialog;
    ChangeDatetoTimestamp change;
    ChangeTimeToHours changetime;
    String timeleftc;
    DBHandle db;
    EditText edfedd;
    List<ListUserContructor> list;
    private Date date;
    private String timest, timestgh, timeleft, tk, note;
    private int time, ngay, thang, nam, gio, phut;
    private long timesttl, giocl, phutcl;
    private RelativeLayout rtButton;


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
        tvod = (TextView) findViewById(R.id.tvod);
        tvloc = (TextView) findViewById(R.id.tvloc);
        tvct = (TextView) findViewById(R.id.tvct);
        tvtm = (TextView) findViewById(R.id.tvtm);
        tvpay = (TextView) findViewById(R.id.tvpay);
        tvMoneyShiper = (TextView) findViewById(R.id.tvMoneyShip);
        tvTimeleft = (TextView) findViewById(R.id.tvTimeLeft);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        rtButton = (RelativeLayout) findViewById(R.id.rlButton);


        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Delevery(1)");

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
        tvod.setText(order);
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
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormatter.format(cal.getTime());
        ngay = cal.get(Calendar.DAY_OF_MONTH);
        thang = cal.get(Calendar.MONTH);
        nam = cal.get(Calendar.YEAR);
        gio = cal.get(Calendar.HOUR_OF_DAY);
        phut = cal.get(Calendar.MINUTE);
        String ngaythang = ngay + "/" + (thang + 1) + "/" + nam + " " + gio + ":" + phut;
        timest = change.main(ngaythang);

        timestgh = change.main("20/7/2017 23:00");
        timesttl = Long.parseLong(timestgh) - Long.parseLong(timest);
        Log.d("timest", timesttl + "");


//        timeleft = changetime.times(timesttl);
        //  Toast.makeText(getApplication(), timeleft+"", Toast.LENGTH_LONG).show();
        new CountDownTimer(timesttl * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                giocl = millisUntilFinished / 3600000;
                phutcl = (millisUntilFinished % 3600000) / 60000;
                timeleftc = String.valueOf(giocl) + "h" + String.valueOf(phutcl);
                tvTimeleft.setText(timeleftc);
            }

            public void onFinish() {

            }

        }.start();


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
                stt2 = "done";
                sendSV();
                dialog.dismiss();
            }
        });
        btnDCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rfDialog.setVisibility(View.VISIBLE);
                btnDFinish.setEnabled(false);
                btnDFinish.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        });
        btnDCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rfDialog.setVisibility(View.GONE);
                btnDFinish.setEnabled(true);
                btnDFinish.setBackgroundResource(R.drawable.custombt);
            }
        });
        btnDSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt2 = "cancel";
                sendSV();
                dialog.dismiss();
                rfDialog.setVisibility(View.GONE);
                btnDFinish.setEnabled(true);
                btnDFinish.setBackgroundResource(R.drawable.custombt);

            }
        });
        dialog.show();
    }

    private void sendSV() {
        note = edfedd.getText().toString();

        //  String link = getResources().getString(R.string.saveStatusOrderAPI);
        String link = "http://needfood.webmantan.com/saveStatusOrderAPI";
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    Log.d("CODEAA", code);

                    if (code.equals("0")) {
//                            Intent i = new Intent(SentPassEmail.this, DangNhapActivity.class);
//                            startActivity(i);
                    } else {
//                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TrangThaiRequest save = new TrangThaiRequest(tk, note, stt2, id, timeleftc, link, response);
        RequestQueue qe = Volley.newRequestQueue(getApplication());
        qe.add(save);

    }


}
