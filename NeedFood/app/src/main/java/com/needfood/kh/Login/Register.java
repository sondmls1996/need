package com.needfood.kh.Login;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText tvname, tvfone, tvmail, tvpass, tvpass2, tvadr, tvbirth;
    TextView policy;
    Button btnokay;
    String fullname, email, adr;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String sex = "man";
    String birtday;
    static final int DATE_DIALOG_ID = 0;
    Calendar c;
    private SimpleDateFormat dateFormatter;
    public DatePickerDialog fromDatePickerDialog;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.regiss));

         c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        fromDatePickerDialog = new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay);
        String formattedDate = dateFormatter.format(c.getTime());
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String regexStr = "^[0-9]$";
        tvname = (EditText) findViewById(R.id.tvfn);
        tvfone = (EditText) findViewById(R.id.tvf);
        tvmail = (EditText) findViewById(R.id.tvem);
        policy = (TextView) findViewById(R.id.policy);
        radioSexGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        sex = "man";
                        break;
                    case R.id.wonman:
                        sex = "wonman";
                        break;
                    case R.id.fix:
                        sex = "flexible";
                        break;
                }
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Policy.class);
                startActivity(it);
            }
        });
        tvpass = (EditText) findViewById(R.id.tvp);
        tvpass2 = (EditText) findViewById(R.id.tvpa);
        tvadr = (EditText) findViewById(R.id.tvadr);
        btnokay = (Button) findViewById(R.id.btnreg);
        tvbirth = (EditText) findViewById(R.id.tvbirt);
        tvbirth.setInputType(InputType.TYPE_NULL);
        tvbirth.requestFocus();
        tvbirth.setOnClickListener(this);
        tvbirth.setText(formattedDate);

        btnokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = DialogUtils.show(Register.this, getResources().getString(R.string.wait));
                String name = tvname.getText().toString();
                String fone = tvfone.getText().toString();
                String mail = tvmail.getText().toString();
                String pass = tvpass.getText().toString();
                String pass2 = tvpass2.getText().toString();
                String adr = tvadr.getText().toString();
                String birtt = tvbirth.getText().toString();

                String link = getResources().getString(R.string.linkreg);
                Map<String, String> map = new HashMap<String, String>();

                if (name.matches("") || fone.matches("") || mail.matches("") || pass.matches("") || pass2.matches("") || adr.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
                } else if (fone.length() < 10) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.checkfon), Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(pass2)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrpass), Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.shortpass), Toast.LENGTH_SHORT).show();
                } else if (!mail.matches(emailPattern)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailwr), Toast.LENGTH_SHORT).show();
                } else {
                    map.put("sex", sex);
                    map.put("birthday", birtt);
                    map.put("fullName", name);
                    map.put("email", mail);
                    map.put("fone", fone);
                    map.put("pass", pass);
                    map.put("passAgain", pass2);
                    map.put("address", adr);
                    Log.d("LOGABC",sex+"-"+birtt);
                    Response.Listener<String> response = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString("code");
                                if (code.equals("0")) {
                                    progressDialog.dismiss();
                                    Intent it = new Intent(getApplicationContext(), Login.class);
                                    startActivity(it);
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PostCL post = new PostCL(link, map, response);
                    RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                    que.add(post);
                }

            }
        });

    }


    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int year,
                              int month, int dayOfMonth) {
            c.set(year, month, dayOfMonth);
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            tvbirth.setText(dateFormatter.format(c.getTime()));


        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            tvbirth.setText(new StringBuilder().append(mDay).append("/").append(mMonth + 1).append("/").append(mYear));

        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvbirt:
                fromDatePickerDialog.show();
                break;
        }
    }
}
