package com.nf.vi.needfoodshipper.MainClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.ChangePassRequest;
import com.nf.vi.needfoodshipper.Request.SaveGpsRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChangePassActivity extends AppCompatActivity {
    private TextView tvTitle;
    private EditText edtPassold, edtPassnew, edtPassnew2;
    private Button btnSavePass;
    private DBHandle db;
    private List<ListUserContructor> list;
    private String token, passold, pass, repass, passdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {

            token = nu.getAccessToken();
            passdb = nu.getPass();
        }
//        setTitle("Change Password");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.ifchangepasstoobar));

        edtPassold = (EditText) findViewById(R.id.edtPassold);
        edtPassnew = (EditText) findViewById(R.id.edtPassnew);
//        edtPassnew2=(EditText)findViewById(R.id.edtPassnew2);
        edtPassnew2 = (EditText) findViewById(R.id.edtPassnew2);
        btnSavePass = (Button) findViewById(R.id.btnSavePass);

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepasss();

            }


        });

    }

    private void savepasss() {
        passold = edtPassold.getText().toString();
        pass = edtPassnew.getText().toString();
        repass = edtPassnew2.getText().toString();

        String link = getResources().getString(R.string.changePassShiperAPI);
        if ( pass.equals(repass)&&!pass.equals("")&&!repass.equals("")) {
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("CODE", response);
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("code");


                        if (code.equals("0")) {

                            Toast.makeText(getApplicationContext(), getString(R.string.sttthanhcong), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if(code.equals("1")) {

                       Toast.makeText(getApplicationContext(), getResources().getString(R.string.mkc), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ChangePassRequest save = new ChangePassRequest(token, passold, pass, repass, link, response);
            RequestQueue qe = Volley.newRequestQueue(getApplicationContext());
            qe.add(save);
        } else if (!pass.equals(repass)) {

            Toast.makeText(getApplicationContext(),getResources().getString(R.string.mkc), Toast.LENGTH_SHORT).show();
        }
        else if (pass.equals("")&&repass.equals("")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(),YourInformationActivity.class));
        finish();


    }
}
