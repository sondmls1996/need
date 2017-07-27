package com.nf.vi.needfoodshipper.MainClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.SentPassNewRequest;


import org.json.JSONException;
import org.json.JSONObject;

public class SentPassEmail extends AppCompatActivity implements View.OnClickListener {
    private String fone;
    private EditText edtcode;
    private Button btnNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_pass_email);
        edtcode = (EditText) findViewById(R.id.edtcode);
        btnNewPass = (Button) findViewById(R.id.btnNewPass);
        btnNewPass.setOnClickListener(this);
        Intent iten = getIntent();
        fone = iten.getStringExtra("fone");
        Toast.makeText(getBaseContext(), fone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnNewPass) {
            String code = edtcode.getText().toString();
            String link = getResources().getString(R.string.sendPassShiperAPI);
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("code");
                        Log.d("CODE", code);

                        if (code.equals("0")) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SentPassEmail.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            SentPassNewRequest glq = new SentPassNewRequest(fone, code, link, response);
            RequestQueue qe = Volley.newRequestQueue(getApplication());
            qe.add(glq);
        }

    }
}
