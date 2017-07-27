package com.needfood.kh.More.History;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.needfood.kh.R;

public class MoreHistory extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lno, histr;

    Class cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_history);
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.his));


        lno = (LinearLayout) findViewById(R.id.hiso);
        lno.setOnClickListener(this);

        histr = (LinearLayout) findViewById(R.id.histr);
        histr.setOnClickListener(this);
    }

    public void ReplaceFrag(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.histr:
                Intent i = new Intent(getApplicationContext(), TransferHistory.class);
                startActivity(i);
                finish();
                break;
            case R.id.hiso:

                Intent it = new Intent(getApplicationContext(), OrderHistory.class);
                startActivity(it);
                finish();
                break;

        }
    }
}
