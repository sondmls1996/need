package com.nf.vi.needfoodshipper.MainClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.TrangThaiRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private String id;
    private String order;
    private String lc;
    private String ct;
    private String re;
    private String tl;
    private String pay;
    private String stt;
    private RelativeLayout rl;
    private TextView ord;
    private TextView loc;
    private String note, tk;

    private TextView cta;
    private TextView rec;
    private TextView timel;
    private TextView paym;
    private EditText edfedd;
    private Button btnacc;
    private Button btndn;
    private Button btnSend;
    private Button btnDeny, btnXem;
    DBHandle db;
    List<ListUserContructor> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor lu : list) {
            tk = lu.getAccessToken();
        }

        Intent data = getIntent();
        id = data.getStringExtra("id");
        order = data.getStringExtra("order");
        lc = data.getStringExtra("lc");
        ct = data.getStringExtra("ct");
        re = data.getStringExtra("re");
        tl = data.getStringExtra("tl");
        pay = data.getStringExtra("pay");
        stt = data.getStringExtra("stt");

        ord = (TextView) findViewById(R.id.tvod);
        loc = (TextView) findViewById(R.id.tvloc);
        cta = (TextView) findViewById(R.id.tvct);
        rec = (TextView) findViewById(R.id.tvre);
        timel = (TextView) findViewById(R.id.tvtm);
        paym = (TextView) findViewById(R.id.tvpay);
        btnacc = (Button) findViewById(R.id.btnac);
        btndn = (Button) findViewById(R.id.btndn);
        rl = (RelativeLayout) findViewById(R.id.rlfeed);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnDeny = (Button) findViewById(R.id.btnDeny);
        btnXem = (Button) findViewById(R.id.btnxem);
        edfedd = (EditText) findViewById(R.id.edfedd);
        settext();
        sukien();
    }

    private void sukien() {
        if (stt.equals("waiting")) {
            btnacc.setText("WAITING");
            btnacc.setBackgroundResource(R.color.orangeDark);
            btndn.setEnabled(false);
            btndn.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.setVisibility(View.GONE);
                btnacc.setEnabled(true);
                btnacc.setBackgroundResource(R.drawable.custombt);
            }
        });
        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.setVisibility(View.VISIBLE);
                btnacc.setEnabled(false);
                btnacc.setBackgroundColor(getResources().getColor(R.color.gray));


            }
        });
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnacc.setText("WAITING");
                btndn.setEnabled(false);
                btndn.setBackgroundColor(getResources().getColor(R.color.gray));
                note = "";
                stt = "waiting";
                sendSV();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = edfedd.getText().toString();
                stt = "cancel";
                sendSV();
                rl.setVisibility(View.GONE);
            }
        });
        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeliveryActivity.class));
            }
        });
    }

    private void settext() {
        ord.setText(order);
        loc.setText(lc);
        cta.setText(ct);
        rec.setText(re);
        timel.setText(tl);
        paym.setText(pay);
    }

    private void sendSV() {

        //  String link = getResources().getString(R.string.saveStatusOrderAPI);
        String link = "http://needfood.webmantan.com/saveStatusOrderAPI";
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("CODE", response);
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");


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
        TrangThaiRequest save = new TrangThaiRequest(tk, note, stt, id,"", link, response);
        RequestQueue qe = Volley.newRequestQueue(this);
        qe.add(save);

    }
}
