package com.needfood.kh.More.History;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.needfood.kh.R;

public class MoreHistory extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lno;
    Class cl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_history);
        lno = (LinearLayout)findViewById(R.id.hiso);
        lno.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.hiso:
                Intent it = new Intent(getApplicationContext(),OrderHistory.class);
                startActivity(it);
                break;

        }
    }
}
