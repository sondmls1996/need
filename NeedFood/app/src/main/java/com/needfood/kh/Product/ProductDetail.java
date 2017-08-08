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
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.needfood.kh.Adapter.ProductDetail.CommentAdapter;
import com.needfood.kh.Adapter.ProductDetail.OftenAdapter;
import com.needfood.kh.Brand.BrandDetail;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.ProductDetail.CommentConstructor;
import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Login.Login;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;
import com.needfood.kh.SupportClass.VerticalScrollview;
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

import static com.needfood.kh.Adapter.ProductDetail.OftenAdapter.arrcheck;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rc, rcof, rcof2, rcquan, rctp;
    ArrayList<CommentConstructor> arr;
    String maniid, idsel, mnid, hot = "";
    LinearLayout view1;
    int a2;
    int numshare = 0;
    ProgressBar pr1;
    String tym;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    CommentAdapter adapter;
    ImageView imgprd;
    public static String priceprd;
    String prdcode, titl;
    Session ses;
    String uadr, tax;
    String typemn;
    private SimpleDateFormat dateFormatter, timeformat;
    Calendar c;
    int day, month2, year2, hour, minitus;
    public DatePickerDialog fromDatePickerDialog;
    public TimePickerDialog timepicker;
    EditText edpickngay, edpickgio, edquan, edadrs, edghichu;
    String cata, sellprice = "";
    Button deal, bn;
    OftenAdapter adapterof1, adapterof2, adapterof3;
    TextView tvco, tvcodes, tvprize, tvphi, tvmyphi;
    TextView tvpr, namesel, tvnameprd, shipm, tvgia1, tvgia2, dess, tvdv1, tvdv2;
    ArrayList<OftenConstructor> arrof, arrof2, arrdelete, arrof3;
    ArrayList<OftenConstructor> arrq;
    public static ArrayList<Integer> listship;
    OftenAdapter quanadapter;
    LinearLayout lnby, lnf, htu, lnshare, lnmyshare, lnprd;
    List<ListMN> list;
    String discount, discountStart, discountEnd, text1, text2, time1, time2, priceDiscount, discountCode;
    List<InfoConstructor> listu;
    DataHandle db;
    String idprd, idsl, namesl, access, idu, fullname, phone, bar, priceother;
    EditText txt_comment;
    ImageView img_comment, imglike;
    Button imgshare;
    RecyclerView re_comment;
    String comment;
    VerticalScrollview ver;
    ShareLinkContent content;
    CallbackManager callbackManager;
    LikeView likeView;
    StringBuilder howto, simg, strship;
    String cmt, time, iduser, fullnameus;
    ImageView imageView, next, down;
    ShareDialog shareDialog;
    ShareButton shareButton;
    int hieu;
    String linkfbb, sttsell = "";
    Button sharee;
    TextView vote;
    String point;
    TextView nameseller, exp, txtcomp, txtof, txtbrand;
    LinearLayout lnpro;
    boolean checkclick = false;
    String sta = "";
    double percentkm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_product_detail);

        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.prddetail));
        ver = (VerticalScrollview)findViewById(R.id.vers);
        listship = new ArrayList<>();
        txtof = (TextView) findViewById(R.id.txtoften);
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        khaibao();
        getProductDT();


        //  getCommen();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(ProductDetail.this, BubbleService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(ProductDetail.this, BubbleService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int prdmoney=0;
