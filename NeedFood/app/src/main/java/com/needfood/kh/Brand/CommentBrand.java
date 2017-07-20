package com.needfood.kh.Brand;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Adapter.ProductDetail.CommentAdapter;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ProductDetail.CommentConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentBrand extends Fragment {
    String idl;
    RecyclerView rc;
    CommentAdapter adapter;
    ImageView imgsend;
    TextView txtcmt;
    ProgressBar prg;
    EditText edcmt;
    Session ses;
    DataHandle db;
    List<InfoConstructor> list;
    ArrayList<CommentConstructor> arr;
    public CommentBrand() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BrandDetail activity = (BrandDetail) getActivity();
        idl = activity.getMyData();
        Log.d("IDSL",idl);
        ses= new Session(getContext());
        db = new DataHandle(getContext());


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comment_brand, container, false);
        arr = new ArrayList<>();
        adapter = new CommentAdapter(getContext(),arr);
        prg = (ProgressBar)v.findViewById(R.id.prgcmt);
        rc = (RecyclerView)v.findViewById(R.id.rccmt);
        txtcmt = (TextView)v.findViewById(R.id.txtcmt);
        rc.setAdapter(adapter);
        imgsend = (ImageView)v.findViewById(R.id.imgSearch);
        edcmt = (EditText)v.findViewById(R.id.txtComment);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMT();
            }
        });
        getComment();

        return v;
    }

    private void sendCMT() {
        if (ses.loggedin()){
            list = db.getAllInfor();
            final String link = getResources().getString(R.string.linksavecmt);
            final String cmt = edcmt.getText().toString();
            arr.clear();
            Map<String,String> map = new HashMap<>();
            map.put("idSeller",idl);
            map.put("accessToken",list.get(list.size()-1).getAccesstoken());
            map.put("comment",cmt);
            if(cmt.equals("")){
                Toast.makeText(getApplicationContext(),"Hãy ghi comment",Toast.LENGTH_SHORT).show();
            }else{
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("SendCMT",response);
                        edcmt.setText(null);
                        getComment();
                    }
                };
                PostCL get = new PostCL(link,map,response);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                que.add(get);
            }

        }else{

        }
    }

    private void getComment() {
        prg.setVisibility(View.VISIBLE);
        txtcmt.setVisibility(View.GONE);
        rc.setVisibility(View.GONE);
        final String link = getResources().getString(R.string.linkgetcmt);

        Map<String,String> map = new HashMap<>();
        map.put("idSeller",idl);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Comment",response);
                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString("code");
                    if(code.equals("0")){

                        JSONArray ja = jo.getJSONArray("listComment");
                        if(ja.length()==0){
                            prg.setVisibility(View.GONE);
                            txtcmt.setVisibility(View.VISIBLE);
                        }else {
                            for (int i = 0; i<ja.length();i++){
                                JSONObject idx = ja.getJSONObject(i);
                                arr.add(new CommentConstructor("https://s-media-cache-ak0.pinimg.com/736x/e9/10/0c/e9100c175a5e1e7dc2034a66f0c73a86.jpg",
                                        idx.getString("fullNameUseronl"),idx.getString("comment"),idx.getLong("time")));

                            }
                            adapter.notifyDataSetChanged();
                            prg.setVisibility(View.GONE);
                            rc.setVisibility(View.VISIBLE);
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link,map,response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

}
