package com.nf.vi.needfoodshipper.MainClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.database.DBHandle;

import java.util.List;

public class YourInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvChangPass;
    private TextView tvTitle, tvPhone, tvEmail, tvCode, tvFullname, tvBrithday, tvAddress, tvSkype, tvFacebook, tvDescription;
    private DBHandle db;
    private List<ListUserContructor> list;
    private String id, phone, email, code1, fullname, brithday, address, skype, facebook, description;
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_information);

        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            id = nu.getId();
            phone = nu.getFone();
            email = nu.getEmail();
            code1 = nu.getCode();
            fullname = nu.getFullName();
            brithday = nu.getBirthday();
            address = nu.getAddress();
            skype = nu.getSkype();
            facebook = nu.getFacebook();
            description = nu.getDescription();

        }

//        setTitle("YOUR INFORMATION");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.iftoobar));

        tvChangPass = (TextView) findViewById(R.id.tvChangPass);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvFullname = (TextView) findViewById(R.id.tvFullName);
        tvBrithday = (TextView) findViewById(R.id.tvBrithday);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvSkype = (TextView) findViewById(R.id.tvSkype);
        tvFacebook = (TextView) findViewById(R.id.tvFacebook);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnEdit = (Button) findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeInformation.class));
                finish();
            }
        });

        tvPhone.setText(phone);
        tvEmail.setText(email);
        tvCode.setText(code1);
        tvFullname.setText(fullname);
        tvBrithday.setText(brithday);
        tvAddress.setText(address);
        tvSkype.setText(skype);
        tvFacebook.setText(facebook);
        tvDescription.setText(description);


        tvChangPass.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == tvChangPass) {
            startActivity(new Intent(this, ChangePassActivity.class));
            finish();
        }


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(),MainActivity.class));
        finish();


    }
}
