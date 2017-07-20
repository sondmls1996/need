package com.needfood.kh.Notif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.needfood.kh.Adapter.CommentNotifAdapter;
import com.needfood.kh.Constructor.CommentNotifConstructor;
import com.needfood.kh.R;

import java.util.ArrayList;

public class ReplyNotif extends AppCompatActivity implements View.OnClickListener {
    CardView cv;
    RelativeLayout rl;
    RecyclerView.LayoutParams p;
    RecyclerView rcrep;
    ArrayList<CommentNotifConstructor> arr;
    CommentNotifAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_notif);
        cv = (CardView)findViewById(R.id.rlcard);
        rcrep = (RecyclerView)findViewById(R.id.rcrep);
        p = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        arr = new ArrayList<>();
        adapter = new CommentNotifAdapter(getApplicationContext(),arr);
        rcrep.setAdapter(adapter);
        rcrep.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        arr.add(new CommentNotifConstructor("sd","sad",""));
        arr.add(new CommentNotifConstructor("sd","sad",""));
        arr.add(new CommentNotifConstructor("sd","sad",""));
        arr.add(new CommentNotifConstructor("sd","sad",""));
        arr.add(new CommentNotifConstructor("sd","sad",""));
        adapter.notifyDataSetChanged();
        rl = (RelativeLayout)findViewById(R.id.rlform);
        cv.setOnClickListener(this);
        rl.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rlcard:
                cv.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);

                break;
            case R.id.rlform:
                cv.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
                break;
        }
    }
}
