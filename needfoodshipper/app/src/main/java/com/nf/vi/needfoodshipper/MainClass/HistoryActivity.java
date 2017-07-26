package com.nf.vi.needfoodshipper.MainClass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Adapter.HistoryAdapter;
import com.nf.vi.needfoodshipper.Adapter.MainAdapter;
import com.nf.vi.needfoodshipper.Constructor.HistoryConstructor;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.Constructor.WaittingContructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.HisoryRequest;
import com.nf.vi.needfoodshipper.Request.SearchRequest;
import com.nf.vi.needfoodshipper.Request.WaittingRequest;
import com.nf.vi.needfoodshipper.SupportClass.EndlessRecyclerViewScrollListener;
import com.nf.vi.needfoodshipper.SupportClass.EndlessScroll;
import com.nf.vi.needfoodshipper.SupportClass.WrapSliding;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    WrapSliding wrap;
    private DBHandle db;
    private List<ListUserContructor> list;
    private List<HistoryConstructor> listht;
    private HistoryAdapter adapter;
    private String ketqua = "", tk, key, dateStart, dateEnd;
    HistoryAdapter adapterh;
    ArrayList<HistoryConstructor> arr;
    private RecyclerView rcvHistory;
    private TextView tvTitle, thongbao, tvngayden, tvngaydi, tvBao;
    private ImageView imghand;
    private EditText edtSearch1;
    String timeLeftShip = "";
    SwipeRefreshLayout swipeRefresh;
    Button btnflitter;
    Calendar cal;
    String note;
    boolean checktime = false;
    Date date;

    private EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager linearLayoutManager;
    CountDownTimer ctime;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            tk = nu.getAccessToken();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvBao = (TextView) findViewById(R.id.tvBao);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.lstoobar));
        wrap = (WrapSliding) findViewById(R.id.slide);
        // code tim kiem
        thongbao = (TextView) findViewById(R.id.textflit);
        imghand = (ImageView) findViewById(R.id.imgflit);
        edtSearch1 = (EditText) findViewById(R.id.edtSearch1);
        tvngaydi = (TextView) findViewById(R.id.tvngaydi);
        tvngayden = (TextView) findViewById(R.id.tvngayden);
        btnflitter = (Button) findViewById(R.id.btnflitter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshhistory);
        rcvHistory = (RecyclerView) findViewById(R.id.rcvHistory);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        listht = new ArrayList<>();
        adapter = new HistoryAdapter(getApplicationContext(), listht);
        rcvHistory.setAdapter(adapter);
        order(1);
        rcvHistory.setLayoutManager(linearLayoutManager);
        swipeRefresh.setOnRefreshListener(this);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                page++;
                order(page);

            }
        };

        // Adds the scroll listener to RecyclerView
        rcvHistory.addOnScrollListener(scrollListener);


        wrap.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                imghand.setImageDrawable(getResources().getDrawable(R.drawable.downb));
                thongbao.setText(getString(R.string.lskeoxuong));
            }
        });
        wrap.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