//        for (int i2 = 0; i2<OftenAdapter.arrcheck.size();i2++){
//            prdmoney = Integer.parseInt(OftenAdapter.arrcheck.get(i2).getMoney())+ prdmoney;
//        }
//        Intent it = new Intent(getApplicationContext(),BubbleService.class);
//        it.putExtra("MN",prdmoney+Integer.parseInt(priceprd)+"");
//        startService(it);
        getNumberShare();
    }


    private void khaibao() {
        db = new DataHandle(this);
        Intent it = getIntent();
        view1 = (LinearLayout) findViewById(R.id.v1);
        pr1 = (ProgressBar) findViewById(R.id.prg1);
        idprd = it.getStringExtra("idprd");
        if (it.hasExtra("sell")) {
            sttsell = it.getStringExtra("sell");
        }
        if (it.hasExtra("hot")) {
            hot = it.getStringExtra("hot");
        }

        OftenAdapter.arrcheck.clear();
        bn = (Button) findViewById(R.id.bn);
        lnshare = (LinearLayout) findViewById(R.id.lnshare);

        lnshare = (LinearLayout) findViewById(R.id.lnshare);
        lnmyshare = (LinearLayout) findViewById(R.id.lnmhysh);
        imgshare = (Button) findViewById(R.id.imgshare);
        tvphi = (TextView) findViewById(R.id.phi);
        txtbrand = (TextView) findViewById(R.id.txtbrand);
        tvmyphi = (TextView) findViewById(R.id.myphi);
        shareDialog = new ShareDialog(ProductDetail.this);
        lnpro = (LinearLayout) findViewById(R.id.id_info_pro);
        next = (ImageView) findViewById(R.id.nextt);
        down = (ImageView) findViewById(R.id.downn);
        lnprd = (LinearLayout) findViewById(R.id.prd);
        lnprd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkclick == false) {
                    checkclick = true;
                    down.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                    lnpro.setVisibility(View.VISIBLE);
                } else {
                    checkclick = false;
                    down.setVisibility(View.GONE);
                    lnpro.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                }

            }
        });

        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnFB();
            }
        });


        htu = (LinearLayout) findViewById(R.id.htu);
        htu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Howtouse.class);
                it.putExtra("htu", howto.toString());
                it.putExtra("img", simg.toString());
                it.putExtra("tit", titl);
                it.putExtra("idpr", idprd);
                it.putExtra("idsl", idsl);
                startActivity(it);
            }
        });
        // likeView.callOnClick();
        edquan = (EditText) findViewById(R.id.edquan);
        ses = new Session(this);

        deal = (Button) findViewById(R.id.btndeal);
        deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senKM2("9000", "0", "deal", numshare);
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
            uadr = listu.get(listu.size() - 1).getAddress();
            //  imgshare.setVisibility(View.VISIBLE);
            lnshare.setVisibility(View.VISIBLE);

        }

        tvco = (TextView) findViewById(R.id.tvco);
        tvcodes = (TextView) findViewById(R.id.tvcodes);
        tvprize = (TextView) findViewById(R.id.tvprize);
        tvdv1 = (TextView) findViewById(R.id.donvi1);

        nameseller = (TextView) findViewById(R.id.nameseller);
        exp = (TextView) findViewById(R.id.exp);

        tvdv2 = (TextView) findViewById(R.id.donvi2);
        imgprd = (ImageView) findViewById(R.id.imgnews);
        arr = new ArrayList<>();
        namesel = (TextView) findViewById(R.id.namesl);

        vote = (TextView) findViewById(R.id.vote);
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
        arrof3 = new ArrayList<>();
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

        rcquan = (RecyclerView) findViewById(R.id.rcquan);
        rc = (RecyclerView) findViewById(R.id.recycm);
        rcof = (RecyclerView) findViewById(R.id.rcprd);
        rcof2 = (RecyclerView) findViewById(R.id.rcprd2);
        rctp = (RecyclerView) findViewById(R.id.rcprd3);

        adapter = new CommentAdapter(getApplicationContext(), arr);
        adapterof1 = new OftenAdapter(getApplicationContext(), arrof);
        adapterof2 = new OftenAdapter(getApplicationContext(), arrof2);
        quanadapter = new OftenAdapter(getApplicationContext(), arrq);
        adapterof3 = new OftenAdapter(getApplicationContext(), arrof3);

        rcof.setAdapter(adapterof1);
        rcof2.setAdapter(adapterof2);
        rcquan.setAdapter(quanadapter);
        rc.setAdapter(adapter);
        rctp.setAdapter(adapterof3);

        rcof.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcof2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcquan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rctp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager3 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);// (int spanCount, int orientation)
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager4 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rcof.setLayoutManager(mStaggeredVerticalLayoutManager);
        rcof2.setLayoutManager(mStaggeredVerticalLayoutManager2);
        rcquan.setLayoutManager(mStaggeredVerticalLayoutManager3);
        rctp.setLayoutManager(mStaggeredVerticalLayoutManager4);
    }

    private void getNumberShare() {
        String link = getResources().getString(R.string.linkgetnum);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", access);
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("{\"code\":-1}")) {
                    AlertDialog alertDialog = taoMotAlertDialog();
                    alertDialog.show();
                } else {
                    a2 = Integer.parseInt(response);
                    Log.d("TTT", a2 + "");
                    tvmyphi.setText(a2 + "");
                    if (a2 >= numshare && numshare != 0) {

                        deal.setVisibility(View.VISIBLE);
                    } else {
                        deal.setVisibility(View.GONE);
                    }

                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }


    private void shareOnFB() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(linkfbb))
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag("#NeedFood")
                            .build())
                    .build();


            shareDialog.show(content);
        }
    }

    private void sendOrder() {

        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        if (ses.loggedin()) {
            String quan;
            int mnship = Collections.max(listship);
            if (edquan.equals("")) {
                quan = "1";
            } else {
                quan = edquan.getText().toString();
            }
            int money1 = Integer.parseInt(quan) * Integer.parseInt(priceprd);
            JSONArray jsonArray = new JSONArray();
            JSONObject j1 = new JSONObject();

            try {
                j1.put("quantity", quan);
                j1.put("price", priceprd);
                j1.put("tickKM", "false");
                j1.put("tickKM_percent", "");
                j1.put("tickKM_money", "");
                j1.put("barcode", idprd);
                j1.put("code", prdcode);
                j1.put("title", titl);
                j1.put("money", money1 + "");
                j1.put("note", "");
                j1.put("id", idprd);
                j1.put("typeMoneyId", tym);
                jsonArray.put(j1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrcheck.size() > 0) {
                for (int i = 0; i < arrcheck.size(); i++) {

                    JSONObject jo = new JSONObject();

                    try {
                        jo.put("quantity", arrcheck.get(i).getQuanli() + "");
                        jo.put("price", arrcheck.get(i).getPrice() + "");
                        jo.put("tickKM", arrcheck.get(i).getTickkm() + "");
                        jo.put("tickKM_percent", arrcheck.get(i).getTickkm2() + "");
                        jo.put("tickKM_money", arrcheck.get(i).getTickkm3() + "");
                        jo.put("barcode", arrcheck.get(i).getBarcode() + "");
                        jo.put("code", arrcheck.get(i).getCode() + "");
                        jo.put("title", arrcheck.get(i).getTitle() + "");
                        jo.put("money", arrcheck.get(i).getMoney() + "");
                        jo.put("note", arrcheck.get(i).getNote() + "");
                        jo.put("id", arrcheck.get(i).getId() + "");
                        jo.put("typeMoneyId", arrcheck.get(i).getTypeid());
                        jsonArray.put(jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            Log.d("HAJAR", jsonArray.toString());

            int money = 0;

            if (arrcheck.size() > 0) {
                for (int i = 0; i < arrcheck.size(); i++) {
                    money = Integer.parseInt(arrcheck.get(i).getMoney()) + money;
                }
            }

            money = money + money1;

            HashMap<String, String> map = new HashMap<>();
            map.put("accessToken", access);
            map.put("listProduct", jsonArray.toString());
            map.put("money", money + "");
            map.put("totalMoneyProduct", money + (money * (Integer.parseInt(tax))) / 100 + "");
            map.put("fullName", "");
            map.put("moneyShip", mnship + "");
            map.put("timeShiper", "");
            map.put("address", "");
            map.put("note", "");
            map.put("fone", "");
            // map.put("idUseronl",idu);
            map.put("idSeller", idsl);
//             Log.d("GTGT",  (money * (Integer.parseInt(tax)+1))/100+  "");
            Intent it = new Intent(getApplicationContext(), Preview.class);
            it.putExtra("map", map);
            it.putExtra("min", mnid);
            it.putExtra("stt", "nom");
            it.putExtra("tymn", tym);
            startActivity(it);
            pro.dismiss();
        } else {
            pro.dismiss();
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }

    }

    private void senKM2(String prices, String ship, String stt, int ns) {

        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        if (ses.loggedin()) {
            String quan = "1";

            int money1 = Integer.parseInt(quan) * Integer.parseInt(prices);
            JSONArray jsonArray = new JSONArray();
            JSONObject j1 = new JSONObject();

            try {
                j1.put("quantity", "1");
                j1.put("price", prices);
                j1.put("tickKM", "false");
                j1.put("tickKM_percent", "");
                j1.put("tickKM_money", "");
                j1.put("barcode", idprd);
                j1.put("code", prdcode);
                j1.put("title", titl);
                j1.put("money", money1 + "");
                j1.put("note", "");
                j1.put("id", idprd);
                j1.put("typeMoneyId", tym);
                jsonArray.put(j1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int money = 0;


            money = money + money1;

            HashMap<String, String> map = new HashMap<>();
            map.put("accessToken", access);
            map.put("listProduct", jsonArray.toString());
            map.put("money", money + "");
            map.put("totalMoneyProduct", money + (money * (Integer.parseInt(tax))) / 100 + "");
            map.put("fullName", "");
            map.put("moneyShip", ship);
            map.put("timeShiper", "");
            map.put("address", "");
            map.put("note", "");
            map.put("fone", "");
            // map.put("idUseronl",idu);
            map.put("idSeller", idsl);


            Intent it = new Intent(getApplicationContext(), Preview.class);
            it.putExtra("map", map);
            it.putExtra("min", mnid);
            it.putExtra("stt", stt);
            it.putExtra("num", ns);
            it.putExtra("tymn", tym);
            startActivity(it);
            pro.dismiss();
        } else {
            pro.dismiss();
            AlertDialog alertDialog = taoMotAlertDialog2();
            alertDialog.show();
        }

    }

    private void senKM(String mkm, String pers, String pride) {

        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        if (ses.loggedin()) {
            String quan = "1";

            int money1 = Integer.parseInt(quan) * Integer.parseInt(pride);
            JSONArray jsonArray = new JSONArray();
            JSONObject j1 = new JSONObject();

            try {
                j1.put("quantity", "1");
                j1.put("price", pride);
                j1.put("tickKM", "1");
                j1.put("tickKM_percent", pers);
                j1.put("tickKM_money", Integer.parseInt(priceprd) - Integer.parseInt(pride));
                j1.put("barcode", idprd);
                j1.put("code", prdcode);
                j1.put("title", titl);
                j1.put("money", pride + "");
                j1.put("note", "Sử dụng mã khuyến mại " + mkm);
                j1.put("id", idprd);
                j1.put("typeMoneyId", tym);
                jsonArray.put(j1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int money = 0;


            money = money + money1;

            HashMap<String, String> map = new HashMap<>();
            map.put("accessToken", access);
            map.put("listProduct", jsonArray.toString());
            map.put("money", money + "");
            map.put("totalMoneyProduct", money + (money * (Integer.parseInt(tax))) / 100 + "");
            map.put("fullName", "");
            map.put("moneyShip", strship.toString());
            map.put("timeShiper", "");
            map.put("address", "");
            map.put("note", "");
            map.put("fone", "");
            // map.put("idUseronl",idu);
            map.put("idSeller", idsl);

            Intent it = new Intent(getApplicationContext(), Preview.class);
            it.putExtra("map", map);
            it.putExtra("min", mnid);
            it.putExtra("stt", "");
            it.putExtra("tymn", tym);
            startActivity(it);
            pro.dismiss();
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

                    pr1.setVisibility(View.GONE);
                    Log.d("PDR", response);

                    JSONObject jo = new JSONObject(response);

                    JSONObject prd = jo.getJSONObject("Product");
                    if (prd.has("linkFacebook")) {
//                        lnf.setVisibility(View.VISIBLE);
                        linkfbb = prd.getString("linkFacebook");

                        content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(prd.getString("linkFacebook")))

                                .setShareHashtag(new ShareHashtag.Builder()
                                        .setHashtag("#NeedFood")
                                        .build())
                                .build();

                        shareDialog = new ShareDialog(ProductDetail.this);
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                @Override
                                public void onSuccess(Sharer.Result result) {
                                    Log.d("SHARE", "SUCC");
                                    saveShare();
                                }

                                @Override
                                public void onCancel() {
                                    Log.d("SHARE", "CANCEL");
                                }

                                @Override
                                public void onError(FacebookException exception) {

                                    Log.d("SHARE", "ERR");
                                }
                            });
                        }
                    }
                    JSONObject joo = prd.getJSONObject("vote");
                    point = joo.getString("point");
                    strship = new StringBuilder(prd.getString("moneyShip"));
                    listship.add(Integer.parseInt(strship.toString()));
                    JSONObject jos = prd.getJSONObject("discount");
                    JSONObject jos1 = jos.getJSONObject("discountStart");
                    text1 = jos1.getString("text");
                    time1 = jos1.getString("time");
                    JSONObject jos2 = jos.getJSONObject("discountEnd");
                    text2 = jos2.getString("text");
                    time2 = jos2.getString("time");
                    priceDiscount = jos.getString("priceDiscount");
                    discountCode = jos.getString("discountCode");
                    if (prd.has("numberShare")) {
                        numshare = prd.getInt("numberShare");

                        if (hot.equals("hot")) {
                            lnmyshare.setVisibility(View.VISIBLE);
                            tvphi.setText(numshare + "");
                            imgshare.setVisibility(View.VISIBLE);
                        } else {
                            lnmyshare.setVisibility(View.GONE);
                            imgshare.setVisibility(View.GONE);
                            //   tvphi.setText(numshare + "");
                        }


                    }
                    Log.d("SHARENUM", numshare + "");
                    howto = new StringBuilder("");
                    howto.append(prd.getString("info"));
                    cata = prd.getJSONArray("category").toString();
                    String title = prd.getString("title");
                    if (title.length() < 30) {
                        tvnameprd.setText(title);
                    } else {
                        tvnameprd.setText(title.substring(0, 30) + "...");
                    }

                    tym = prd.getString("typeMoneyId");
                    String dvs = prd.getString("nameUnit");
                    titl = prd.getString("title");
                    namesl = prd.getString("nameSeller");
                    prdcode = prd.getString("code");
                    bar = prd.getString("barcode");

                    if (sttsell.equals("true")) {
                        JSONObject selling = prd.getJSONObject("sellingOut");
                        priceprd = selling.getString("price");
                        priceother = prd.getString("price");
                    } else {
                        priceprd = prd.getString("price");
                        priceother = prd.getString("priceOther");
                    }

                    Intent i = new Intent(getApplicationContext(), BubbleService.class);
                    i.putExtra("MN", priceprd);
                    startService(i);

                    namesel.setText(namesl);
                    idsl = prd.getString("idSeller");
                    lnby.setVisibility(View.VISIBLE);
                    maniid = prd.getString("manufacturerId");
                    tvdv1.setText(dvs);
                    tvdv2.setText(dvs);
                    list = db.getMNid(tym);
                    for (ListMN lu : list) {
                        mnid = lu.getMn();
                        tvgia1.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(priceprd)) + lu.getMn());
                        tvgia2.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(priceother)) + lu.getMn(), TextView.BufferType.SPANNABLE);
                        Spannable spannable = (Spannable) tvgia2.getText();
                        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, tvgia2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvprize.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("price"))) + lu.getMn());
                    }
                    tvco.setText(prd.getString("code"));
                    tvcodes.setText(prd.getString("storeID"));

                    dess.setText(prd.getString("description"));
                    JSONArray ja = prd.getJSONArray("images");
                    simg = new StringBuilder("http://needfood.webmantan.com" + ja.getString(0));
                    Picasso.with(getApplicationContext()).load("http://needfood.webmantan.com" + ja.getString(0)).into(imgprd);
                    vote.setText(point);
                    JSONObject ex = prd.getJSONObject("expiryDate");
                    String txt_hsd = ex.getString("text");
                    String time_hsd = ex.getString("time");
                    ver.setVisibility(View.VISIBLE);
                    exp.setText(txt_hsd);
                    nameseller.setText(titl);
                    tvpr.setVisibility(View.VISIBLE);
                    getBrand();
                    getCommen();
                    getPrdCompo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }


    public void saveShare() {
        final String link = getResources().getString(R.string.linksaveShare);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", access);
        map.put("idSeller", idsl);
        map.put("numberShare", "1");

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EEEE", response);
                getNumberShare();

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

                Log.d("EEE", response);
                try {
                    JSONArray ja = new JSONArray(response);

                    for (int i = 0; i < ja.length(); i++) {
                        String mn = "";
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject prd = jo.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");
                        typemn = prd.getString("typeMoneyId");
                        list = db.getMNid(typemn);
                        for (ListMN lu : list) {
                            mn = lu.getMn();
                        }

                        arrq.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                prd.getString("price"), mn, prd.getString("nameUnit"), false, prd.getString("id"), prd.getString("code"),
                                "", prd.getString("id"), prd.getString("moneyShip"), typemn));


                    }
                    for (int i2 = 0; i2 < arrq.size(); i2++) {
                        if (arrq.get(i2).getId().equals(idprd)) {
                            arrq.remove(i2);
                        }
                    }

                    for (int i3 = 0; i3 < arrof.size(); i3++) {
                        for (int i4 = 0; i4 < arrq.size(); i4++) {
                            if (arrq.get(i4).getId().equals(arrof.get(i3).getId())) {
                                arrq.remove(i4);
                            }
                        }

                    }
                    for (int i3 = 0; i3 < arrof2.size(); i3++) {
                        for (int i4 = 0; i4 < arrq.size(); i4++) {
                            if (arrq.get(i4).getId().equals(arrof2.get(i3).getId())) {
                                arrq.remove(i4);
                            }
                        }

                    }
                    for (int i3 = 0; i3 < arrof3.size(); i3++) {
                        for (int i4 = 0; i4 < arrq.size(); i4++) {
                            if (arrq.get(i4).getId().equals(arrof3.get(i3).getId())) {
                                arrq.remove(i4);
                            }
                        }

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


    private void getBrand() {
        final String link = getResources().getString(R.string.linkifsel);
        Map<String, String> map = new HashMap<>();
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAXXX", response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject sl = jo.getJSONObject("Seller");
                    tax = sl.getString("taxNumber");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    public void getPrdCompo() {
        final String link = getResources().getString(R.string.linkprcom);

        Map<String, String> map = new HashMap<>();

        map.put("idProduct", idprd);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("RSEEE", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    if (ja.length() == 0) {
//                        txtcomp.setVisibility(View.GONE);
                    } else {
//                        txtcomp.setVisibility(View.VISIBLE);
                        for (int i = 0; i < ja.length(); i++) {
                            String mn = "";
                            JSONObject jo = ja.getJSONObject(i);
                            JSONObject prd = jo.getJSONObject("Product");
                            JSONArray jaimg = prd.getJSONArray("images");
                            typemn = prd.getString("typeMoneyId");
                            list = db.getMNid(typemn);
                            for (ListMN lu : list) {
                                mn = lu.getMn();
                            }

                            arrof3.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                    prd.getString("price"), mn, prd.getString("nameUnit"), false, prd.getString("id"), prd.getString("code"),
                                    "", prd.getString("id"), prd.getString("moneyShip"), typemn));

                        }
                        for (int i2 = 0; i2 < arrof3.size(); i2++) {
                            if (arrof3.get(i2).getId().equals(idprd)) {
                                arrof3.remove(i2);
                            }
                        }
                        adapterof3.notifyDataSetChanged();

                    }

//                    getPrdDH();
                    getPrdDK();
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
        map.put("limit", "7");
        map.put("category", cata);
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("RE", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    if (ja.length() == 0) {
                        txtof.setVisibility(View.GONE);
                    } else {
                        txtof.setVisibility(View.VISIBLE);
                        for (int i = 0; i < ja.length(); i++) {
                            String mn = "";
                            JSONObject jo = ja.getJSONObject(i);
                            JSONObject prd = jo.getJSONObject("Product");
                            JSONArray jaimg = prd.getJSONArray("images");
                            typemn = prd.getString("typeMoneyId");
                            list = db.getMNid(typemn);
                            for (ListMN lu : list) {
                                mn = lu.getMn();
                            }

                            arrof.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                    prd.getString("price"), mn, prd.getString("nameUnit"), false, prd.getString("id"), prd.getString("code"),
                                    "", prd.getString("id"), prd.getString("moneyShip"), typemn));

                        }
                        for (int i2 = 0; i2 < arrof.size(); i2++) {
                            if (arrof.get(i2).getId().equals(idprd)) {
                                arrof.remove(i2);
                            }
                        }
                        for (int i3 = 0; i3 < arrof3.size(); i3++) {
                            for (int i4 = 0; i4 < arrof.size(); i4++) {
                                if (arrof.get(i4).getId().equals(arrof3.get(i3).getId())) {
                                    arrof.remove(i4);
                                }
                            }

                        }
                        if (arrof.size() == 0) {
                            txtof.setVisibility(View.GONE);
                        }
                        adapterof1.notifyDataSetChanged();
                    }

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
        map.put("limit", "7");
        map.put("manufacturerId", maniid);
        map.put("idSeller", idsl);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("REDH", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    if (ja.length() == 0) {
                        txtbrand.setVisibility(View.GONE);
                    } else {
                        txtbrand.setVisibility(View.VISIBLE);
                        for (int i = 0; i < ja.length(); i++) {
                            String mn = "";
                            JSONObject jo = ja.getJSONObject(i);
                            JSONObject prd = jo.getJSONObject("Product");
                            JSONArray jaimg = prd.getJSONArray("images");
                            typemn = prd.getString("typeMoneyId");
                            list = db.getMNid(typemn);
                            for (ListMN lu : list) {
                                mn = lu.getMn();
                            }

                            arrof2.add(new OftenConstructor("http://needfood.webmantan.com" + jaimg.getString(0), prd.getString("title"),
                                    prd.getString("price"), mn, prd.getString("nameUnit"),
                                    false, prd.getString("id"), prd.getString("code"),
                                    "", prd.getString("id"), prd.getString("moneyShip"), typemn));
                        }
                        for (int i2 = 0; i2 < arrof2.size(); i2++) {
                            if (arrof2.get(i2).getId().equals(idprd)) {
                                arrof2.remove(i2);
                            }
                        }
                        for (int i3 = 0; i3 < arrof.size(); i3++) {
                            for (int i4 = 0; i4 < arrof2.size(); i4++) {
                                if (arrof2.get(i4).getId().equals(arrof.get(i3).getId())) {
                                    arrof2.remove(i4);
                                }
                            }

                        }
                        for (int i3 = 0; i3 < arrof3.size(); i3++) {
                            for (int i4 = 0; i4 < arrof2.size(); i4++) {
                                if (arrof2.get(i4).getId().equals(arrof3.get(i3).getId())) {
                                    arrof2.remove(i4);
                                }
                            }

                        }
                        if (arrof2.size() == 0) {
                            txtbrand.setVisibility(View.GONE);
                        }

                        adapterof2.notifyDataSetChanged();
                    }

                    getAtach();

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
        } else if (!ses.loggedin()) {
            AlertDialog alertDialog = taoMotAlertDialog2();
            alertDialog.show();
        } else {

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
        final TextView txt = (TextView) dialog.findViewById(R.id.tx_km);
        final EditText edpro = (EditText) dialog.findViewById(R.id.promotion);
        final LinearLayout lnn = (LinearLayout) dialog.findViewById(R.id.lnn);
        Calendar c = Calendar.getInstance();
        final double timenow = c.getTimeInMillis();
        final double a1 = Long.parseLong(time1);
        final double b = Long.parseLong(time2);


        Button clo = (Button) dialog.findViewById(R.id.proc);
        clo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = edpro.getText().toString();
                if(a.equals("")){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrreg), Toast.LENGTH_SHORT).show();
                }else if (!a.equals(discountCode)) {
                    getDiscountAdmin(a);


                } else {
                    if (a1 < timenow && timenow < b) {
                        txt.setVisibility(View.GONE);
                        lnn.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    } else {
                        senKM(a, "", priceDiscount);
                        dialog.dismiss();
                    }

                }

            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.img_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

    }

    public void getDiscountAdmin(final String codekm) {
        final String link = getResources().getString(R.string.linkgetdiscountadmin);
        Map<String, String> map = new HashMap<>();
        map.put("code", codekm);
        final int pri1 = Integer.parseInt(priceprd);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("REEEE", response);
                try {
                    JSONObject ja = new JSONObject(response);
                    String code = ja.getString("code");
                    if (code.equals("0")) {
                        percentkm = ja.getDouble("percent");
                        int pridekm = (int) ((pri1 / 100) * percentkm);
                        int pridekm2 = Integer.parseInt(String.valueOf(pri1 - pridekm));
                        Log.d("KM22", pridekm + "-" + pridekm2);
                        String pridekm3 = String.valueOf(pridekm2);
                        senKM(codekm, percentkm + "", pridekm3);
                    } else if (code.equals("-1")) {
                        AlertDialog alertDialog = taoMotAlertDialog();
                        alertDialog.show();
                    } else if (code.equals("1")) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.code1), Toast.LENGTH_SHORT).show();
                    } else if (code.equals("2")) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.code2), Toast.LENGTH_SHORT).show();
                    } else if (code.equals("3")) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.code3), Toast.LENGTH_SHORT).show();
                    } else if (code.equals("4")) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.code4), Toast.LENGTH_SHORT).show();
                    }
                    if (ses.loggedin()) {
                        getNumberShare();
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
                    if (ses.loggedin()) {
                        getNumberShare();
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
        if (v == img_comment) {
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
