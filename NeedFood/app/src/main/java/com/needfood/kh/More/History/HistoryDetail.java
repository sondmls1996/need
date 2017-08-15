package com.needfood.kh.More.History;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.needfood.kh.Adapter.PreAdapter;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.PreConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryDetail extends AppCompatActivity implements OnMapReadyCallback {
    String js,mn;
    RecyclerView rchis;
    TextView txtgia,txtdv,texttime,textship,txttax,txtadr;
    ArrayList<PreConstructor> arr;
    LinearLayout shiplo;
    GoogleMap mMap;
    View v;
    List<InfoConstructor> listu;
    String idprd, idsl, namesl, access, idu, fullname, phone, bar, priceother;
    private View rootView;
    List<ListMN> listmn;
    Context context;
    DataHandle db;
    SupportMapFragment mapFragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    PreAdapter adapter;
    String idshipper;
    Session ses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        ImageView imgb = (ImageView)findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = this;

        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.orif));
        db = new DataHandle(this);
        ses = new Session(this);
       // v = (View) LayoutInflater.from(this).inflate(R.layout.maplayout, null);

        txtgia=(TextView) findViewById(R.id.mntong);
        txtdv=(TextView) findViewById(R.id.dvtong);
        texttime = (TextView)findViewById(R.id.retime);
        txtadr = (TextView)findViewById(R.id.txtadr);
        textship = (TextView)findViewById(R.id.shipmn);
        txttax = (TextView)findViewById(R.id.taxx);
        shiplo = (LinearLayout)findViewById(R.id.shiplo);
        shiplo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShip();
            }
        });
        if(ses.loggedin()){
            listu = db.getAllInfor();
            access = listu.get(listu.size() - 1).getAccesstoken();
            idu = listu.get(listu.size() - 1).getId();
            fullname = listu.get(listu.size() - 1).getFullname();
            phone = listu.get(listu.size() - 1).getFone();
        }

        Intent it = getIntent();
        js = it.getStringExtra("js");
        rchis = (RecyclerView)findViewById(R.id.lvpre);
        arr = new ArrayList<>();
        adapter = new PreAdapter(getApplicationContext(),arr);
        rchis.setAdapter(adapter);
        rchis.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.d("IDSHIPPER",js);
        try {
            JSONObject jo = new JSONObject(js);
            JSONArray list = jo.getJSONArray("listProduct");
            JSONObject ino = jo.getJSONObject("infoOrder");
            int total = ino.getInt("totalMoneyProduct");

            JSONObject incus = jo.getJSONObject("infoCustomer");
            idshipper = ino.getString("idShiper");
            String tax = ino.getString("percentTaxAll");
            String times = ino.getString("timeShiper");
            String mns = ino.getString("moneyShip");
            for (int i=0;i<list.length();i++){
                JSONObject idx = list.getJSONObject(i);
                listmn = db.getMNid(idx.getString("typeMoneyId"));
                for (ListMN lu:listmn){
                    mn=lu.getMn();
                }
                arr.add(new PreConstructor(idx.getString("title"), idx.getString("quantity"), idx.getString("money"), mn));
            }
            adapter.notifyDataSetChanged();
            txtadr.setText(incus.getString("address"));
            txtgia.setText(NumberFormat.getNumberInstance(Locale.UK).format(total)+"");

        //    txtdv.setText(mn +" ("+tax+"%"+" VAT"+")");
            txttax.setText(tax+"%");
            texttime.setText(times);
            textship.setText(mns+" "+mn);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        getLocalShipper();
    }

    private void dialogShip() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.maplayout);
        dialog.show();
        GoogleMap googleMap;



        MapsInitializer.initialize(this);

        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void getLocalShipper() {
        String linkk = getResources().getString(R.string.linkshipper);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", access);
        map.put("idShiper", idshipper);
        Response.Listener<String> response = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("LOGA", response);

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);

    }

}
