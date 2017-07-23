package com.needfood.kh.Product;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.easyandroidanimations.library.SlideInUnderneathAnimation;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.needfood.kh.Adapter.ProductDetail.CommentAdapter;
import com.needfood.kh.Adapter.ProductDetail.OftenAdapter;
import com.needfood.kh.Adapter.ProductDetail.QuanAdapter;
import com.needfood.kh.Brand.BrandDetail;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.ProductDetail.CommentConstructor;
import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Constructor.ProductDetail.QuanConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rc, rcof, rcof2, rcquan;
    ArrayList<CommentConstructor> arr;
    String maniid, idsel;
    LinearLayout view1;
    ProgressBar pr1;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    CommentAdapter adapter;
    ImageView imgprd;
    String priceprd, prdcode, titl;
    Session ses;
    String uadr;
    private SimpleDateFormat dateFormatter, timeformat;
    Calendar c;
    int day, month2, year2, hour, minitus;
    public DatePickerDialog fromDatePickerDialog;
    public TimePickerDialog timepicker;
    EditText edpickngay, edpickgio, edquan, edadrs, edghichu;
    String cata;
    Button prev, bn;
    OftenAdapter adapterof1, adapterof2;
    TextView tvco, tvcodes, tvprize;
    TextView tvpr, namesel, tvnameprd, tvgia1, tvgia2, dess, tvdv1, tvdv2;
    ArrayList<OftenConstructor> arrof, arrof2;
    ArrayList<QuanConstructor> arrq;
    QuanAdapter quanadapter;
    LinearLayout lnby,lnf;
    List<ListMN> list;
    List<InfoConstructor> listu;
    DataHandle db;
    String idprd, idsl, namesl, access, idu, fullname, phone, bar;
    EditText txt_comment;
    ImageView img_comment,imglike,imgshare;
    RecyclerView re_comment;
    String comment;
    CallbackManager callbackManager;
    LikeView likeView;
    String cmt, time, iduser, fullnameus;
    ImageView imageView;
    ShareButton shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_product_detail);

        TextView txt = (TextView)findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.prddetail));

        khaibao();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       getProductDT();
                    }
                });
            }
        });
        th.start();



    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int year,
                              int month, int dayOfMonth) {
            c.set(year, month, dayOfMonth);
            year2 = year;
            month2 = month;
            day = dayOfMonth;
            edpickngay.setText(dateFormatter.format(c.getTime()));


        }
    };
    private TimePickerDialog.OnTimeSetListener timepic = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            edpickgio.setText(hourOfDay + ":" + minute);
        }
    };

    private void khaibao() {
        c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month2 = c.get(Calendar.MONTH);
        year2 = c.get(Calendar.YEAR);
        bn = (Button) findViewById(R.id.bn);
         shareButton = (ShareButton)findViewById(R.id.btnshare);
         likeView = (LikeView) findViewById(R.id.btnlike);
        imglike = (ImageView)findViewById(R.id.imglike);
        imgshare = (ImageView)findViewById(R.id.imgshare);
     likeView.setLikeViewStyle(LikeView.Style.STANDARD);


       // likeView.callOnClick();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minitus = c.get(Calendar.MINUTE);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormatter.format(c.getTime());
        edghichu = (EditText) findViewById(R.id.ghichu);
        timeformat = new SimpleDateFormat("HH:mm");
        String formattime = timeformat.format(c.getTime());
        db = new DataHandle(this);
        Intent it = getIntent();
        view1 = (LinearLayout) findViewById(R.id.v1);
        pr1 = (ProgressBar) findViewById(R.id.prg1);
        idprd = it.getStringExtra("idprd");
        lnf = (LinearLayout)findViewById(R.id.lnfb);

        edadrs = (EditText) findViewById(R.id.edadrship);
        Log.d("PRODUCTID",idprd);
        //edpick ngay
        edpickngay = (EditText) findViewById(R.id.pickngay);
        edpickngay.setInputType(InputType.TYPE_NULL);
        edpickngay.requestFocus();
        edpickngay.setOnClickListener(this);
        edpickngay.setText(formattedDate);
        //edpicgio
        edpickgio = (EditText) findViewById(R.id.pickgio);
        edpickgio.setInputType(InputType.TYPE_NULL);
        edpickgio.requestFocus();
        edpickgio.setOnClickListener(this);
        edpickgio.setText(formattime);

        fromDatePickerDialog = new DatePickerDialog(this, datePickerListener, year2, month2, day);
        timepicker = new TimePickerDialog(this, timepic, hour, minitus, true);
        edquan = (EditText) findViewById(R.id.edquan);
        ses = new Session(this);
        prev = (Button) findViewById(R.id.btnpre);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });
        if (ses.loggedin()) {
            listu = db.getAllInfor();
            access = listu.get(listu.size() - 1).getAccesstoken();
            idu = listu.get(listu.size() - 1).getId();
            fullname = listu.get(listu.size() - 1).getFullname();
            phone = listu.get(listu.size() - 1).getFone();
            uadr =  listu.get(listu.size() - 1).getAddress();
            edadrs.setText(uadr);

        }
        tvco = (TextView) findViewById(R.id.tvco);
        tvcodes = (TextView) findViewById(R.id.tvcodes);
        tvprize = (TextView) findViewById(R.id.tvprize);
        tvdv1 = (TextView) findViewById(R.id.donvi1);
        tvdv2 = (TextView) findViewById(R.id.donvi2);
        imgprd = (ImageView) findViewById(R.id.imgnews);
        arr = new ArrayList<>();
        namesel = (TextView) findViewById(R.id.namesl);

        txt_comment = (EditText) findViewById(R.id.txt_comment);
        img_comment = (ImageView) findViewById(R.id.img_comment);
        img_comment.setOnClickListener(this);

        tvnameprd = (TextView) findViewById(R.id.tvname2);
        tvgia1 = (TextView) findViewById(R.id.pr1);
        tvgia2 = (TextView) findViewById(R.id.pr2);
        dess = (TextView) findViewById(R.id.des);

        imageView = (ImageView) findViewById(R.id.imageView);
        arrof = new ArrayList<>();
        arrof2 = new ArrayList<>();
        arrq = new ArrayList<>();
        tvpr = (TextView) findViewById(R.id.tvpro);
        tvpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPro();
            }
        });
        lnby = (LinearLayout) findViewById(R.id.lnby);
        lnby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), BrandDetail.class);
                it.putExtra("ids", idsl);
                startActivity(it);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Howtouse.class);
                intent.putExtra("idp",idprd);
                startActivity(intent);
            }
        });
        rcquan = (RecyclerView) findViewById(R.id.rcquan);
        rc = (RecyclerView) findViewById(R.id.recycm);
        rcof = (RecyclerView) findViewById(R.id.rcprd);
        rcof2 = (RecyclerView) findViewById(R.id.rcprd2);

        adapter = new CommentAdapter(getApplicationContext(), arr);
        adapterof1 = new OftenAdapter(getApplicationContext(), arrof);
        adapterof2 = new OftenAdapter(getApplicationContext(), arrof2);
        quanadapter = new QuanAdapter(getApplicationContext(), arrq);

        rcof.setAdapter(adapterof1);
        rcof2.setAdapter(adapterof2);
        rcquan.setAdapter(quanadapter);
        rc.setAdapter(adapter);

        rcof.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcof2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcquan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);// (int spanCount, int orientation)
        rcof.setLayoutManager(mStaggeredVerticalLayoutManager);
        rcof2.setLayoutManager(mStaggeredVerticalLayoutManager2);

    }

    private void sendOrder() {
        String link = getResources().getString(R.string.linkorder);

        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        if (ses.loggedin()) {
            String quan;
            if(edquan.equals("")){
                quan="1";
            }else{
                quan=edquan.getText().toString();
            }
            int money1 = Integer.parseInt(quan)*Integer.parseInt(priceprd);
                JSONArray jsonArray = new JSONArray();
            JSONObject j1 = new JSONObject();

            try {
                j1.put("quantity", quan);
                j1.put("price", priceprd);
                j1.put("tickKM", "false");
                j1.put("tickKM_percent", null);
                j1.put("tickKM_money", null);
                j1.put("barcode", idprd);
                j1.put("code", prdcode);
                j1.put("title", titl);
                j1.put("money", money1 + "");
                j1.put("note", edghichu.getText().toString());
                j1.put("id", idprd);
                jsonArray.put(j1);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < OftenAdapter.arrcheck.size(); i++) {

                JSONObject jo = new JSONObject();

                try {
                    jo.put("quantity", OftenAdapter.arrcheck.get(i).getQuanli() + "");
                    jo.put("price", OftenAdapter.arrcheck.get(i).getPrice() + "");
                    jo.put("tickKM", OftenAdapter.arrcheck.get(i).getTickkm() + "");
                    jo.put("tickKM_percent", OftenAdapter.arrcheck.get(i).getTickkm2() + "");
                    jo.put("tickKM_money", OftenAdapter.arrcheck.get(i).getTickkm3() + "");
                    jo.put("barcode", OftenAdapter.arrcheck.get(i).getBarcode() + "");
                    jo.put("code", OftenAdapter.arrcheck.get(i).getCode() + "");
                    jo.put("title", OftenAdapter.arrcheck.get(i).getTitle() + "");
                    jo.put("money", OftenAdapter.arrcheck.get(i).getMoney() + "");
                    jo.put("note", OftenAdapter.arrcheck.get(i).getNote() + "");
                    jo.put("id", OftenAdapter.arrcheck.get(i).getId() + "");
                    jsonArray.put(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                Log.d("HAJAR",jsonArray.toString());

            int money = 0;
            String adr = edadrs.getText().toString();
            if(OftenAdapter.arrcheck.size()>0){
                for (int i = 0; i<OftenAdapter.arrcheck.size();i++){
                    money= Integer.parseInt(OftenAdapter.arrcheck.get(i).getMoney())+money;
                }
            }

            money = money+money1;
            Map<String,String> map = new HashMap<>();
            map.put("accessToken",access);
            map.put("listProduct",jsonArray.toString());

            map.put("money",money+"");
            map.put("totalMoneyProduct",(money*1.1)+"");
            map.put("moneyShip","");

            map.put("timeShiper",year2+"/"+month2+"/"+day+" "+edpickgio.getText().toString());
            map.put("fullName",fullname);
            map.put("address",adr);
            map.put("fone",phone);
           // map.put("idUseronl",idu);
            map.put("idSeller",idsl);
            map.put("note",edghichu.getText().toString());
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    pro.dismiss();
                    try {
                    JSONObject jo = new JSONObject(response);
                        String code = jo.getString("code");
                        if(code.equals("0")){
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.ssor),Toast.LENGTH_SHORT).show();
                        }else if(code.equals("-1")){
                            AlertDialog alertDialog = taoMotAlertDialog();
                            alertDialog.show();
                          //  Toast.makeText(getApplicationContext(),getResources().getString(R.string.lostss),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.er),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            PostCL get = new PostCL(link, map, response);
            RequestQueue que = Volley.newRequestQueue(getApplicationContext());
            que.add(get);
        } else {
            pro.dismiss();
            AlertDialog alertDialog = taoMotAlertDialog2();
            alertDialog.show();
        }

    }

    public void getProductDT() {
        final String link = getResources().getString(R.string.linkprdde);
        Map<String, String> map = new HashMap<>();
        map.put("idProduct", idprd);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    view1.setVisibility(View.VISIBLE);
                    new SlideInUnderneathAnimation(view1).setDuration(500).animate();
                    pr1.setVisibility(View.GONE);
                    Log.d("PDR", response);

                    JSONObject jo = new JSONObject(response);
                    JSONObject prd = jo.getJSONObject("Product");
                    if(!prd.getString("linkFacebook").equals("")){
                        lnf.setVisibility(View.VISIBLE);


                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(prd.getString("linkFacebook")))
                                .build();

                        shareButton.setShareContent(content);
                    }


                    cata = prd.getJSONArray("category").toString();
                    tvnameprd.setText(prd.getString("title"));
                    String tym = prd.getString("typeMoneyId");
                    String dvs = prd.getString("nameUnit");
                    titl = prd.getString("title");
                    namesl = prd.getString("nameSeller");
                    prdcode = prd.getString("code");
                    bar = prd.getString("barcode");
                    priceprd = prd.getString("price");
                    namesel.setText(namesl);
                    idsl = prd.getString("idSeller");
                    maniid = prd.getString("manufacturerId");
                    tvdv1.setText(dvs);
                    tvdv2.setText(dvs);
                    list = db.getMNid(tym);
                    for (ListMN lu : list) {
                        tvgia1.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("price"))) + lu.getMn());
                        tvgia2.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("priceOther"))) + lu.getMn(), TextView.BufferType.SPANNABLE);
                        Spannable spannable = (Spannable) tvgia2.getText();
                        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, tvgia2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvprize.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("price"))) + lu.getMn());
                    }
                    tvco.setText(prd.getString("code"));
                    tvcodes.setText(prd.getString("storeID"));

                    dess.setText(prd.getString("description"));
                    JSONArray ja = prd.getJSONArray("images");
                    Picasso.with(getApplicationContext()).load("http://needfood.webmantan.com" + ja.getString(0)).into(imgprd);

                    getPrdDK();
                    getCommen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void getAtach() {
        final String link = getResources().getString(R.string.linkprdat);

        Map<String, String> map = new HashMap<>();
        map.put("idProduct", idprd);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("AT", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++) {
                        String mn = "";
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject prd = jo.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");

                        arrq.add(new QuanConstructor(prd.getString("id"), "http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title")));

                    }
                    quanadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void getPrdDK() {
        final String link = getResources().getString(R.string.linkprdcata);

        Map<String, String> map = new HashMap<>();
        map.put("page", "1");
        map.put("limit", "5");
        map.put("category", cata);
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("RE", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++) {
                        String mn = "";
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject prd = jo.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");
                        String typemn = prd.getString("typeMoneyId");
                        list = db.getMNid(typemn);
                        for (ListMN lu : list) {
                            mn = lu.getMn();
                        }
                        arrof.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                prd.getString("price"), mn, prd.getString("nameUnit"), false, prd.getString("id"), "",
                                "", prd.getString("id")));
                    }

                    adapterof1.notifyDataSetChanged();
                    getPrdDH();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    private void getPrdDH() {
        final String link = getResources().getString(R.string.linkspch);

        Map<String, String> map = new HashMap<>();
        map.put("page", "1");
        map.put("limit", "5");
        map.put("manufacturerId", maniid);
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("RE", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0;i<ja.length();i++){
                        String mn = "";
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject prd = jo.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");
                            String typemn = prd.getString("typeMoneyId");
                            list = db.getMNid(typemn);
                            for (ListMN lu : list) {
                                mn = lu.getMn();
                            }

                            arrof2.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                    prd.getString("price"), mn, prd.getString("nameUnit"), false, prd.getString("id"), "",
                                    "", prd.getString("id")));
                    }
                    adapterof2.notifyDataSetChanged();
                       getAtach();
