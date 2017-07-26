package com.nf.vi.needfoodshipper.MainClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.nf.vi.needfoodshipper.Request.ChangeInformationRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChangeInformation extends AppCompatActivity {
    private EditText edtEmail, edtFullname, edtBrithday, edtAddress, edtSkype, edtFacebook, edtDescription;
    private TextView tvTitle;
    private DBHandle db;
    private List<ListUserContructor> list;
    private String id, email, fullname, brithday, address, skype, facebook, description, token, email1, fullname1, brithday1, address1, skype1, facebook1, description1;
    private ProgressDialog progressDialog;
    private Button btnSaveChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);

        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            id = nu.getId();

            email = nu.getEmail();
            token = nu.getAccessToken();
            fullname = nu.getFullName();
            brithday = nu.getBirthday();
            address = nu.getAddress();
            skype = nu.getSkype();
            facebook = nu.getFacebook();
            description = nu.getDescription();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.ifchangetoobar));
        progressDialog = new ProgressDialog(this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtFullname = (EditText) findViewById(R.id.edtFullName);
        edtBrithday = (EditText) findViewById(R.id.edtBrithday);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtSkype = (EditText) findViewById(R.id.edtSkype);
        edtFacebook = (EditText) findViewById(R.id.edtFacebook);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        btnSaveChange = (Button) findViewById(R.id.btnSaveChange);


        edtEmail.setText(email);

        edtFullname.setText(fullname);
        edtBrithday.setText(brithday);
        edtAddress.setText(address);
        edtSkype.setText(skype);
        edtFacebook.setText(facebook);
        edtDescription.setText(description);

        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luu();
            }
        });
    }

    private void luu() {
        fullname1 = edtFullname.getText().toString();
        email1 = edtEmail.getText().toString();
        address1 = edtAddress.getText().toString();
        brithday1 = edtBrithday.getText().toString();
        skype1 = edtSkype.getText().toString();

        facebook1 = edtFacebook.getText().toString();
        description1 = edtDescription.getText().toString();

        String link = getResources().getString(R.string.updateInfoShiperAPI);

        if (fullname1.matches("")  || address1.matches("")  ) {
//            progressDialog.dismiss();
            Toast.makeText(getApplication(), "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = ProgressDialog.show(ChangeInformation.this, "Đang thay đổi", "Vui lòng chờ", true);
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String Code = jsonResponse.getString("code");
//                                Toast.makeText(getBaseContext(),Code,Toast.LENGTH_LONG).show();
                        if (Code.equals("0")) {
                            progressDialog.dismiss();
                            boolean update = db.updateinfo(id, fullname1, email1, address1, brithday1, skype1, facebook1, description1);
                            if (update == true) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
                            }
//                            Intent intent = new Intent(ChinhSuaTTActivity.this, ThongTinActivity.class);
//                            ChinhSuaTTActivity.this.startActivity(intent);
                            startActivity(new Intent(getBaseContext(), YourInformationActivity.class));
                        } else {
//                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangeInformation.this);
                            builder.setMessage("Thay đổi thất bại thất bại")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            };
            ChangeInformationRequest registerRequest = new ChangeInformationRequest(token, fullname1, email1, address1, brithday1, skype1, facebook1, description1, link, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ChangeInformation.this);
            queue.add(registerRequest);

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(),YourInformationActivity.class));


    }

}