//                fab.setVisibility(View.VISIBLE);
                imghand.setImageDrawable(getResources().getDrawable(R.drawable.upb));
                thongbao.setText(getString(R.string.lskeolen));
            }
        });
        tvngaydi.setOnClickListener(showDatePicker);
        tvngayden.setOnClickListener(showDatePicker1);
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        //Định dạng kiểu ngày / tháng /năm
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        //hiển thị lên giao diện
        tvngaydi.setText(strDate);
        tvngayden.setText(strDate);
        btnflitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

    }

    private void order(int page) {


        final String link = getResources().getString(R.string.getListOrderDoneShiperAPI);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("GG", response);
                    if (checktime == true) {
                        ctime.cancel();
                    }
                    if (response.equals("[]")) {
                        tvBao.setVisibility(View.VISIBLE);
                    }
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        StringBuilder sb = new StringBuilder();
                        String money;
                        JSONObject json = arr.getJSONObject(i);
                        JSONObject Order = json.getJSONObject("Order");

                        JSONObject listProduct = Order.getJSONObject("listProduct");
                        JSONObject infoOrder = Order.getJSONObject("infoOrder");
                        JSONObject infoCustomer = Order.getJSONObject("infoCustomer");


                        Iterator<String> ite = listProduct.keys();

                        while (ite.hasNext()) {
                            String key = ite.next();

                            JSONObject idx = listProduct.getJSONObject(key);
                            sb.append((idx.getString("quantity") + idx.getString("title")) + ";" + "\t");


                        }
                        String timeShiper = infoOrder.getString("timeShiper");

                        String fullName = infoCustomer.getString("fullName");
                        String fone = infoCustomer.getString("fone");
                        String address = infoCustomer.getString("address");
                        String id = Order.getString("id");
                        String status = Order.getString("status");
                        String code = Order.getString("code");
                        if (Order.has("timeLeftShip")) {
                            timeLeftShip = Order.getString("timeLeftShip");
                        }
                        if (Order.has("noteShiper")) {
                            note = Order.getString("noteShiper");
                        }

//                        Toast.makeText(getApplication(), note, Toast.LENGTH_LONG).show();

                        Log.d("hh", fullName);
                        listht.add(new HistoryConstructor(status, code, timeShiper, timeLeftShip, note));


                    }
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

        HisoryRequest loginRequest = new HisoryRequest(page + "", tk, link, response);
        RequestQueue queue = Volley.newRequestQueue(HistoryActivity.this);
        queue.add(loginRequest);
    }

    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                //Sự kiện khi click vào nút Done trên Dialog
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // Set text cho textView
                    tvngaydi.setText(day + "/" + (month + 1) + "/" + year);
                    //Lưu vết lại ngày mới cập nhật
                    cal.set(year, month, day);
                    date = cal.getTime();
                }
            };
            String s = tvngaydi.getText() + "";
            //Lấy ra chuỗi của textView Date
            String strArrtmp[] = s.split("/");
            int ngay = Integer.parseInt(strArrtmp[0]);
            int thang = Integer.parseInt(strArrtmp[1]) - 1;
            int nam = Integer.parseInt(strArrtmp[2]);
            //Hiển thị ra Dialog
            DatePickerDialog pic = new DatePickerDialog(
                    HistoryActivity.this,
                    callback, nam, thang, ngay);
            pic.setTitle("Chọn ngày bắt đầu");
            pic.show();
        }
    };
    View.OnClickListener showDatePicker1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                //Sự kiện khi click vào nút Done trên Dialog
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // Set text cho textView
                    tvngayden.setText(day + "/" + (month + 1) + "/" + year);
                    //Lưu vết lại ngày mới cập nhật
                    cal.set(year, month, day);
                    date = cal.getTime();
                }
            };
            String s = tvngayden.getText() + "";
            //Lấy ra chuỗi của textView Date
            String strArrtmp[] = s.split("/");
            int ngay = Integer.parseInt(strArrtmp[0]);
            int thang = Integer.parseInt(strArrtmp[1]) - 1;
            int nam = Integer.parseInt(strArrtmp[2]);
            //Hiển thị ra Dialog
            DatePickerDialog pic = new DatePickerDialog(
                    HistoryActivity.this,
                    callback, nam, thang, ngay);
            pic.setTitle("Chọn ngày kết thúc");
            pic.show();
        }
    };

    private void search() {
        listht.clear();
        adapter.notifyDataSetChanged();

        String page1 = "1";
        key = edtSearch1.getText().toString();
        dateStart = tvngaydi.getText().toString();
        dateEnd = tvngayden.getText().toString();
        final String link = getResources().getString(R.string.searchOrderShiperAPI);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("GG", response);
                    if (response.equals("[]")) {
                        tvBao.setVisibility(View.VISIBLE);
                    }

                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        StringBuilder sb = new StringBuilder();
                        String money;
                        JSONObject json = arr.getJSONObject(i);
                        JSONObject Order = json.getJSONObject("Order");

                        JSONObject listProduct = Order.getJSONObject("listProduct");
                        JSONObject infoOrder = Order.getJSONObject("infoOrder");
                        JSONObject infoCustomer = Order.getJSONObject("infoCustomer");


                        Iterator<String> ite = listProduct.keys();

                        while (ite.hasNext()) {
                            String key = ite.next();

                            JSONObject idx = listProduct.getJSONObject(key);
                            sb.append((idx.getString("quantity") + idx.getString("title")) + ";" + "\t");


                        }
                        String timeShiper = infoOrder.getString("timeShiper");

                        String fullName = infoCustomer.getString("fullName");
                        String fone = infoCustomer.getString("fone");
                        String address = infoCustomer.getString("address");
                        String id = Order.getString("id");
                        String status = Order.getString("status");
                        String code = Order.getString("code");
                        if (Order.has("timeLeftShip")) {
                            timeLeftShip = Order.getString("timeLeftShip");
                        }

                        if (Order.has("noteShiper")) {
                            note = Order.getString("noteShiper");
                        }

                        Log.d("111111", fullName);
                        listht.add(new HistoryConstructor(status, code, timeShiper, timeLeftShip, note));


                    }
                    adapter.notifyDataSetChanged();
//                    swipeRefresh.setRefreshing(false);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        };

        SearchRequest loginRequest = new SearchRequest(page1, tk, key, dateStart, dateEnd, link, response);
        RequestQueue queue = Volley.newRequestQueue(HistoryActivity.this);
        queue.add(loginRequest);
    }

    @Override
    public void onRefresh() {
        listht.clear();
        adapter.notifyDataSetChanged();
        ctime = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                checktime = true;
                order(1);
            }

            @Override
            public void onFinish() {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getApplicationContext(), getString(R.string.dhloiketnoi), Toast.LENGTH_SHORT).show();
            }

        };
        ctime.start();

    }
}
