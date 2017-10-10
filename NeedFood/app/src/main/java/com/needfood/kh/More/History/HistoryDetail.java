package com.needfood.kh.More.History;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.needfood.kh.Adapter.PreAdapter;
import com.needfood.kh.Constructor.CheckVoteConstructor;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.PreConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryDetail extends AppCompatActivity {
    String idp, mn;
    RecyclerView rchis;
    TextView txtgia, txtdv, texttime, textship, txttax, txtadr, textshiptime, txttotal;
    ArrayList<PreConstructor> arr;
    LinearLayout shipvote, ln1, ln2;
    Button shiplo;
    GoogleMap mMap;
    View v;
    List<InfoConstructor> listu;
    String idprd, idsl, namesl, access, idu, fullname, fullnamee, phone, bar, priceother;
    private View rootView;
    List<ListMN> listmn;
    Context context;
    DataHandle db;
    PreAdapter adapter;
    String idshipper;
    Session ses;
    String status;
    double lat, lo;
    String dia_comment;
    String idshiper;
    String idseller;
    String res;
    RelativeLayout votee;
    String mscode;
    List<CheckVoteConstructor> list1, list2;
    RatingBar ratingBar1, ratingBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        ImageView imgr = (ImageView) findViewById(R.id.reimg);
        imgr.setVisibility(View.VISIBLE);
        imgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReport();
            }
        });
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
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
        list1 = db.getCheckVoteS();
        list2 = db.getCheckVoteSh();
        arr = new ArrayList<>();
        Intent it = getIntent();
        idp = it.getStringExtra("idp");
        txtgia = (TextView) findViewById(R.id.mntong);
        txtdv = (TextView) findViewById(R.id.dvtong);
        texttime = (TextView) findViewById(R.id.retime);
        txtadr = (TextView) findViewById(R.id.txtadr);
        textship = (TextView) findViewById(R.id.shipmnn);
        txttax = (TextView) findViewById(R.id.taxx);
        shiplo = (Button) findViewById(R.id.shiploa);
        shipvote = (LinearLayout) findViewById(R.id.shipvote);
        ln1 = (LinearLayout) findViewById(R.id.linner);
        ln2 = (LinearLayout) findViewById(R.id.linner1);
        textshiptime = (TextView) findViewById(R.id.retimee);
        txttotal = (TextView) findViewById(R.id.totalmoney);
        votee = (RelativeLayout) findViewById(R.id.votee);
        ratingBar1 = (RatingBar) findViewById(R.id.ratingpro);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingship);
        if (ses.loggedin()) {
            listu = db.getAllInfor();
            access = listu.get(listu.size() - 1).getAccesstoken();
            idu = listu.get(listu.size() - 1).getId();
            fullname = listu.get(listu.size() - 1).getFullname();
            phone = listu.get(listu.size() - 1).getFone();

        }
        rchis = (RecyclerView) findViewById(R.id.lvpre);
        adapter = new PreAdapter(getApplicationContext(), arr);
        rchis.setAdapter(adapter);
        rchis.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getData();
        shiplo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShip();
            }
        });
    }


    private void getData() {
        String link = getResources().getString(R.string.linkInforo);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", access);
        map.put("idOrder", idp);
        Log.d("RRRR", access + "-" + idp);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RRRR", response);
                try {
                    JSONObject joo = new JSONObject(response);
                    JSONObject jo = joo.getJSONObject("Order");
                    JSONArray list = jo.getJSONArray("listProduct");
                    JSONObject ino = jo.getJSONObject("infoOrder");
                    int total = ino.getInt("totalMoneyProduct");
                    mscode = jo.getString("code");
                    idseller = jo.getString("idSellerBoss");
                    JSONObject incus = jo.getJSONObject("infoCustomer");
                    status = jo.getString("status");
                    String price = ino.getString("moneyProduct");
                    idshipper = ino.getString("idShiper");
                    String tax = ino.getString("percentTaxAll");
                    String times = ino.getString("timeShiper");
                    String mns = ino.getString("moneyShip");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject idx = list.getJSONObject(i);
                        listmn = db.getMNid(idx.getString("typeMoneyId"));
                        for (ListMN lu : listmn) {
                            mn = lu.getMn();
                        }
                        if(!idx.getString("quantity").equals("0")){
                            arr.add(new PreConstructor(idx.getString("title"), idx.getString("quantity"),
                                    idx.getString("money"), mn));
                        }

                    }
                    getLocalShipper();
                    adapter.notifyDataSetChanged();
                    txtadr.setText(incus.getString("address"));
                    txtgia.setText(NumberFormat.getNumberInstance(Locale.UK).format(total) + "");
                    txttax.setText(price + " " + mn);
                    texttime.setText(mscode);
                    textship.setText(mns + " " + mn);
                    textshiptime.setText(times);
                    txttotal.setText(total + " " + mn);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                dialogVote();
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).getId().equals(mscode)) {
                        ln2.setVisibility(View.GONE);
                    }
                }

                for (int k = 0; k < list2.size(); k++) {
                    if (list2.get(k).getId().equals(mscode)) {
                        ln1.setVisibility(View.GONE);
                    }
                }

                Log.d("CHECKDATA", status);
                if (status.equals("done")) {
                    shiplo.setVisibility(View.GONE);
                    votee.setVisibility(View.VISIBLE);
                } else if (status.equals("waiting") && !idshipper.equals("")) {
                    shiplo.setVisibility(View.VISIBLE);
                    votee.setVisibility(View.GONE);
                } else {
                    shiplo.setVisibility(View.GONE);
                    votee.setVisibility(View.GONE);
                }
            }
        };
        PostCL po = new PostCL(link, map, response);
        RequestQueue re = Volley.newRequestQueue(getApplicationContext());
        re.add(po);
    }

    public void getReport() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.htucontent1);
        dialog.show();
        TextView txtht = (TextView) dialog.findViewById(R.id.txtht);
        TextView txtvote = (TextView) dialog.findViewById(R.id.txt_votee);
        final RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        final EditText edtcoment = (EditText) dialog.findViewById(R.id.dia_comment);
        ratingbar.setVisibility(View.GONE);
        txtvote.setVisibility(View.GONE);

        Button bbtn1 = (Button) dialog.findViewById(R.id.button1);
        bbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(HistoryDetail.this);
                progressDialog.setMessage(getResources().getString(R.string.wait));
                progressDialog.show();
                String link = getResources().getString(R.string.linkReportSeller);
                dia_comment = edtcoment.getText().toString();
                if (dia_comment.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("idSeller", idseller);
                    map.put("accessToken", access);
                    map.put("comment", dia_comment);
                    Response.Listener<String> response = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("checkcc", response);
                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString("code");
                                if (code.equals("0")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.tkre), Toast.LENGTH_SHORT).show();
                                    dialog.cancel();

                                } else if (code.equals("-1")) {
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                    AlertDialog alertDialog = taoMotAlertDialog();
                                    alertDialog.show();
                                } else {
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PostCL po = new PostCL(link, map, response);
                    RequestQueue re = Volley.newRequestQueue(getApplicationContext());
                    re.add(po);
                }
            }
        });
    }

    private void dialogShip() {
        Dialog dialog = new Dialog(HistoryDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.maplayout);
        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(HistoryDetail.this);
        Log.d("CHECKSIZE", lat + "-" + lo);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.clear();
                LatLng latLng = new LatLng(lat, lo);
                if (mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()).title(fullnamee).snippet("Shipper"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    private void dialogVote() {
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final String link = getResources().getString(R.string.linkvotshiper);
                Map<String, String> map = new HashMap<>();
                map.put("idShiper", idshipper);
                map.put("accessToken", access);
                map.put("point", String.valueOf(rating));

                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("EEE", response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            String codez = jo.getString("code");
                            if (codez.equals("0")) {
                                db.addCheckVoteSH(new CheckVoteConstructor(mscode));
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();

                            } else if (codez.equals("-1")) {
                                AlertDialog alertDialog = taoMotAlertDialog();
                                alertDialog.show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PostCL get = new PostCL(link, map, response);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                que.add(get);
            }

        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final String link = getResources().getString(R.string.linkvoteseller);

                Map<String, String> map = new HashMap<>();
                map.put("idSeller", idseller);
                map.put("accessToken", access);
                map.put("point", String.valueOf(rating));

                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("EEE", response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString("code");
                            if (code.equals("0")) {
                                db.addCheckVoteSe(new CheckVoteConstructor(mscode));
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.succ), Toast.LENGTH_SHORT).show();

                            } else if (code.equals("-1")) {
                                AlertDialog alertDialog = taoMotAlertDialog();
                                alertDialog.show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PostCL get = new PostCL(link, map, response);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                que.add(get);

            }
        });


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
                res = response;
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject jos = jo.getJSONObject("Shiper");
                    fullnamee = jos.getString("fullName");
                    String fone = jos.getString("fone");
                    lat = jos.getDouble("latGPS");
                    lo = jos.getDouble("longGPS");
                    idshiper = jos.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL post = new PostCL(linkk, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(post);

    }

    private AlertDialog taoMotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryDetail.this);
        //Thiết lập tiêu đề hiển thị
        builder.setTitle(getResources().getString(R.string.er));
        //Thiết lập thông báo hiển thị

        builder.setMessage(getResources().getString(R.string.lostss));
        builder.setCancelable(false);
        //Tạo nút Chu hang
        builder.setNegativeButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        db.deleteInfo();
                        db.deleteAll();
                        ses = new Session(getBaseContext());
                        ses.setLoggedin(false);
                        Intent i = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}