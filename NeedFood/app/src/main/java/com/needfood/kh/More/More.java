package com.needfood.kh.More;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.Login;
import com.needfood.kh.More.History.MoreHistory;
import com.needfood.kh.R;
import com.needfood.kh.Setting.Setting;
import com.needfood.kh.SupportClass.Session;
import com.needfood.kh.SupportClass.TransImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class More extends Fragment implements View.OnClickListener {
    ImageView imgavt;

    LinearLayout lntranf,lnself,lnset,lnhis;



    Button btnlog;
    View v;
    Session ses;
    TextView nameus;
    DataHandle db;
    List<InfoConstructor>list;
    String name;
    public More() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ses = new Session(getContext());
        db = new DataHandle(getContext());


        if(ses.loggedin()){
             v = inflater.inflate(R.layout.fragment_more, container, false);
            list = db.getAllInfor();
            for (InfoConstructor it : list){
                name = it.getFullname();
            }
            imgavt = (ImageView)v.findViewById(R.id.avt);
            nameus = (TextView) v.findViewById(R.id.nameuser);
            lnhis = (LinearLayout)v.findViewById(R.id.history);
            lnhis.setOnClickListener(this);
            lnself =(LinearLayout)v.findViewById(R.id.lnself);
            lnset = (LinearLayout)v.findViewById(R.id.lnset);
            lntranf =(LinearLayout)v.findViewById(R.id.lntran);
            lnset.setOnClickListener(this);
            lnself.setOnClickListener(this);
            lntranf.setOnClickListener(this);
            nameus.setText(name);
            Picasso.with(getContext()).load(R.drawable.logo).transform(new TransImage()).into(imgavt);
        }else{
             v = inflater.inflate(R.layout.fragment_frag_log, container, false);
            btnlog = (Button)v.findViewById(R.id.btnlog);
            btnlog.setOnClickListener(this);
        }
        // Inflate the layout for this fragment


        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.lntran:
                Intent itt = new Intent(getContext(),Tranfer.class);
                startActivity(itt);
                break;
            case R.id.lnself:
                Intent it = new Intent(getContext(),MoreContanct.class);
                startActivity(it);
                break;
            case R.id.lnset:
                Intent it2 = new Intent(getContext(), Setting.class);
                startActivity(it2);
                break;
            case R.id.btnlog:
                Intent it3 = new Intent(getContext(), Login.class);
                startActivity(it3);
                break;
            case R.id.history:
                Intent it4 = new Intent(getContext(), MoreHistory.class);
                startActivity(it4);
                break;
        }
    }
}