//                JSONObject jo = new JSONObject(response);
//                    String code = jo.getString("code");
//                    if(code.equals("0")){
//                        JSONArray ja = jo.getJSONArray("listData");
//                        for (int i = 0; i < ja.length(); i++) {
//                            String mn = "";
//                            JSONObject jo2 = ja.getJSONObject(i);
//                            JSONObject prd = jo2.getJSONObject("Product");
//                            JSONArray jaimg = prd.getJSONArray("images");
//                            String typemn = prd.getString("typeMoneyId");
//                            list = db.getMNid(typemn);
//                            for (ListMN lu : list) {
//                                mn = lu.getMn();
//                            }
//
//                            arrof2.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
//                                    prd.getString("price"), mn, prd.getString("nameUnit"), false, prd.getString("id"), "",
//                                    "", prd.getString("id")));
//
//                        }
//                        adapterof2.notifyDataSetChanged();
//                        getAtach();
//                    }else{
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);

    }

    public void saveComment() {
        final String link = getResources().getString(R.string.linksavecmpr);
        comment = txt_comment.getText().toString();
        if (comment.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.er), Toast.LENGTH_SHORT).show();
        }else if(!ses.loggedin()){
            AlertDialog alertDialog = taoMotAlertDialog2();
            alertDialog.show();
        }else {

            Map<String, String> map = new HashMap<>();
            map.put("accessToken", access);
            map.put("idProduct", idprd);
            map.put("comment", comment);
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject ja = new JSONObject(response);
                        String code = ja.getString("code");
                        if (code.equals("0")) {
                            txt_comment.setText("");
                            getCommen();
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
    }

    private void dialogPro() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.promotiondialog);
        dialog.show();
    }

    public void getCommen() {
        arr.clear();
        final String link = getResources().getString(R.string.linkgetcmpr);


        Map<String, String> map = new HashMap<>();
        map.put("idProduct", idprd);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("REEEE", response);
                try {
                    JSONObject ja = new JSONObject(response);
                    String code = ja.getString("code");
                    if (code.equals("0")) {
                        JSONArray jsonArray = ja.getJSONArray("listComment");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject js = jsonArray.getJSONObject(i);
                            cmt = js.getString("comment");
                            time = js.getString("time");
                            iduser = js.getString("idUseronl");
                            fullnameus = js.getString("fullNameUseronl");
                            arr.add(new CommentConstructor("", fullnameus, cmt, Long.parseLong(time)));
                            Collections.reverse(arr);
                        }
                        adapter.notifyDataSetChanged();

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

    @Override
    public void onClick(View v) {
        if (v == edpickngay) {
            fromDatePickerDialog.show();
        } else if (v == edpickgio) {
            timepicker.show();
        } else if (v == img_comment) {
            saveComment();
        }
    }
    private AlertDialog taoMotAlertDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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



    // hoi xoa
    private AlertDialog taoMotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // verify we're returning from like action
            if ("com.facebook.platform.action.request.LIKE_DIALOG".equals(data.getStringExtra("com.facebook.platform.protocol.PROTOCOL_ACTION"))) {
                // get action results
                Bundle bundle = data.getExtras().getBundle("com.facebook.platform.protocol.RESULT_ARGS");
                if (bundle != null) {
                    bundle.getBoolean("object_is_liked"); // liked/unliked
                    bundle.getInt("didComplete");
                    bundle.getInt("like_count"); // object like count
                    bundle.getString("like_count_string");
                    bundle.getString("social_sentence");
                    bundle.getString("completionGesture"); // liked/cancel/unliked
                }
            }
        }

    }
}
