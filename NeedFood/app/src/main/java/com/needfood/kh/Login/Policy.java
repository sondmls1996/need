package com.needfood.kh.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.needfood.kh.R;

public class Policy extends AppCompatActivity implements View.OnClickListener {
    LinearLayout upl,selpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.poli));
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upl = (LinearLayout)findViewById(R.id.upl);
        selpl = (LinearLayout)findViewById(R.id.selpl);
        upl.setOnClickListener(this);
        selpl.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.upl:
                Intent it = new Intent(getApplicationContext(),DetailPolicy.class);
                it.putExtra("stt","u");
                startActivity(it);
                break;
            case R.id.selpl:
                Intent it2 = new Intent(getApplicationContext(),DetailPolicy.class);
                it2.putExtra("stt","s");
                startActivity(it2);
                break;

        }
    }
}
