package com.needfood.kh.Product;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.PreAdapter;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.PreConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Preview extends AppCompatActivity implements View.OnClickListener {
    String json, mid, stt, mnship, idsl, acess,idmn;
    RecyclerView lv;
    private SimpleDateFormat dateFormatter, timeformat;
    ArrayList<PreConstructor> arr;
    DataHandle dataHandle;
    List<ListMN> list;
    List<InfoConstructor> listif;
    Session ses;
    TextView shipm;
    HashMap<String, String> hashMap;
    EditText edname, edadr, edphome, edemail, edghichu, edpickngay, edpickgio;
    Calendar c;
    double total;
    int day, month2, year2, hour, minitus, numshare;
    public DatePickerDialog fromDatePickerDialog;
    public TimePickerDialog timepicker;
    PreAdapter adapter;
    Button btno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.conor));

        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");
        json = hashMap.get("listProduct");

        idsl = hashMap.get("idSeller");
        total = Double.parseDouble(hashMap.get("totalMoneyProduct"));
        mnship = hashMap.get("moneyShip");
        stt = intent.getStringExtra("stt");
        if (intent.hasExtra("num")) {
            numshare = intent.getIntExtra("num", 0);
        }
        btno = (Button) findViewById(R.id.btno);
        mid = intent.getStringExtra("min");
        edadr = (EditText) findViewById(R.id.edadrship);
        dataHandle = new DataHandle(this);
        c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month2 = c.get(Calendar.MONTH);
        year2 = c.get(Calendar.YEAR);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minitus = c.get(Calendar.MINUTE);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormatter.format(c.getTime());

        timeformat = new SimpleDateFormat("HH:mm");
        shipm = (TextView) findViewById(R.id.shipmn);
        shipm.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(mnship)) + "VND");

        fromDatePickerDialog = new DatePickerDialog(this, datePickerListener, year2, month2, day);
        timepicker = new TimePickerDialog(this, timepic, hour, minitus, true);
        String formattime = timeformat.format(c.getTime());
        //edpick ngay
        edpickngay = (EditText) findViewById(R.id.pickngay);
        edpickngay.setInputType(InputType.TYPE_NULL);
        edpickngay.requestFocus();
        edpickngay.setOnClickListener(this);
        edpickngay.setText(formattedDate);
        //edpicgio
        edpickgio = (EditText) findViewById(R.id.pickgio);
        edpickgio.setInputType(InputType.TYPE_NULL);
        edpickgio.requestFocus();
        edpickgio.setOnClickListener(this);
        edpickgio.setText(formattime);

        listif = dataHandle.getAllInfor();

        edname = (EditText) findViewById(R.id.edname);

        edphome = (EditText) findViewById(R.id.edphone);
        edphome.requestFocus();
        edghichu = (EditText) findViewById(R.id.ghichu);

        for (InfoConstructor lu : listif) {
            edname.setText(lu.getFullname());
            edadr.setText(lu.getAddress());
            edphome.setText(lu.getFone());
            acess = lu.getAccesstoken();

        }
        getNumberShare();
        lv = (RecyclerView) findViewById(R.id.lvpre);
        arr = new ArrayList<>();
        adapter = new PreAdapter(getApplicationContext(), arr);
        lv.setAdapter(adapter);
        lv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSever();
            }
        });
        try {
            JSONArray jo = new JSONArray(json);
            for (int i = 0; i < jo.length(); i++) {
                JSONObject idx = jo.getJSONObject(i);
                arr.add(new PreConstructor(idx.getString("title"), idx.getString("quantity"), idx.getString("money"), mid));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNumberShare() {
        String link = getResources().getString(R.string.linkgetnum);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", acess);
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int a2 = Integer.parseInt(response);
                Log.d("TTT", a2 + "");
                if (a2 < numshare) {
                    btno.setEnabled(false);
                    btno.setBackgroundColor(R.color.gray2);
                } else {
                    btno.setEnabled(true);
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void sendSever() {
        btno.setEnabled(false);
        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        String link = getResources().getString(R.string.linkorder);

        String name = edname.getText().toString();
        String adr = edadr.getText().toString();
        String phone = edphome.getText().toString();
        String note = edghichu.getText().toString();

        if (adr.equals("")||phone.equals("")) {
            pro.dismiss();
            btno.setEnabled(true);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
        } else {
            btno.setEnabled(true);
            hashMap.put("totalMoneyProduct",total+Integer.parseInt(mnship)+"");
            hashMap.put("fullName", name);
            hashMap.put("timeShiper", day + "/" + (month2 + 1) + "/" + year2 + " " + hour + ":" + minitus);
            hashMap.put("address", adr);
            hashMap.put("note", note);
            hashMap.put("fone", phone);
            Log.d("total",total+"");

            Response.Listener<String> response = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString("code");
                        if (code.equals("0")) {
                            if (stt.equals("deal")) {
                                saveShare();
                            }
                            pro.dismiss();
                            finish();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ssor), Toast.LENGTH_SHORT).show();
                        } else if (code.equals("-1")) {
                            pro.dismiss();
                            taoMotAlertDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };
            PostCL get = new PostCL(link, hashMap, response);
            RequestQueue que = Volley.newRequestQueue(getApplicationContext());
            que.add(get);
        }


    }

    public void saveShare() {
        final String link = getResources().getString(R.string.linksaveShare);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", acess);
        map.put("idSeller", idsl);
        map.put("numberShare", -numshare + "");

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getNumberShare();


            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int year,
                              int month, int dayOfMonth) {
            c.set(year, month, dayOfMonth);
            year2 = year;
            month2 = month;
            day = dayOfMonth;
            edpickngay.setText(dateFormatter.format(c.getTime()));


        }
    };
    private TimePickerDialog.OnTimeSetListener timepic = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            edpickgio.setText(hourOfDay + ":" + minute);
        }
    };

    @Override
    public void onClick(View v) {
        if (v == edpickngay) {
            fromDatePickerDialog.show();
        } else if (v == edpickgio) {
            timepicker.show();
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
                        dataHandle.deleteInfo();
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
