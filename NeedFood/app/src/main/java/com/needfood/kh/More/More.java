package com.needfood.kh.More;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.Login;
import com.needfood.kh.More.History.MoreHistory;
import com.needfood.kh.R;
import com.needfood.kh.Setting.Setting;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class More extends Fragment implements View.OnClickListener {
    ImageView imgavt;

    LinearLayout lntranf, lnself, lnset, lnhis, lnparent, lncontac;

    String first;// this will contain "Fruit"
    String second;
    Button btnlog;
    View v;
    Session ses;
    TextView nameus;
    DataHandle db;
    List<InfoConstructor> list;
    String name;
    Context c;
    String token, id;

    public More() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ses = new Session(getContext());
        db = new DataHandle(getContext());

        v = inflater.inflate(R.layout.fragment_more, container, false);

         c = getActivity().getApplicationContext();
        imgavt = (ImageView) v.findViewById(R.id.avt);
        nameus = (TextView) v.findViewById(R.id.nameuser);
        lnhis = (LinearLayout) v.findViewById(R.id.history);
        lnhis.setOnClickListener(this);
        lnself = (LinearLayout) v.findViewById(R.id.lnself);
        lnset = (LinearLayout) v.findViewById(R.id.lnset);
        lntranf = (LinearLayout) v.findViewById(R.id.lntran);
        lnparent = (LinearLayout) v.findViewById(R.id.lnparent);
        lncontac = (LinearLayout) v.findViewById(R.id.lncont);
        lnset.setOnClickListener(this);
        lnself.setOnClickListener(this);
        lntranf.setOnClickListener(this);
        lnparent.setOnClickListener(this);
        lncontac.setOnClickListener(this);
        if (ses.loggedin()) {
            list = db.getAllInfor();
            Log.d("ABCCA", list.size() + "");
            if (list.size() > 0) {
                for (InfoConstructor it : list) {
                    name = it.getFullname();
                    nameus.setText(name);
                    token = it.getAccesstoken();
                    id = it.getId();
                }
            }

        }
//            Picasso.with(getContext()).load(R.drawable.logo).transform(new TransImage()).into(imgavt);
        addInfo();
//            v = inflater.inflate(R.layout.fragment_frag_log, container, false);
//            btnlog = (Button) v.findViewById(R.id.btnlog);
//            btnlog.setOnClickListener(this);

        // Inflate the layout for this fragment


        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (ses.loggedin()) {
            switch (id) {
                case R.id.lntran:
                    Intent itt = new Intent(getContext(), Tranfer.class);
                    startActivity(itt);
                    break;
                case R.id.lnself:
                    Intent it = new Intent(getContext(), MoreContanct.class);
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
                case R.id.lnparent:
                    Intent it4 = new Intent(getContext(), ParentUs.class);
                    startActivity(it4);
                    break;
                case R.id.lncont:
                    Intent it5 = new Intent(getContext(), Contact.class);
                    startActivity(it5);
                    break;
                case R.id.history:
                    Intent it6 = new Intent(getContext(), MoreHistory.class);
                    startActivity(it6);
                    break;
            }
        } else {
            switch (id) {
                case R.id.lntran:
                    taoMotAlertDialog2();
                    break;
                case R.id.lnself:
                    Intent it3 = new Intent(getContext(), Login.class);
                    startActivity(it3);
                    break;
                case R.id.lnset:
                    Intent it2 = new Intent(getContext(), Setting.class);
                    startActivity(it2);
                    break;

                case R.id.lnparent:
                    Intent it4 = new Intent(getContext(), ParentUs.class);
                    startActivity(it4);
                    break;
                case R.id.lncont:
                    Intent it5 = new Intent(getContext(), Contact.class);
                    startActivity(it5);
                    break;
                case R.id.history:
                    taoMotAlertDialog2();
                    break;
            }
        }

    }

    private AlertDialog taoMotAlertDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //Thiết lập tiêu đề hiển thị
        builder.setTitle(getResources().getString(R.string.er));
        //Thiết lập thông báo hiển thị

        builder.setMessage(getResources().getString(R.string.yhl));
        builder.setCancelable(false);
        //Tạo nút Chu hang
        builder.setNegativeButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
//
//    public void decode(String imageDecode) {
//        byte[] decodedString = Base64.decode(imageDecode, Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        Log.d("LOGBITMAP", decodedByte + "");
//        imgavt.setImageBitmap(decodedByte);
//    }

    private void addInfo() {
        String linkk = getResources().getString(R.string.linkgetinfo);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("idUseronl", id);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("LOGA", response);
                    JSONObject js = new JSONObject(response);
                    JSONObject jo = js.getJSONObject("Useronl");
                    String fullname = jo.getString("fullName");
                    String email = jo.getString("email");
                    String fone = jo.getString("fone");
                    String address = jo.getString("address");
                    String coin = jo.getString("coin");
                    String ava = jo.getString("avatar");

                    Picasso.with(c).load(ava).into(imgavt);
//                    StringTokenizer tokenss = new StringTokenizer(ava, ",");
//                    first = tokenss.nextToken();// this will contain "Fruit"
//                    second = tokenss.nextToken();
//                    decode(second);

                    db.updateinfo(fullname, email, address, id, coin);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getActivity());
        que.add(post);

    }
}
