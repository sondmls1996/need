package com.needfood.kh.Product;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.needfood.kh.Adapter.PreAdapter;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.PreConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Preview extends AppCompatActivity implements View.OnClickListener{
    String json,mid;
    RecyclerView lv;
    private SimpleDateFormat dateFormatter, timeformat;
    ArrayList<PreConstructor> arr;
    DataHandle dataHandle;
    List<ListMN> list;
    List<InfoConstructor> listif;
    TextView shipm;
    HashMap<String, String> hashMap;
    EditText edname,edadr,edphome,edemail,edghichu,edpickngay,edpickgio;
    Calendar c;
    int day, month2, year2, hour, minitus;
    public DatePickerDialog fromDatePickerDialog;
    public TimePickerDialog timepicker;
    PreAdapter adapter;
    Button btno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.conor));
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
         hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        json = hashMap.get("listProduct");
        btno = (Button)findViewById(R.id.btno); 
        mid = intent.getStringExtra("min");
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

        edname = (EditText)findViewById(R.id.edname);
        edadr = (EditText)findViewById(R.id.edadr);
        edphome = (EditText)findViewById(R.id.edphone);

        edghichu = (EditText) findViewById(R.id.ghichu);

        for(InfoConstructor lu:listif){
            edname.setText(lu.getFullname());
            edadr.setText(lu.getAddress());
            edphome.setText(lu.getFone());

        }
        lv = (RecyclerView) findViewById(R.id.lvpre);
        arr = new ArrayList<>();
        adapter = new PreAdapter(getApplicationContext(),arr);
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
            for (int i =0; i<jo.length();i++){
                JSONObject idx = jo.getJSONObject(i);
                arr.add(new PreConstructor(idx.getString("title"),idx.getString("quantity"),idx.getString("money"),mid));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendSever() {
        String name = edname.getText().toString();
        String adr = edadr.getText().toString();
        String phone = edphome.getText().toString();

        String note = edghichu.getText().toString();

        hashMap.put("fullName",name);
        hashMap.put("timeShiper",day+"/"+month2+"/"+year2+" "+hour+":"+minitus);
        hashMap.put("address",adr);
        hashMap.put("note",note);
        hashMap.put("fone",phone);


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
}
